package com.feiling.dasong.ui.function.outsource

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.decorator.DividerItemDecoration2
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.model.os.OsProcessModel
import com.feiling.dasong.model.os.OutsourceModel
import com.feiling.dasong.ui.adapter.NavDetailsAdapter
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.DateUtils
import com.feiling.dasong.uitils.ExceptionUtils
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：委外管理出库
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/27
 * @author ql
 */
class OutsourceOutFragment : CommSelectFragment() {


    var mAdapter = NavDetailsAdapter();

    private var mPage: BasePage = BasePage()

    private var mDataList: MutableList<OutsourceModel> = mutableListOf()

    private var mOptIndex: Int = 0;

    companion object {
        const val REQUEST_DETAILS = 200
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("委外待出库")
        topbar.addLeftBackImageButton().onClick { popBackStack() }
        topbar.addRightImageButton(R.drawable.icon_scanning, R.id.topbar_right_scanning_button)
            .onClick {
                toScanning()
            }
        comm_select_rv.layoutManager = LinearLayoutManager(context)
        comm_select_rv.addItemDecoration(
            DividerItemDecoration2(
                context,
                LinearLayoutManager.VERTICAL,
                QMUIDisplayHelper.dp2px(context, 10),
                R.color.div_color
            )
        )
        comm_select_rv.adapter = mAdapter


        comm_slt_pull_layout.isEnabled = false

        comm_slt_pull_layout.setOnRefreshListener {
            mPage.reset()
            loadData()
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val outsourceModel = mDataList[position]
            mOptIndex = position
            startFragmentForResult(
                OutsourceProcessOutDetailsFragment.instance(
                    outsourceModel.ccode,
                    OutsourceProcessOutDetailsFragment.OPT_TYPE_OUT
                ),
                REQUEST_DETAILS
            )
        }
        mAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPage.nextPage()
            loadData()
        }


        emptyView.showEmptyView()
        loadData()
    }


    private fun loadData() {
        mAdapter.loadMoreModule?.isEnableLoadMore = false
        comm_slt_pull_layout.isEnabled = false
        val subscribe =
            ServiceBuild.outsourceService.outsourceUnoutPageList(mPage.pageNo, mPage.pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (it.isFailed) {
                        throw DsException(it)
                    }
                    it.getPageModel()
                }
                .flatMap {
                    if (it.isFirst) {
                        mDataList.clear()
                        mAdapter.data.clear()
                    }

                    if (it.isFirst && it.isEmpty) {

                    } else if (it.isFirst && it.isNotLoadSize) {
                        mAdapter.loadMoreModule?.loadMoreEnd()
                    } else if (!it.isFirst && it.isNotLoadSize) {
                        mAdapter.loadMoreModule?.loadMoreEnd()
                    } else {
                        mAdapter.loadMoreModule?.loadMoreComplete()
                    }
                    Observable.fromIterable(it.list)
                }
                .map {
                    JsonUtils.toJson(it)
                }
                .map {
                    JsonUtils.fromJson(it, OutsourceModel::class.java)
                }
                .map {
                    mDataList.add(it)
                    //设置item显示内容
                    val navDetailsModel = NavDetailsModel(it.suppname, null)
                    navDetailsModel.subTitle = it.suppcode
                    val status = OsProcessModel.OrderState.getStatus(it.state)
                    navDetailsModel.hint = status.state
                    navDetailsModel.children = mutableListOf<LabelTextModel>(
                        LabelTextModel("委外单号", it.ccode.orEmpty()),
                        LabelTextModel("备注", it.remarks.orEmpty()),
                        LabelTextModel("创建时间", DateUtils.replaceYYYY_MM_dd_HHmmss(it.createddate))
                    )

                    navDetailsModel
                }
                .subscribe(
                    {
                        mAdapter.addData(it)
                    },
                    {
                        it.printStackTrace()
                        topbar?.post {
                            if (comm_slt_pull_layout.isRefreshing) {
                                comm_slt_pull_layout.isRefreshing = false
                            }

                            val msg = ExceptionUtils.getExceptionMessage(it)
                            if (mDataList.isEmpty()) {
                                emptyView.showFailed(msg = msg) {
                                    it.showEmptyView(true)
                                    loadData()
                                }
                            } else {
                                if (!comm_slt_pull_layout.isEnabled) {
                                    comm_slt_pull_layout.isEnabled = true
                                }
                                mAdapter.loadMoreModule?.loadMoreFail()
                            }
                        }
                    },
                    {
                        if (comm_slt_pull_layout.isRefreshing) {
                            comm_slt_pull_layout.isRefreshing = false
                        }

                        if (mDataList.isEmpty()) {
                            emptyView.showEmpty { loadData() }
                        } else {
                            if (!comm_slt_pull_layout.isEnabled) {
                                comm_slt_pull_layout.isEnabled = true
                            }
                            if (emptyView.isShowing) {
                                emptyView.hideEmpty()
                            }
                        }
                        mAdapter.loadMoreModule?.isEnableLoadMore = true
                        mAdapter.notifyDataSetChanged()

                    }
                )
        addDisposable(subscribe)
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_DETAILS -> {
                if (RESULT_OK == resultCode) {
                    mDataList.removeAt(mOptIndex)
                    mAdapter.data.removeAt(mOptIndex)
                    mAdapter.notifyDataSetChanged()
                }
            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }

    }

    override fun onScanningCodeResult(code: String?, requestCode: Int?) {
        startFragmentForResult(
            OutsourceProcessOutDetailsFragment.instance(
                code,
                OutsourceProcessOutDetailsFragment.OPT_TYPE_OUT
            ), REQUEST_DETAILS
        )

    }


}