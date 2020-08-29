package com.feiling.dasong.ui.function.arrange

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.chad.library.adapter.base.entity.node.BaseNode
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.GroupModel
import com.feiling.dasong.ui.function.arrange.adapter.ChildrenNode
import com.feiling.dasong.ui.function.arrange.adapter.GroupSelectAdapter
import com.feiling.dasong.ui.function.arrange.adapter.GroupNode
import com.feiling.dasong.ui.function.arrange.adapter.GroupNodeProvider
import com.feiling.dasong.uitils.ExceptionUtils
import com.feiling.dasong.widget.EmptyView
import com.google.gson.JsonElement
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：组搜索
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class GroupSelectFragment : CommSelectFragment() {
    var mAllList: MutableList<GroupModel> = mutableListOf();
    var mFilterList: MutableList<GroupModel> = mutableListOf();
    var mGroupNodeList: MutableList<BaseNode> = mutableListOf()
    var mGroupSelectAdapter: GroupSelectAdapter = GroupSelectAdapter();


    companion object {
        fun getData(data: Intent): GroupModel {
            return C.sGson.fromJson(data.getStringExtra("data"), GroupModel::class.java);
        }
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("操作组选择")
        topbar.addLeftBackImageButton().setOnClickListener {
            popBackStack()
        }
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
                mGroupNodeList.clear()
                var tempGroupNode: GroupNode? = null;
                val subscribe = Observable.fromIterable(mAllList)
                    .filter {
                        it.groupCode!!.contains(s.toString())
                                || it.gruopName!!.contains(s.toString())
                                || isHasMemberByGroup(s.toString(), it)
                    }.flatMap {
                        mFilterList.add(it)
                        tempGroupNode = GroupNode(it.gruopName, it.groupCode)
                        tempGroupNode!!.childNode = mutableListOf<BaseNode>();
                        mGroupNodeList.add(tempGroupNode!!)
                        Observable.fromIterable(it.opGroupMembers)
                    }
                    .subscribe({ it ->
                        tempGroupNode!!.childNode!!.add(ChildrenNode(it.firstname, it.employeeCode))
                    }, {

                    }, {
                        mGroupSelectAdapter.setNewData(mGroupNodeList)
                        mGroupSelectAdapter.notifyDataSetChanged()
                    })
                RxApiManager.instance.add(this@GroupSelectFragment, subscribe)
            }


        })
        comm_input_group.visibility =View.VISIBLE
        mGroupSelectAdapter.setOnGroupSelectListener(object :
            GroupNodeProvider.OnSelectGroupListener {
            override fun onClick(position: Int) {
                val intent = Intent()
                intent.putExtra("data", C.sGson.toJson(mFilterList[position]))
                setFragmentResult(RESULT_OK, intent)
                popBackStack()
            }
        })



        emptyView.showEmptyView(true, null, getString(R.string.loading_data), null, null)
        loadData()
    }

    private fun isHasMemberByGroup(key: String, group: GroupModel): Boolean {
        val opGroupMembers = group.opGroupMembers
        if (opGroupMembers != null) {
            for (item in opGroupMembers) {
                if (item.employeeCode!!.contains(key)) {
                    return true
                }
                if (item.firstname!!.contains(key)) {
                    return true
                }

            }
        }
        return false;
    }

    private fun loadData() {
        var groupNode: GroupNode? = null;

        val subscribe = ServiceBuild.groupService.getOpGroupAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap() { t ->
                if (t.isFailed) {
                    throw DsException(t.message!!)
                }
                if (comm_slt_pull_layout.isRefreshing) {
                    mGroupSelectAdapter.data.clear()
                }
                mAllList.clear()
                mFilterList.clear();
                mGroupNodeList.clear();
                Observable.fromIterable<JsonElement>(t.data!!.asJsonArray)
            }
            .map { t ->
                val groupModel = C.sGson.fromJson(t, GroupModel::class.java)
                mAllList.add(groupModel)
                mFilterList.add(groupModel)
                groupModel
            }
            .flatMap {
                groupNode = GroupNode(it.gruopName, it.groupCode)
                groupNode!!.childNode = mutableListOf<BaseNode>()
                groupNode!!.isExpanded = false
                mGroupNodeList.add(groupNode!!)
                Observable.fromIterable(it.opGroupMembers)
            }
            .subscribe({ member ->
                val childrenNode = ChildrenNode(member.firstname, member.employeeCode);
                groupNode!!.childNode!!.add(childrenNode)
            }, {
                it.printStackTrace()
                emptyView.post {
                    var msg: CharSequence? = ExceptionUtils.getExceptionMessage(it)
                    if (it is DsException) {
                        msg = it.msg;
                    }
                    emptyView.showFailed(msg = msg!!.toString())

                }
            }, {
                if (emptyView.isShowing) {
                    if (mAllList.isEmpty()) {
                        emptyView.showEmpty();
                    } else {
                        emptyView.hideEmpty()
                        comm_slt_pull_layout.visibility = View.VISIBLE
                    }
                }

                if (mAllList.isNotEmpty()) {
                    val adapterEmptyView: EmptyView = EmptyView(context);
                    adapterEmptyView.showEmpty(msg = "没有过滤到信息");
                    mGroupSelectAdapter.setEmptyView(adapterEmptyView)
                }

                if (comm_slt_pull_layout.isRefreshing) {
                    comm_slt_pull_layout.isRefreshing = false;
                }
                mGroupSelectAdapter.setNewData(mGroupNodeList)
                mGroupSelectAdapter.loadMoreModule?.loadMoreEnd()
            })

        RxApiManager.instance.add(this, subscribe)


    }


}