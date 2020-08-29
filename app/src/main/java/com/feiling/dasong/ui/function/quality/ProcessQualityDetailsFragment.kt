package com.feiling.dasong.ui.function.quality

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.process.ProcessModel
import com.feiling.dasong.ui.function.arrange.ProcessRecodeFragment
import com.feiling.dasong.ui.function.arrange.ProcessTimerStatFragment
import com.feiling.dasong.ui.function.model.NavData
import com.feiling.dasong.uitils.ExceptionUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_process_check_details.*

/**
 * 描述：工序质检详情
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/27
 * @author ql
 */
class ProcessQualityDetailsFragment : BaseFragment(), IProcessQualityPresenter {

    companion object {
        private const val KEY_CODE = "KEY_CODE";

        /**
         * 质检失败填写的描述信息
         */
        const val REQUEST_FAILED_MSG = 1002

        /**
         * 让步接收 必须填写消息内容
         */
        const val REQUEST_CONCESSION = 1003

        fun instance(code: String): ProcessQualityDetailsFragment {
            val fragment = ProcessQualityDetailsFragment();

            val args = Bundle();
            args.putString(KEY_CODE, code);

            fragment.arguments = args;
            return fragment;
        }
    }

    private lateinit var mProcessModel: ProcessModel

    private lateinit var mProcessCode: String


    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_process_check_details, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.addLeftBackImageButton().setOnClickListener { popBackStack() }

        processCheckDetailsView.presenter = this;

        mProcessCode = arguments?.getString(KEY_CODE)!!
        topbar.setTitle(mProcessCode)


