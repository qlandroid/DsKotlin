package com.feiling.dasong.ui.function.devcheck

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.IntDef
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseTopBarFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.comm.toBody
import com.feiling.dasong.decorator.DivUtils
import com.feiling.dasong.decorator.LinearLayoutUnVerticallyManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.DevCheckItemModel
import com.feiling.dasong.model.DevCheckOrderModel
import com.feiling.dasong.ui.adapter.LabelInputAdapter
import com.feiling.dasong.ui.adapter.LabelInputModel
import com.feiling.dasong.ui.function.dev.DevDetailsFragment
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.uitils.DateUtils
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.onClick
import io.reactivex.Observable
import kotlinx.android.synthetic.main.dev_check_input.*

/**
 * 描述：设备点检
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/10
 * @author ql
 */
class DevCheckInputFragment : BaseTopBarFragment() {
    /**
     * 添加物品时，区分类型
     */
    @IntDef(value = [DetailsType.TYPE_NEW_ORDER, DetailsType.TYPE_DETAILS])
    @Retention(AnnotationRetention.SOURCE)
    annotation class DetailsType {
        companion object {
            const val TYPE_NEW_ORDER = 0X10
            const val TYPE_DETAILS = 0X11
        }
    }

    companion object {


        const val KEY_TYPE = "KEY-TYPE"
        const val KEY_ORDER_ID = "KEY-ORDER-ID"
        const val KEY_DEV_CODE = "KEY-DEV-CODE"

        /**
         * 创建新的订单
         * @param devCode 设备编号
         */
        fun instanceCreateNewOrder(devCode: String): DevCheckInputFragment {
            return instance(DetailsType.TYPE_NEW_ORDER, null, devCode)
        }

        /**
         * 查看点检单
         */
        fun instanceEditOrderByCode(orderId: String): DevCheckInputFragment {
            return instance(DetailsType.TYPE_DETAILS, orderId, null)
        }

        fun instance(
            @DetailsType type: Int, orderId: String?,
            devCode: String?
        ): DevCheckInputFragment {
            var bundle = Bundle()
            bundle.putInt(KEY_TYPE, type)
            bundle.putString(KEY_ORDER_ID, orderId)
            bundle.putString(KEY_DEV_CODE, devCode)
            var devCheckInputFragment = DevCheckInputFragment()
            devCheckInputFragment.arguments = bundle;
            return devCheckInputFragment
        }

    }

    /**
     * 用户是否已经开始输入
     */
    private var isInput: Boolean = false;


    private var mInputAdapter = LabelInputAdapter();
    /**
     * 当前操作点检单详情
     */
    private var mOptOrder: DevCheckOrderModel? = null;

    /**
     * 当前操作的任务单号
     */
    private var mOptOderCode: String? = null;


    override fun createContentView(): View {
        return layoutInflater.inflate(R.layout.dev_check_input, null);
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        mTopBar?.setTitle("点检操作单")
        mTopBar?.addLeftBackImageButton()?.onClick {
            if (isInput) {
                displayAskMsgDialog("退出后输入的数据将会被取消，确定要退出操作吗?"
                    , okBlock = {
                        it.dismiss()
                        popBackStack()
                    }, cancelBlock = {
                        it.dismiss()
                    })
                return@onClick
            }
            popBackStack()
        }

        devCheckInputDevDetailsNdv.setNavTitleClickListener {
            startFragment(DevDetailsFragment.instance(mOptOrder!!.ceqCode))
        }

        devCheckInputRv.layoutManager = LinearLayoutUnVerticallyManager(context);
        devCheckInputRv.addItemDecoration(DivUtils.getDivDefault(context))
        devCheckInputRv.adapter = mInputAdapter;


        devCheckInputSubmitBtn.onClick {
            actionSubmit()
        }

        arguments?.let {
            var detailsType = it.getInt(KEY_TYPE)
            when (detailsType) {
                DetailsType.TYPE_DETAILS -> {
                    showEmptyView(true)
                    mOptOderCode = it.getString(KEY_ORDER_ID)!!
                    getOrderDetails(mOptOderCode!!)
                }
                else -> {
                    showEmptyView(true)
                    var devCode = it.getString(KEY_DEV_CODE)
                    createNewOrder(devCode)
                }
            }
        }


    }

