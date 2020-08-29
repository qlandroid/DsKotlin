package com.feiling.dasong.ui.function.group

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.model.GroupMemberModel
import com.feiling.dasong.ui.function.arrange.EmployeeSelectFragment
import com.feiling.dasong.uitils.ExceptionUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_group_details_insert.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/28
 * @author ql
 */
class GroupDetailsInsertFragment : BaseFragment(), IGroupDetailsPresenter {

    companion object {
        const val KEY_CODE = "GROUP_CODE"
        const val REQUEST_EMPLOYEE = 20

        fun instance(code: String): GroupDetailsInsertFragment {

            val fragment = GroupDetailsInsertFragment()
            val bundle = Bundle()

            bundle.putString(KEY_CODE, code);
            fragment.arguments = bundle;
            return fragment
        }
    }

    private var mMemberList: MutableList<GroupMemberModel> = mutableListOf()


    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_group_details_insert, null)
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.addLeftBackImageButton().onClick { popBackStack() }

        topbar.setTitle("组详细信息")

        groupDetailsInsertView.presenter = this;
        groupDetailsInsertView.setGroupMember(mMemberList)
    }


    override fun onClickEditGroupName() {
        val builder = QMUIDialog.EditTextDialogBuilder(context)
            .setTitle("组的名称编辑")
            .setPlaceholder("请输入组的名称")
            .setCancelable(false)
        builder.addAction("确定") { qmuiDialog: QMUIDialog, i: Int ->
            qmuiDialog.dismiss()
            if (builder.editText.text.isEmpty()) {
                mDialogTipHelper.showFailTip("请输入名称", topbar) {
                    mDialogTipHelper.dismissFail()

                }
            }
        }
            .addAction("取消") { qmuiDialog: QMUIDialog, i: Int ->
                qmuiDialog.dismiss()
            }
        builder.show()
    }

    override fun onClickAddMember() {
        startFragmentForResult(EmployeeSelectFragment(), REQUEST_EMPLOYEE)
    }

    override fun onClickRemoveMember(position: Int?) {
        val memberModel = position?.let { mMemberList[it] }
        QMUIDialog.MessageDialogBuilder(context)
            .setTitle("删除提示")
            .setCancelable(false)
            .setMessage("确定要移出\r\n组员:${memberModel?.firstname}\r\n编号:${memberModel?.employeeCode}")
            .addAction("确定") { dialog, index ->
                mMemberList.remove(memberModel)
                groupDetailsInsertView.setGroupMember(mMemberList)
                dialog.dismiss()
            }
            .addAction("取消") { qmuiDialog: QMUIDialog, i: Int ->
                qmuiDialog.dismiss()
            }
            .show()
    }


    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_EMPLOYEE -> {
                if (resultCode == RESULT_OK) {
                    val employeeModel = EmployeeSelectFragment.getEmployeeModel(data!!)
                    val subscribe = Observable.fromIterable(mMemberList)
                        .filter {
                            it.employeeCode == employeeModel.code
                        }.subscribe({
                            throw DsException("成员${it.firstname}已经存在不能重复添加")
                        }, {
                            val msg = ExceptionUtils.getExceptionMessage(it)
                            mDialogTipHelper.showFailTip(msg, topbar) {
                                mDialogTipHelper.dismissFail()
                            }
                        }, {
                            val groupMemberModel = GroupMemberModel()
                            groupMemberModel.firstname = employeeModel.firstname
                            groupMemberModel.employeeCode = employeeModel.code
                            mMemberList.add(groupMemberModel)
                            groupDetailsInsertView.setGroupMember(mMemberList)
                        })
                    RxApiManager.instance.add(this, subscribe)


                }
            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }
    }
}