        loadData();
    }

    private fun loadData() {
        emptyView.showEmptyView(loading = true)

        val subscribe = ServiceBuild.processService.qualityProcessGetById(mProcessCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isFailed) {
                    throw DsException(it)
                }

                it.getData(ProcessModel::class.java)
            }
            .subscribe(
                {
                    emptyView.hideEmpty()
                    processCheckDetailsView.setShowFailedView(false)
                    mProcessModel = it;
                    topbar.setTitle(it.pmname)
                    it.stan8.let { msg ->
                        if (msg.isNullOrEmpty()) {
                            return@let
                        }
                        processCheckDetailsView.setShowFailedView(true)
                        processCheckDetailsView.setShowFailedMsg(msg)
                    }

                    val textView = TextView(context)
                    textView.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.qmui_config_color_white
                        )
                    )
                    textView.textSize = 12f
                    val layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    topbar.addRightImageButton(
                        R.drawable.btn_nav_more,
                        R.id.topbar_right_yes_button
                    )
                        .onClick {
                            displayMoreMenu()
                        }
                    var status: ProcessModel.StatusType =
                        ProcessModel.StatusType.getStatus(it.opState)
                    var statueTextColor = Color.BLUE
                    when (status) {
                        ProcessModel.StatusType.PENDING, ProcessModel.StatusType.DEFAULT -> {
                            processCheckDetailsView.initDefault()
                            textView.text = "待开工"
                        }
                        ProcessModel.StatusType.PAUSED -> {
                            processCheckDetailsView.initPause();
                            textView.text = "已暂停"
                        }
                        ProcessModel.StatusType.STARTED -> {
                            processCheckDetailsView.initStarted();
                            textView.text = "已开工"
                        }
                        ProcessModel.StatusType.ENDED -> {
                            when (mProcessModel.qualityState) {
                                ProcessModel.CheckStatus.OK.status -> {
                                    textView.text = getString(R.string.quality_btn_ok)
                                    processCheckDetailsView.initOk()
                                    statueTextColor = ContextCompat.getColor(context!!,R.color.green_0)
                                }
                                ProcessModel.CheckStatus.FAILED.status -> {
                                    processCheckDetailsView.initFailed()
                                    statueTextColor = ContextCompat.getColor(context!!,R.color.app_color_red)

                                    textView.text = getString(R.string.quality_btn_failed)
                                }
                                ProcessModel.CheckStatus.CONCESSION.status -> {
                                    processCheckDetailsView.initConcession()
                                    statueTextColor = ContextCompat.getColor(context!!,R.color.qmui_config_color_50_blue)

                                    textView.text = getString(R.string.quality_btn_concession)
                                }

                                else -> {
                                }
                            }

                        }
                    }
                    processCheckDetailsView.setProcessDetails(
                        NavData.ProcessViewModel(
                            it.pmname.orEmpty()
                            , it.pmcode.orEmpty()
                            , textView.text.toString()
                            , it.porder.orEmpty()
                            , it.ddate.orEmpty(),
                            statusColor = statueTextColor
                        )
                    )

                    processCheckDetailsView.setProductDetails(
                        NavData.ProductViewModel(
                            it.custabbname.orEmpty(),
                            it.contractnum.orEmpty(),
                            it.pinvcode.orEmpty(),
                            it.invname.orEmpty()
                        )
                    )

                },
                {
                    val msg = ExceptionUtils.getExceptionMessage(it)
                    topbar?.post {
                        emptyView.showFailed(msg = msg) {
                            emptyView.showEmptyView()
                            loadData()
                        }
                    }
                },
                {

                })
        RxApiManager.instance.add(this, subscribe)

    }

    override fun onProcessCheckStart() {
        mDialogTipHelper.showLoading(getString(R.string.submiting))
        val subscribe = ServiceBuild.processService.qualityProcessStart(mProcessCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if (it.isFailed) {
                        throw DsException(it)
                    }
                    mDialogTipHelper.showSuccessTip(getString(R.string.submit_success), topbar) {
                        mDialogTipHelper.dismissSuccess()
                        setFragmentResult(RESULT_OK, null)
                        popBackStack()
                    }
                }, {
                    mDialogTipHelper.dismissLoading()
                    val msg = ExceptionUtils.getExceptionMessage(it)
                    topbar?.post {
                        mDialogTipHelper.showFailTip(msg, topbar) {
                            mDialogTipHelper.dismissFail()
                        }
                    }
                }, {
                    mDialogTipHelper.dismissLoading()
                }
            )
        RxApiManager.instance.add(this, subscribe)
    }

    override fun onProcessCheckEnd() {
        val ok = getString(R.string.quality_btn_ok)
        val failed = getString(R.string.quality_btn_failed)
        val concession = getString(R.string.quality_btn_concession)
        QMUIBottomSheet.BottomListSheetBuilder(context)
            .addItem(R.drawable.quality_ok, ok, ok)
            .addItem(R.drawable.quality_concession, concession, concession)
            .addItem(R.drawable.quality_failed, failed, failed)
            .setGravityCenter(true)
            .setRadius(QMUIDisplayHelper.dp2px(context, 10))
            .setCancelText(getString(R.string.btn_cancel))
            .setTitle(getString(R.string.pleace_select_opt_content))
            .setAddCancelBtn(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                dialog.dismiss()
                when (tag) {
                    ok -> {
                        handleProcessOK()
                    }
                    failed -> {
                        toFailedFragment(REQUEST_FAILED_MSG)
                    }
                    concession -> {
                        toFailedFragment(REQUEST_CONCESSION)
                    }
                    else -> {
                    }
                }
            }.build().show()

    }

    override fun onProcessCheckPause() {
        mDialogTipHelper.showLoading("提交中")
        val subscribe = ServiceBuild.processService.qualityProcessPause(mProcessCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isFailed) {
                    throw DsException(it)
                }
                mDialogTipHelper.showSuccessTip("操作成功", topbar) {
                    mDialogTipHelper.dismissSuccess()
                    setFragmentResult(RESULT_OK, null)
                    popBackStack()
                }
            }, {

                val httpExceptionMessage = ExceptionUtils.getExceptionMessage(it)
                topbar?.post {
                    mDialogTipHelper.dismissLoading()
                    mDialogTipHelper.showFailTip(httpExceptionMessage, topbar) {
                        mDialogTipHelper.dismissFail()
                    }
                }

            }, {
                mDialogTipHelper.dismissLoading()
            })
        RxApiManager.instance.add(this, subscribe)

    }


    private fun toFailedFragment(requestCode: Int) {
        startFragmentForResult(FailedMsgFragment(), requestCode);
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_FAILED_MSG -> {
                if (resultCode == RESULT_OK) {
                    val msg: String? = FailedMsgFragment.getData(data)
                    actionProcessFailed(msg)
                }
            }
            REQUEST_CONCESSION -> {
                if (resultCode == RESULT_OK) {
                    val msg: String? = FailedMsgFragment.getData(data)
                    actionProcessConcession(msg)
                }
            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }
    }


    private fun handleProcessOK() {
        mDialogTipHelper.showLoading("提交中")
        val subscribe = ServiceBuild.processService.qualityProcessOk(mProcessCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isFailed) {
                    throw DsException(it)
                }
                mDialogTipHelper.showSuccessTip("操作成功", topbar) {
                    mDialogTipHelper.dismissSuccess()
                    setFragmentResult(RESULT_OK, null)
                    popBackStack()
                }
            }, {
                topbar?.post {
                    mDialogTipHelper.dismissLoading()
                    val httpExceptionMessage = ExceptionUtils.getExceptionMessage(it)
                    mDialogTipHelper.showFailTip(httpExceptionMessage, topbar) {
                        mDialogTipHelper.dismissFail()
                    }
                }

            }, {
                mDialogTipHelper.dismissLoading()
            })
        RxApiManager.instance.add(this, subscribe)
    }

    private fun actionProcessFailed(errorInfo: String?) {
        mDialogTipHelper.showLoading("提交中")
        val subscribe = ServiceBuild.processService.qualityProcessFailed(mProcessCode, errorInfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isFailed) {
                    throw DsException(it)
                }
                mDialogTipHelper.showSuccessTip("操作成功", topbar) {
                    mDialogTipHelper.dismissSuccess()
                    setFragmentResult(RESULT_OK, null)
                    popBackStack()
                }
            }, {
                topbar?.post {
                    mDialogTipHelper.dismissLoading()
                    val httpExceptionMessage = ExceptionUtils.getExceptionMessage(it)
                    mDialogTipHelper.showFailTip(httpExceptionMessage, topbar) {
                        mDialogTipHelper.dismissFail()
                    }
                }

            }, {
                mDialogTipHelper.dismissLoading()
            })
        RxApiManager.instance.add(this, subscribe)
    }

    /**
     * 让步接收 网络提交
     * @param errorInfo 让步接收描述信息
     */
    private fun actionProcessConcession(errorInfo: String?) {
        mDialogTipHelper.showLoading(getString(R.string.submiting))
        val subscribe = ServiceBuild.processService.actionQualityConcession(mProcessCode, errorInfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isFailed) {
                    throw DsException(it)
                }
                mDialogTipHelper.showSuccessTip(getString(R.string.operate_success), topbar) {
                    mDialogTipHelper.dismissSuccess()
                    setFragmentResult(RESULT_OK, null)
                    popBackStack()
                }
            }, {
                topbar?.post {
                    mDialogTipHelper.dismissLoading()
                    val httpExceptionMessage = ExceptionUtils.getExceptionMessage(it)
                    mDialogTipHelper.showFailTip(httpExceptionMessage, topbar) {
                        mDialogTipHelper.dismissFail()
                    }
                }

            }, {
                mDialogTipHelper.dismissLoading()
            })
        RxApiManager.instance.add(this, subscribe)
    }


    private fun displayMoreMenu() {
        var menuBuilder = QMUIDialog.MenuDialogBuilder(context)
            .addItem("工时统计") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss();
                startFragment(ProcessTimerStatFragment.instance(mProcessModel.ccode))
            }
        val status = ProcessModel.StatusType.getStatus(mProcessModel.opState)
        if (status != ProcessModel.StatusType.DEFAULT && status != ProcessModel.StatusType.PENDING) {
            menuBuilder
                .addItem("操作记录") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss();
                    val instance = ProcessRecodeFragment.instance(mProcessModel.ccode)
                    startFragment(instance)
                }
        }

        menuBuilder.show();

    }

}