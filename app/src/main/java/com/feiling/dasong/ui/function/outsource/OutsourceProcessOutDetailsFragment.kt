package com.feiling.dasong.ui.function.outsource

import android.os.Bundle
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.toBody
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.os.OsProcessModel
import com.feiling.dasong.model.os.OutsourceModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.qmuiteam.qmui.kotlin.onClick
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_os_out_details.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/27
 * @author ql
 */
class OutsourceProcessOutDetailsFragment : BaseFragment(), IOSDetailsProcessPresenter {
    companion object {
        private const val KEY_CODE = "KEY_CCODE"
        private const val KEY_OPT_TYPE = "KEY-OPT-TYPE"
        private const val KEY_SHOW_Inv_CODE = "SHOW-PROCESS-CODE"
        const val OPT_TYPE_IN = 0
        const val OPT_TYPE_OUT = 1

        fun instance(
            ccode: String?,
            optType: Int = OPT_TYPE_OUT
        ): OutsourceProcessOutDetailsFragment {
            val bundle = Bundle()
            bundle.putString(KEY_CODE, ccode)
            bundle.putInt(KEY_OPT_TYPE, optType)
            val outsourceOutFragment = OutsourceProcessOutDetailsFragment()
            outsourceOutFragment.arguments = bundle
            return outsourceOutFragment
        }

        fun instance(
            ccode: String?,
            showInvcode: String?,
            optType: Int = OPT_TYPE_OUT
        ): OutsourceProcessOutDetailsFragment {
            val bundle = Bundle()
            bundle.putString(KEY_CODE, ccode)
            bundle.putInt(KEY_OPT_TYPE, optType)
            bundle.putString(KEY_SHOW_Inv_CODE, showInvcode)
            val outsourceOutFragment = OutsourceProcessOutDetailsFragment()
            outsourceOutFragment.arguments = bundle
            return outsourceOutFragment
        }
    }

    private lateinit var mDetailsView: IOSDetailsProcessView

    private lateinit var mOsDetailsModel: OutsourceModel
    /**
     * 当前委外单据的code
     */
    private lateinit var mCode: String
    /**
     * 当前操作的操作的 存货编码
     */
    private var mShowInvcode: String? = null


    private var mOptType: Int = OPT_TYPE_IN

    private var mActionInProcessModel: OsProcessModel? = null

    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_os_out_details, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        mDetailsView = osDetailsProcessView
        mOptType = arguments!!.getInt(KEY_OPT_TYPE)
        mShowInvcode = arguments!!.getString(KEY_SHOW_Inv_CODE)
        osDetailsProcessView.presenter = this;
        topbar.setTitle("工序委外详情")
        topbar.addLeftBackImageButton().onClick { popBackStack() }
        topbar.addRightImageButton(
            R.drawable.icon_scanning,
            R.id.topbar_right_scanning_button
        )
            .onClick { toScanning() }

