package com.feiling.dasong.ui.function.devcheck

import android.content.Intent
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.comm.toStringMap
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.DevCheckOrderModel
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.ui.function.dev.DevAllSelectFragment
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.DateUtils
import com.google.gson.reflect.TypeToken
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import io.reactivex.Observable

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/10
 * @author ql
 */
class DevCheckListFragment : CommSelectFragment() {

    companion object {
        const val REQUEST_CODE_DETAILS = 0x10001
        const val REQUEST_CODE_DEV_SELECT = 0X1002;
    }

    private var mPage: BasePage = BasePage()
    private var mParams = DevCheckOrderModel();

    private var mOrderList: MutableList<DevCheckOrderModel> = mutableListOf();

    private var mRefreshBlock: (SwipeRefreshLayout?) -> Unit = {
        mPage.reset()
        loadData()
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        getTopBar().setTitle("设备点检任务")

        getTopBar().addRightTextButton("添加", R.id.topbar_right_yes_button)
            .onClick {
                QMUIBottomSheet.BottomListSheetBuilder(context)
                    .setTitle("操作选择")
                    .addItem("扫描设备编号")
                    .addItem("设备列表选择")
                    .setGravityCenter(true)
                    .setRadius(QMUIDisplayHelper.dp2px(context, 20))
                    .setAddCancelBtn(true)
                    .setOnSheetItemClickListener { qmuiBottomSheet: QMUIBottomSheet, view: View, i: Int, s: String ->
                        qmuiBottomSheet.dismiss()
                        when (i) {
                            0 -> {
                                toScanning()
                            }
                            1 -> {
                                var devAllSelectFragment = DevAllSelectFragment()
                                startFragmentForResult(
                                    devAllSelectFragment,
                                    REQUEST_CODE_DEV_SELECT
                                )
                            }
                            else -> {
                            }
                        }
                    }
                    .setSkinManager(QMUISkinManager.defaultInstance(context))
                    .build()
                    .show()
            }

        mCommAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPage.nextPage()
            loadData()
        }

        mCommAdapter.setOnItemClickListener { adapter, view, position ->
            var order = mOrderList[position]
            toCheckEditOrder(order.checkSheetCode!!)
        }
        showLoading()
        loadData()
    }


    private fun loadData() {
        mCommAdapter.loadMoreModule?.isEnableLoadMore = false;
        var disposable = ServiceBuild
            .deviceCheckService
            .getPageList(
                mPage.pageNo,
                mPage.pageSize,
                mParams.toStringMap()
            )
            .response()
            .map {
                it.getPageModel()
            }
            .map {
                hideEmpty();
                setRefresh(mRefreshBlock)
                setOpenRefresh(true)
                setIsRefresh(false)
                mCommAdapter.loadMoreModule?.isEnableLoadMore = true;
                if (it.isFirst) {
                    mOrderList.clear();
                    mCommAdapter.data.clear()
                    if (it.isEmpty) {
                        setOpenRefresh(false)
                        showEmpty() { emptyView ->
                            emptyView.showEmptyView()
                            loadData()
                        }
                    }
                }
                if (it.isNotLoadSize) {
                    mCommAdapter.loadMoreModule?.loadMoreEnd()
                }


                it.list?.asJsonArray
            }
            .flatMap {
                var type = object : TypeToken<MutableList<DevCheckOrderModel>>() {}.type
                var list = JsonUtils.fromTypeJson<MutableList<DevCheckOrderModel>>(it, type)
                Observable.fromIterable(list);
            }
            .map {
                mOrderList.add(it)
                var navDetailsModel = NavDetailsModel()
                navDetailsModel.title = it.checkSheetCode
                navDetailsModel.children = mutableListOf(
                    LabelTextModel("设备名称", it.ceqName),
                    LabelTextModel("设备编码", it.ceqCode),
                    LabelTextModel("创建人", it.createBy),
                    LabelTextModel("创建时间", DateUtils.replaceYYYY_MM_dd_HHmmss(it.createDate))
                )
                navDetailsModel
            }
            .subscribe({
                mCommAdapter.addData(it)
            }, {
                it.printStackTrace()
                runUiThread {
                    setIsRefresh(false)
                    if (mPage.isFirst()) {
                        setOpenRefresh(false)
                        showFailed(msg = it.getDsMsg()) { emptyView ->
                            emptyView.showEmptyView()
                            loadData()
                        }
                    } else {
                        mCommAdapter.loadMoreModule?.loadMoreFail()
                    }
                }
            }, {
                mCommAdapter.notifyDataSetChanged()
            })

        addDisposable(disposable)
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_DEV_SELECT -> {
                val devModel = DevAllSelectFragment.getData(data)
                toCheckNewOrder(devModel!!.ceqcode)
            }
            REQUEST_CODE_DETAILS -> {
                if (resultCode == QMUIFragment.RESULT_OK) {
                    mPage.reset()
                    loadData()
                }
            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }
    }


    override fun onScanningCodeResult(code: String?, requestCode: Int?) {
        toCheckNewOrder(code)
    }

    /**
     * 跳转到 编辑页面 并新建 点检单
     */
    private fun toCheckNewOrder(devCode: String?) {
        var instanceCreateNewOrder = DevCheckInputFragment.instanceCreateNewOrder(devCode!!)
        startFragmentForResult(instanceCreateNewOrder, REQUEST_CODE_DETAILS)
    }

    /**
     * 跳转 编辑点检单
     */
    private fun toCheckEditOrder(orderCode: String) {
        var instance = DevCheckInputFragment.instanceEditOrderByCode(orderCode)
        startFragment(instance)
    }

    override fun onDestroyView() {
        RxApiManager.cancel(this)
        super.onDestroyView()
    }

    override fun onDestroy() {
        RxApiManager.cancel(this)
        super.onDestroy()
    }

}