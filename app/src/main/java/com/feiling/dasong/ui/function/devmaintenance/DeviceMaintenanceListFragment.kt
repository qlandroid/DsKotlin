package com.feiling.dasong.ui.function.devmaintenance

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.comm.toStringMap
import com.feiling.dasong.decorator.LinearLayoutUnVerticallyManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.DevMaintenOrderModel
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.ql.comm.utils.JsonUtils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.device_main_input.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/15
 * @author ql
 */
class DeviceMaintenanceListFragment : CommSelectFragment() {

    private var mPage: BasePage = BasePage();
    private var mParams: DevMaintenOrderModel = DevMaintenOrderModel();

    private var mOrderList: MutableList<DevMaintenOrderModel> = mutableListOf()

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        var topBar = getTopBar()
        topBar.setTitle("保养列表")
        mCommAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            var instance = DeviceMainInputFragment.instance(mOrderList[i].did!!)
            startFragment(instance)
        }
        mCommAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPage.nextPage()
            loadData()
        }


        setRefresh {
            mPage.reset()
            loadData()
        }

        showLoading()
        loadData()
    }

    private fun loadData() {
        mCommAdapter.loadMoreModule?.isEnableLoadMore = false
        var disposable = ServiceBuild.deviceMainService
            .loadPage(mPage.pageNo, mPage.pageSize, mParams.toStringMap())
            .response()
            .map {
                it.getPageModel()
            }
            .flatMap {
                hideEmpty()
                mCommAdapter.loadMoreModule?.isEnableLoadMore = true
                setIsRefresh(false)
                if (it.isFirst) {
                    mOrderList.clear()
                    mCommAdapter.data.clear()
                    if (it.isEmpty) {
                        mCommAdapter.loadMoreModule?.isEnableLoadMore = false
                        showEmpty {
                            loadData()
                        }
                    }
                }
                if (it.isNotLoadSize) {
                    mCommAdapter.loadMoreModule?.loadMoreEnd()
                }


                Observable.fromIterable(it.list)
            }
            .map {
                JsonUtils.fromJson(it, DevMaintenOrderModel::class.java)
            }
            .map {
                mOrderList.add(it)
                var navDetailsModel = NavDetailsModel()
                navDetailsModel.title = "保养日期:${it.begintime}"

                navDetailsModel.children = mutableListOf(
                    LabelTextModel("保养单号", it.keepcode),
                    LabelTextModel("设备名称", it.devicename),
                    LabelTextModel("设备编码", it.devicecode)
                )
                navDetailsModel
            }
            .subscribe({
                mCommAdapter.addData(it)
                mCommAdapter.notifyItemChanged(mCommAdapter.data.size - 1)
            }, {
                it.printStackTrace()
                runUiThread {
                    hideEmpty()
                    if (mPage.isFirst()) {
                        showFailed(msg = it.getDsMsg()) {
                            showLoading()
                            loadData()
                        }
                    } else {
                        mCommAdapter.loadMoreModule?.loadMoreFail()
                    }
                }
            }, {

            })
        addDisposable(disposable)

    }
}