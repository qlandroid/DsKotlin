package com.feiling.dasong.ui.function.devmaintenance

import android.os.Bundle
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseTopBarFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.comm.toBody
import com.feiling.dasong.decorator.LinearLayoutUnVerticallyManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.DevMaintenOrderModel
import com.feiling.dasong.ui.adapter.LabelInputAdapter
import com.feiling.dasong.ui.adapter.LabelInputModel
import com.feiling.dasong.ui.function.dev.DevDetailsFragment
import com.feiling.dasong.ui.model.LabelTextModel
import com.qmuiteam.qmui.kotlin.onClick
import io.reactivex.Observable
import kotlinx.android.synthetic.main.device_main_input.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/15
 * @author ql
 */
class DeviceMainInputFragment : BaseTopBarFragment() {
    private var mInputAdapter: LabelInputAdapter = LabelInputAdapter()

    private var mOrderModel: DevMaintenOrderModel? = null;
    private var mOrderId: Int? = null;

    companion object {
        const val KEY_ID = "KEY_ID-"
        fun instance(orderId: Int): DeviceMainInputFragment {
            var bundle = Bundle()
            bundle.putInt(KEY_ID, orderId)
            var deviceMainInputFragment = DeviceMainInputFragment()
            deviceMainInputFragment.arguments = bundle
            return deviceMainInputFragment
        }
    }

    override fun createContentView(): View {
        return layoutInflater.inflate(R.layout.device_main_input, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)

        mTopBar?.setTitle("设备保养")
        mTopBar?.addLeftBackImageButton()?.onClick {
            popBackStack()
        }
        mOrderId = arguments!!.getInt(KEY_ID)

        deviceMainInputSubmitBtn.onClick {
            actionSubmit()
        }

        deviceMainInputRv.layoutManager = LinearLayoutUnVerticallyManager(context)
        deviceMainInputRv.adapter = mInputAdapter

        showEmptyView()
        loadData(mOrderId!!)
    }


    private fun loadData(orderId: Int) {
        var disposable = ServiceBuild.deviceMainService.getById(orderId)
            .response()
            .map {
                hideEmpty()
                it.getData(DevMaintenOrderModel::class.java)
            }
            .flatMap {
                mOrderModel = it;
                //设置订单详情
                deviceMainOrderNdv.setDetailsData(
                    mutableListOf(
                        LabelTextModel("保养单号", it.keepcode),
                        LabelTextModel("保养日期", it.beginTime)
                    )
                )
                //设置设备详情
                deviceMainDevDetailsNdv.setDetailsData(
                    mutableListOf(
                        LabelTextModel("设备名称", it.devicename),
                        LabelTextModel("设备编号", it.devicecode)
                    )
                )
                deviceMainDevDetailsNdv.setNavTitleClickListener {
                    startFragment(DevDetailsFragment.instance(mOrderModel!!.devicecode))
                }
                //设置输入项
                Observable.fromIterable(it.list)
            }
            .map {

                var labelInputModel = LabelInputModel()
                labelInputModel.key = it.ddid
                labelInputModel.label = it.upkeepname
                labelInputModel.value = it.value

                labelInputModel
            }
            .subscribe(
                {
                    mInputAdapter.addData(it)
                }, {
                    it.printStackTrace()
                    runUiThread {
                        showFailed(msg = it.getDsMsg()) { emptyView ->
                            emptyView.showEmptyView()
                            loadData(orderId)
                        }
                    }
                }, {
                    mInputAdapter.notifyDataSetChanged()
                })

        addDisposable(disposable)
    }

    private fun actionSubmit() {
        showTipLoading("提交中...")
        var disposable = Observable.fromIterable(mInputAdapter.data)
            .flatMap { inputModel ->
                Observable.fromIterable(mOrderModel!!.list!!)
                    .filter {
                        inputModel.key == it.ddid
                    }
                    .map { it ->
                        it.value = inputModel.value.toString();
                    }
            }
            //将数据进行合并
            .collect({
            }, { unit: Unit, unit1: Unit ->
            })
            .flatMapObservable {
                ServiceBuild.deviceMainService.actionSubmit(mOrderModel!!.toBody())
            }
            .response()
            .subscribe({
                cancelTipLoading()
                showTipSuccess("提交成功") {
                    popBackStack()
                }
            }, {
                it.printStackTrace()
                runUiThread {
                    cancelTipLoading()
                    showTipFailed("提交失败")
                }
            })
        addDisposable(disposable)

    }
}