        loadData()

    }


    fun loadData() {
        emptyView.showEmptyView(true)
        val list: MutableList<NavDetailsModel> = mutableListOf()
        val subscribe = Observable.create<String> {
            mCode = arguments?.getString(KEY_CODE)!!
            it.onNext(mCode)
            it.onComplete()
        }
            .flatMap {
                ServiceBuild.outsourceService.outsourceDetailsById(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isFailed) {
                    throw DsException(it)
                }
                it.getData(OutsourceModel::class.java)
            }
            .flatMap {
                mOsDetailsModel = it
                when (OsProcessModel.OrderState.getStatus(it.state)) {
                    OsProcessModel.OrderState.UNOUT -> {
                        mDetailsView.initUnout()
                    }
                    OsProcessModel.OrderState.OUT -> {
                        mDetailsView.initOutEnd()
                    }
                    else -> {
                        mDetailsView.initIning();
                    }
                }


                mDetailsView.setSupplierDetails(
                    mutableListOf<LabelTextModel>(
                        LabelTextModel("委外单号", it.ccode.orEmpty()),
                        LabelTextModel("供应商名称", it.suppname.orEmpty()),
                        LabelTextModel("供应商编码", it.suppcode.orEmpty()),
                        LabelTextModel("状态", it.state.orEmpty())
                    )
                )
                Observable.fromIterable(it.boutsources)
            }
            .map {
                val navDetailsModel = NavDetailsModel(it.pmname, null)
                navDetailsModel.subTitle = it.pmcode
                val status = OsProcessModel.OsProcessState.getStatus(it.state)
                navDetailsModel.hint = status.label
                navDetailsModel.children = mutableListOf(
                    LabelTextModel("存货名称", it.invname.orEmpty(), 4),
                    LabelTextModel("存货编码", it.invcode.orEmpty(), 4),
                    LabelTextModel("合同号", it.conno.orEmpty())
                )


                navDetailsModel.showRight = true;
                navDetailsModel
            }
            .subscribe(
                {
                    list.add(it)
                },
                {
                    it.printStackTrace()
                    topbar?.post {
                        val exceptionMessage = ExceptionUtils.getExceptionMessage(it)
                        emptyView.showFailed(msg = exceptionMessage) { emptyView ->
                            emptyView.showEmptyView()
                            loadData()
                        }
                    }
                },
                {
                    if (emptyView != null) {
                        emptyView.hideEmpty()
                    }

                    mDetailsView.setProcessList(list);
                    if (checkIsInOptions()) {
                        //当前已经出库状态 显示当前进入的工序详情
                        actionProcessInByInvcode(mShowInvcode)
                    }

                }
            )
        RxApiManager.instance.add(this, subscribe)

    }

    /**
     * 检测当前是 是否是入库状态
     */
    private fun checkIsInOptions(): Boolean {
        val status = OsProcessModel.OsProcessState.getStatus(mOsDetailsModel.state)
        if (status == OsProcessModel.OsProcessState.OUTEND) {
            //已出库状态
            return true;
        }
        return false
    }

    /**
     * 扫描 存货
     * @param code
     */
    private fun actionProcessInByInvcode(invcode: String?) {
        mActionInProcessModel = null
        val subscribe = Observable.fromIterable(mOsDetailsModel.boutsources)
            .filter {
                it.invcode == invcode
            }
            .firstElement()
            .map {
                //当前需要提交 入库的对象
                mActionInProcessModel = it;
                createProcessDetailsModelByOsProcessModel(it)
            }
            .subscribe(
                {
                    val status =
                        OsProcessModel.OsProcessState.getStatus(mActionInProcessModel?.state)
                    if (status != OsProcessModel.OsProcessState.INEND) {
                        //显示 收货 按钮
                        mDetailsView.displayProductInOptionsPopupWindow(it) {
                            actionSubmitProcessIn()
                        }
                    } else {
                        mDetailsView.displayProductDetails(it)
                    }


                },
                {
                    it.printStackTrace()
                    topbar?.post {
                        val exceptionMessage = ExceptionUtils.getExceptionMessage(it)
                        mDialogTipHelper.displayMsgDialog(msg = exceptionMessage)
                    }
                },
                {
                    if (mActionInProcessModel == null) {
                        mDialogTipHelper.displayMsgDialog(msg = "没有找到编号为${invcode}的辊子")
                    }
                }
            )
        RxApiManager.instance.add(this, subscribe)
    }

    private fun actionSubmitProcessIn() {
        mDialogTipHelper.showLoading("提交中")
        val subscribe = ServiceBuild.outsourceService.outsourceInAction(
            arrayListOf<Int>(
                mActionInProcessModel!!.did!!
            ).toBody()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isFailed) {
                    throw DsException(it)
                }
                mDialogTipHelper.dismissLoading()
                mDialogTipHelper.showSuccessTip("收货成功") {
                    loadData()
                }
            }, {
                it.printStackTrace()
                topbar?.post {
                    mDialogTipHelper.dismissLoading()
                    val exceptionMessage = ExceptionUtils.getExceptionMessage(it)
                    mDialogTipHelper.displayMsgDialog(msg = exceptionMessage)
                }
            }, {

            })
        RxApiManager.instance.add(this, subscribe)
    }


    override fun onClickToProcessDetails(position: Int) {
        val status = OsProcessModel.OrderState.getStatus(mOsDetailsModel.state)
        if (status == OsProcessModel.OrderState.UNOUT) {
            //未出库订单
            val subscribe = Observable.create<OsProcessModel> {
                it.onNext(mOsDetailsModel!!.boutsources?.get(position)!!)
            }
                .map {
                    val navDetailsModel = createProcessDetailsModelByOsProcessModel(it)
                    navDetailsModel
                }.subscribe({
                    mDetailsView.displayProductDetails(it)
                }, {
                    it.printStackTrace()
                    mDialogTipHelper.displayMsgDialog(msg = ExceptionUtils.getExceptionMessage(it))
                })
            addDisposable( subscribe)
        } else if (status != OsProcessModel.OrderState.END) {
            //已出库单
            val osProcessModel = mOsDetailsModel!!.boutsources?.get(position)!!
            actionProcessInByInvcode(osProcessModel.invcode!!)
        }
    }

    private fun createProcessDetailsModelByOsProcessModel(it: OsProcessModel): NavDetailsModel {
        val navDetailsModel = NavDetailsModel()
        navDetailsModel.title = if (it.invname != null) it.invname else "未发现名称"
        navDetailsModel.subTitle = it.invcode

        var qty = "0"
        qty = if (it.quantity == null) {
            ""
        } else {
            it.quantity.toString()
        }

        navDetailsModel.children = mutableListOf(
            LabelTextModel("客户名称", it.custabbname),
            LabelTextModel("合同号", it.conno),
            LabelTextModel("工序名称", it.pmname),
            LabelTextModel("工序编码", it.pmcode),
            LabelTextModel(
                "数量",
                "${qty}${it.invunitname.orEmpty()}"
            )
        )
        return navDetailsModel
    }

    override fun onClickOutSend() {
        mDialogTipHelper.showLoading("提交中")
        val mutableMapOf =
            mutableMapOf<String, String>(Pair("code", mOsDetailsModel.ccode.orEmpty()))
        val subscribe = ServiceBuild.outsourceService.outsourceOutAction(mutableMapOf.toBody())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isFailed) {
                    throw DsException(it)
                }
                mDialogTipHelper.dismissLoading()
                mDialogTipHelper.showSuccessTip("提交成功", topbar) {
                    mDialogTipHelper.dismissSuccess()
                    setFragmentResult(RESULT_OK, null)
                    this.loadData()
                }
            }, {
                it.printStackTrace()
                topbar?.post {
                    mDialogTipHelper.dismissLoading()
                    val exceptionMessage = ExceptionUtils.getExceptionMessage(it)
                    mDialogTipHelper.displayMsgDialog(msg = exceptionMessage)
                }
            }, {

                mDialogTipHelper.dismissLoading()
            })
        RxApiManager.instance.add(this, subscribe)
    }

    override fun onScanningCodeResult(code: String?,requestCode:Int?) {
        super.onScanningCodeResult(code,requestCode)
        if (checkIsInOptions()) {
            actionProcessInByInvcode(code!!)
        } else {
            mActionInProcessModel = null
            val subscribe = Observable.fromIterable(mOsDetailsModel.boutsources)
                .filter {
                    it.ccode == code || it.invcode == code
                }.firstElement()
                .map {
                    mActionInProcessModel = it
                    createProcessDetailsModelByOsProcessModel(it)
                }
                .doOnError {
                    it.printStackTrace()
                    mDialogTipHelper.displayMsgDialog(msg = ExceptionUtils.getExceptionMessage(it))
                }
                .doOnComplete {
                    if (mActionInProcessModel == null) {
                        mDialogTipHelper.displayMsgDialog(msg = "当前单据中没有找到编码为->${code}工序和产品产品")
                    }
                }
                .subscribe {
                    mDetailsView.displayProductDetails(it)
                }
        }
    }
}