    /**
     * 提交
     */
    private fun actionSubmit() {
        showTipLoading("提交中")

        //存储的value值
        var data = mInputAdapter.data
        //需要提交的内容
        var checkList = mOptOrder?.checkSheetProjectList
        /*
         *   先将value值赋值
         */
        var disposable = Observable.fromIterable(data)
            .flatMap { inputModel ->
                Observable.fromIterable(checkList)
                    .filter { devCheckItemModel ->
                        inputModel.key == devCheckItemModel.id
                    }
                    .doOnNext {
                        it.standardValue = inputModel.value.toString()
                    }
            }
            .collect(
                //将数据合并
                {
                },
                { mutableList: Unit, devCheckItemModel: DevCheckItemModel ->

                })
            .flatMapObservable {
                checkList?.map {
                    if (TextUtils.isEmpty(it.standardValue)){
                        it.standardValue = ""
                    }
                }
                ServiceBuild.deviceCheckService.actionSubmit(checkList!!.toBody())
            }
            .response()
            .subscribe({
                showTipSuccess("提交成功") {
                    it?.dismiss()
                    popBackStack()
                }
                cancelTipLoading()
            }, {
                it.printStackTrace()
                runUiThread {
                    displayMsgDialog(it.getDsMsg())
                    cancelTipLoading()
                }
            }, {

            })

        addDisposable( disposable)
    }


    /**
     * 创建订单
     */
    private fun createNewOrder(devCode: String) {
        var disposable = ServiceBuild.deviceCheckService.actionInsert(devCode)
            .response()
            .map {
                it.data?.asString
            }
            .subscribe({
                mOptOderCode = it;
                getOrderDetails(it!!)
            }, {
                it.printStackTrace()
                showFailed(msg = it.getDsMsg()) { emptyview ->
                    emptyview.showEmptyView()
                    setFragmentResult(QMUIFragment.RESULT_OK, Intent())
                    createNewOrder(devCode);
                }
            })
        addDisposable(disposable)
    }

    /**
     * 查询订单
     */
    private fun getOrderDetails(orderCode: String) {
        var disposable = ServiceBuild.deviceCheckService.getById(orderCode)
            .response()
            .map {
                hideEmpty()
                it.getData(DevCheckOrderModel::class.java)
            }
            .map {
                mOptOrder = it;
                //设置订单信息
                devCheckInputOrderDetailsNdv.setDetailsData(
                    mutableListOf(
                        LabelTextModel("单据编号", it.checkSheetCode),
                        LabelTextModel("创建日期", DateUtils.replaceYYYY_MM_dd_HHmmss(it.createDate))
                    )
                )
                //设置设备详情
                devCheckInputDevDetailsNdv.setDetailsData(
                    mutableListOf(
                        LabelTextModel("设备名称", it.ceqCode),
                        LabelTextModel("设备编码", it.ceqName)
                    )
                )

                //设置需要填写的明细
                it.checkSheetProjectList
            }.flatMap {
                Observable.fromIterable(it)
            }
            .map {
                //创建每条明细
                var labelInputModel = LabelInputModel(it.id, it.checkName, it.standardValue)
                labelInputModel
            }
            .subscribe({
                mInputAdapter.addData(it);
            }, {
                it.printStackTrace()
                runUiThread {
                    showFailed(msg = it.getDsMsg()) {
                        getOrderDetails(orderCode)
                    }
                }
            }, {
                isInput = true;
                //用于更新布局
                mInputAdapter.notifyDataSetChanged()
            })
        addDisposable(disposable);
    }
}