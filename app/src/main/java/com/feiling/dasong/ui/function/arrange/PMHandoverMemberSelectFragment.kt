package com.feiling.dasong.ui.function.arrange

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.chad.library.adapter.base.entity.node.BaseNode
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.comm.response
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.EmployeeModel
import com.feiling.dasong.model.GroupModel
import com.feiling.dasong.ui.function.arrange.adapter.ChildrenNode
import com.feiling.dasong.ui.function.arrange.adapter.GroupNode
import com.feiling.dasong.ui.function.arrange.adapter.GroupNodeProvider
import com.feiling.dasong.ui.function.arrange.adapter.GroupSelectAdapter
import com.feiling.dasong.uitils.ExceptionUtils
import com.feiling.dasong.widget.EmptyView
import com.google.gson.JsonElement
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：工序负责人 选择
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/27
 * @author ql
 */
class PMHandoverMemberSelectFragment : CommSelectFragment() {

    var pmcode: String? = null

    var memberAllModel: ProcessHandoverMemberModel? = null;
    var mFilterList: MutableList<BaseNode> = mutableListOf();
    var mGroupNodeList: MutableList<BaseNode> = mutableListOf()
    var mGroupSelectAdapter: GroupSelectAdapter = GroupSelectAdapter();


    companion object {
        private const val KEY_PM_CODE = "KEY_PM_CODE"
        private const val NODE_TYPE_EMPLOYEE = "EMPLOYEE"
        private const val NODE_TYPE_GROUP = "GROUP"


        const val TYPE_GROUP = 0;
        const val TYPE_MEMBER = 1;

        /**
         * 返回值类型  {TYPE_GROUP}0-组 ，{TYPE_MEMBER}1-人
         */
        private const val RESULT_KEY_TYPE = "RESULT_KEY_TYPE"
        private const val RESULT_KEY_CODE = "RESULT_KEY_CODE"
        private const val RESULT_KEY_NAME = "RESULT_KEY_NAME"
        fun instance(pmcode: String?): PMHandoverMemberSelectFragment {
            var bundle = Bundle()
            bundle.putString(KEY_PM_CODE, pmcode)

            var fragment = PMHandoverMemberSelectFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun getType(data: Intent?): Int? {
            return data?.getIntExtra(RESULT_KEY_TYPE, -1)
        }

        fun getCode(data: Intent?): String? {
            return data?.getStringExtra(RESULT_KEY_CODE)
        }

        fun getName(data: Intent?): String? {
            return data?.getStringExtra(RESULT_KEY_NAME)
        }
    }

    override fun initData() {
        super.initData()
        arguments?.let {
            pmcode = it.getString(KEY_PM_CODE)
        }
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        addLeftBack()
            .setTitle("工序交接人员选择")
            .setOpenRefresh(false)
            .setShowInputGroup(true, "请输入需要交接人员姓名")
        comm_select_rv.adapter = mGroupSelectAdapter
        comm_slt_pull_layout.setOnRefreshListener {
            loadData()
        }
        comm_search_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mFilterList.clear()
                addDisposable(
                    Observable.fromIterable(mGroupNodeList)
                        .filter {
                            it as GroupNode
                            it.code!!.contains(s.toString())
                                    || it.name!!.contains(s.toString())
                                    || isHasMemberByGroup(s.toString(), it)
                        }
                        .subscribe({ it ->
                            mFilterList.add(it)
                        }, {

                        }, {
                            mGroupSelectAdapter.notifyDataSetChanged()
                        })
                )
            }


        })
        mGroupSelectAdapter.setOnGroupSelectListener(object :
            GroupNodeProvider.OnSelectGroupListener {
            override fun onClick(position: Int) {
                val intent = Intent()
                var groupNode = mFilterList[position] as GroupNode

                if (groupNode.type == NODE_TYPE_GROUP) {
                    //选择的是 组
                    intent.putExtra(RESULT_KEY_TYPE, TYPE_GROUP)

                } else if (groupNode.type == NODE_TYPE_EMPLOYEE) {
                    //选择的是 人
                    intent.putExtra(RESULT_KEY_TYPE, TYPE_MEMBER)

                }
                intent.putExtra(RESULT_KEY_CODE, groupNode.code)
                intent.putExtra(RESULT_KEY_NAME, groupNode.name)

                setFragmentResult(RESULT_OK, intent)
                popBackStack()
            }
        })



        emptyView.showEmptyView(true, null, getString(R.string.loading_data), null, null)
        loadData()
    }

    private fun isHasMemberByGroup(key: String, group: GroupNode): Boolean {
        val opGroupMembers = group.childNode
        if (opGroupMembers != null) {
            for (item in opGroupMembers) {
                item as ChildrenNode
                if (item.code!!.contains(key)) {
                    return true
                }
                if (item.name!!.contains(key)) {
                    return true
                }

            }
        }
        return false;
    }

    private fun loadData() {
        addDisposable(
            ServiceBuild.processService.getMemberListFromProcessDev(pmcode)
                .response()
                .map {
                    it.getData(ProcessHandoverMemberModel::class.java)
                }
                .subscribe({
                    memberAllModel = it;
                    mFilterList.clear();
                    mGroupNodeList.clear();

                    it.grupsList?.forEach { groupModel ->
                        val groupNode = GroupNode(groupModel.gruopName, groupModel.groupCode)
                        groupNode.type = NODE_TYPE_GROUP

                        groupNode.childNode = mutableListOf<BaseNode>()
                        groupNode.isExpanded = false
                        mGroupNodeList.add(groupNode)
                        //给组添加人员
                        groupModel.opGroupMembers?.forEach { memberModel ->
                            val childrenNode =
                                ChildrenNode(memberModel.firstname, memberModel.employeeCode);
                            groupNode!!.childNode!!.add(childrenNode)
                        }
                    }
                    it.personnelsList?.forEach { employeeModel: EmployeeModel ->
                        val groupNode = GroupNode(employeeModel.name, employeeModel.code)
                        groupNode.type = NODE_TYPE_EMPLOYEE
                        groupNode.childNode = mutableListOf<BaseNode>()
                        groupNode.isExpanded = false
                        mGroupNodeList.add(groupNode)
                    }
                    mGroupNodeList.forEach { baseNode ->
                        this.mFilterList.add(baseNode)
                    }
                    mGroupSelectAdapter.setNewData(mFilterList)
                    mGroupSelectAdapter.notifyDataSetChanged()
                    this.hideEmpty()
                }, {

                }, {

                })
        )


    }

}

class ProcessHandoverMemberModel {
    /**
     *
    grupsList (Array[OpGroup]): 操作组列表 ,
    personnelsList (Array[Employees]): 操作人列表
     */

    var grupsList: MutableList<GroupModel>? = null;
    var personnelsList: MutableList<EmployeeModel>? = null;
}