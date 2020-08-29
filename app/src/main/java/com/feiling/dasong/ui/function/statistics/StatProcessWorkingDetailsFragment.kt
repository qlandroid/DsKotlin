package com.feiling.dasong.ui.function.statistics

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.isNumber
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.process.ProcessTimeStatisticsModel
import com.feiling.dasong.ui.function.arrange.TaskDetailsFragment
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.feiling.dasong.uitils.LoginUtils
import com.feiling.dasong.uitils.StringUtils
import com.qmuiteam.qmui.kotlin.onClick
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_employee_working_stat.emptyView
import kotlinx.android.synthetic.main.fragment_employee_working_stat.topbar
import kotlinx.android.synthetic.main.fragment_stat_process_working_details.*

/**
 * 描述：员工列表 并统计工时
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/16
 * @author ql
 */
class StatProcessWorkingDetailsFragment : BaseFragment() {


    private lateinit var mView: IStatProcessWorkingDetailsView;

    private lateinit var mProcessWorkingHourCode: String

    private lateinit var mProcessTimeStatisticsModel: ProcessTimeStatisticsModel;

    companion object {
        const val KEY_CODE = "KEY-CODE"
        /**
         * @param processWorkingHourCode 工序工时统计的 唯一编码
         **/
        fun instance(processWorkingHourCode: String): StatProcessWorkingDetailsFragment {

            var bundle = Bundle()
            bundle.putString(KEY_CODE, processWorkingHourCode)
            var fragment = StatProcessWorkingDetailsFragment()
            fragment.arguments = bundle;
            return fragment

        }
    }


    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_stat_process_working_details, null)
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)

        mProcessWorkingHourCode = arguments?.getString(KEY_CODE)!!

        topbar.addLeftBackImageButton().onClick { popBackStack() }
        topbar.setTitle("工序工时详情")

        statProcessWorkingDetails.presenter = Presenter()

        mView = statProcessWorkingDetails;


        var userModel = LoginUtils.getUserModel(context)
        userModel?.isSectionChief?.let { mView.setShowAudit(it) }
        emptyView.showEmptyView(true)


        topbar.addRightTextButton("刷新", R.id.topbar_right_yes_button)
            .onClick { loadData() }
        loadData();
    }


    private fun loadData() {
        var subscribe =
            ServiceBuild.processService.getEmployeeWorkingHourFormProcess(mProcessWorkingHourCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (it.isFailed) {
                        throw DsException(it)
                    }
                    it.getData(ProcessTimeStatisticsModel::class.java)
                }
                .subscribe({
                    mProcessTimeStatisticsModel = it;


                    if (!TextUtils.isEmpty(it.vstatus)) {
                        mView.setAuditStatus(it.vstatus!!)
                    }

                    LoginUtils.getUserModel(context)?.let { UserModel ->
                        if (UserModel.isSectionChief) {
                            //工段长
                            var srcTimer = it.srcTimer
                            mView.setPlanWorkingTimer(StringUtils.replaceNumber(srcTimer.orEmpty()))
                            mView.setAuditWorkingTimer(StringUtils.replaceNumber(it.repairTimer.orEmpty()))
                        } else {
                            //普通员工 如果已经审核 则查看的是已审核的内容，
                            var s = if (it.isAuditWorkingTimer) it.repairTimer else it.srcTimer
                            mView.setPlanWorkingTimer(StringUtils.replaceNumber(s.orEmpty()))
                        }
                    }




                    mView.setUserDetails(
                        mutableListOf(
                            LabelTextModel("姓名", it.memberName),
                            LabelTextModel("编号", it.memberCode)
                        )
                    )
                    mView.setProcessDetails(
                        mutableListOf(
                            LabelTextModel("工序名称", it.opProcess?.pmname),
                            LabelTextModel("工序编码", it.opProcess?.pmcode),
                            LabelTextModel("合同号", it.opProcess?.contractnum),
                            LabelTextModel("订单号", it.opProcess?.porder)
                        )
                    )


                }, {
                    it.printStackTrace()
                    runUiThread {
                        mDialogTipHelper.dismissLoading()
                        emptyView.showFailed(msg = ExceptionUtils.getExceptionMessage(it)) {
                            loadData()
                        }
                    }
                }, {
                    emptyView.hideEmpty()
                    mDialogTipHelper.dismissLoading()
                })
        addDisposable( subscribe)
    }

    private fun submitAuditWorkingTimer(workingTimer: Int) {
        mDialogTipHelper.showLoading("提交中")
        var subscribe =
            ServiceBuild.processService.actionWorkingHourFormProcess(
                mProcessWorkingHourCode,
                workingTimer
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (it.isFailed) {
                        throw DsException(it)
                    }
                    it
                }
                .subscribe({
                    if (it.isFailed) {
                        throw DsException(it)
                    }
                    mDialogTipHelper.showSuccessTip("提交成功", topbar) { dialog ->
                        mDialogTipHelper.dismissSuccess()
                        emptyView.showEmptyView(true)
                        loadData()
                    }
                }, {
                    it.printStackTrace()
                    runUiThread {
                        mDialogTipHelper.dismissLoading()
                        emptyView.showFailed(msg = ExceptionUtils.getExceptionMessage(it)) {
                            loadData()
                        }
                    }
                }, {
                    mDialogTipHelper.dismissLoading()
                })
        addDisposable(subscribe)
    }

    inner class Presenter : IStatProcessWorkingDetailsPresenter {
        override fun actionAudit(auditTimer: String) {
            if (!auditTimer.isNumber()) {
                mDialogTipHelper.displayMsgDialog(msg = "请输入正确的整数")
                return
            }
            var toInt = auditTimer.toInt()
            submitAuditWorkingTimer(toInt)

        }

        override fun actionToProcessDetails() {
            var instance =
                TaskDetailsFragment.instance(mProcessTimeStatisticsModel.opProcess?.ccode.orEmpty())

            startFragment(instance)

        }

    }


}