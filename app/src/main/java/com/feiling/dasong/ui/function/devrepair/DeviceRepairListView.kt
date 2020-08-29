package com.feiling.dasong.ui.function.devrepair

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.toStringMap
import com.feiling.dasong.decorator.DividerItemDecoration2
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.DevRepairModel
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.ui.adapter.NavDetailsAdapter
import com.feiling.dasong.ui.function.arrange.model.StatusTypeEnum
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.feiling.dasong.widget.EmptyView
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.arrange_work.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/4
 * @author ql
 */
class DeviceRepairListView(
    context: Context?,
    var page: DeviceRepairListContentPage,
    var ctlListener: ArrangeWorkControlListener
) :
    QMUIWindowInsetLayout(context) {
    private lateinit var mEmptyView: EmptyView


    private val mNdvAdapter = NavDetailsAdapter()

    private var mPage = BasePage();
    private var mParamsModel: DevRepairModel = DevRepairModel()

    private val mAllList: MutableList<DevRepairModel> by lazy { mutableListOf<DevRepairModel>() }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.arrange_work, this)
        ButterKnife.bind(view)


        initView()
    }

    private fun initView() {
        mNdvAdapter.animationEnable = true
        mNdvAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPage.nextPage()
            loadData()
        }

        arrange_work_refreshView.setColorSchemeColors(
            ContextCompat.getColor(
                context,
                R.color.app_color_red
            )
        )

        arrange_work_refreshView.isEnabled = false;



        arrange_work_refreshView.setOnRefreshListener {
            onRefresh()
        }

        mParamsModel.vstatus = DevRepairModel.RepairVerifyState.VERIFY.state
        mParamsModel.devState = page.state




        arrange_work_rv.layoutManager = LinearLayoutManager(context)
        arrange_work_rv.addItemDecoration(
            DividerItemDecoration2(
                context,
                LinearLayoutManager.VERTICAL,
                QMUIDisplayHelper.dp2px(context, 10),
                ContextCompat.getColor(context, R.color.div_color)
            )
        )


        mEmptyView = EmptyView(context)
        mEmptyView.showEmptyView(loading = true);
        mEmptyView.isClickable = true
        mEmptyView.onClick {
            if (it is EmptyView) {
                it.showEmptyView(loading = true)
            }
            loadData()
        }

        mNdvAdapter.setEmptyView(mEmptyView)

        arrange_work_rv.adapter = mNdvAdapter

        mNdvAdapter.setOnItemClickListener { _: BaseQuickAdapter<Any?, BaseViewHolder>, _: View, i: Int ->
            val item = mAllList[i]
            ctlListener.startFragment(DeviceRepairDetailsFragment.instance(item.devCode!!))
        }
        loadData();
    }

    private fun loadData() {
        mNdvAdapter.loadMoreModule?.isEnableLoadMore = false
        val subscribe = ServiceBuild.deviceRepairService.getPageList(
            mPage.pageNo,
            mPage.pageSize,
            mParamsModel.toStringMap()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                if (arrange_work_refreshView.isRefreshing) {
                    arrange_work_refreshView.isRefreshing = false;
                }

                if (it.isFailed) {
                    throw DsException(it.message.orEmpty())
                }
                it.getPageModel()
            }
            .flatMap {
                if (it.isFirst) {
                    mAllList.clear()
                    mNdvAdapter.data.clear()
                }
                if (it.isFirst && it.isEmpty) {
                    arrange_work_refreshView.isEnabled = false;
                    mEmptyView.showEmpty("没有找到数据", "点击页面重新加载"){emptyView ->
                        emptyView.showEmptyView();
                        loadData()
                    }

                } else if (it.isNotLoadSize) {
                    //如果不够一页,显示没有更多数据布局
                    initLoadMoreAndRefreshView()
                    arrange_work_refreshView.isEnabled = true;

                    mNdvAdapter.loadMoreModule?.loadMoreEnd()

                } else {
                    arrange_work_refreshView.isEnabled = true;

                    initLoadMoreAndRefreshView()
                    mNdvAdapter.loadMoreModule?.loadMoreComplete()
                }
                Observable.fromIterable(it.list)
            }
            .map {
                JsonUtils.fromJson<DevRepairModel>(it, DevRepairModel::class.java)
            }
            .subscribe(
                { model ->
                    mAllList.add(model)
                    val type = when (model.devState) {
                        DeviceRepairListContentPage.DEFAULT.state -> {
                            StatusTypeEnum.PENDING
                        }
                        DeviceRepairListContentPage.STARTED.state -> {
                            StatusTypeEnum.START
                        }
                        DeviceRepairListContentPage.PAUSED.state -> {
                            StatusTypeEnum.PAUSE
                        }
                        DeviceRepairListContentPage.END.state -> {
                            var state = DevRepairModel.RepairState.getState(model.repairState)
                            when (state) {
                                DevRepairModel.RepairState.OK -> {
                                    StatusTypeEnum.END
                                }
                                else -> {
                                    StatusTypeEnum.FAILED
                                }
                            }
                        }
                        else -> {
                            StatusTypeEnum.DEFAULT
                        }
                    }
                    var navDetailsModel = NavDetailsModel()
                    navDetailsModel.showTag = true
                    navDetailsModel.tagText = context.getString(type.label)
                    navDetailsModel.tagBackgroundColor = ContextCompat.getColor(context,type.color)
                    navDetailsModel.title = model.ceqname
                    navDetailsModel.subTitle = model.ceqcode

                    navDetailsModel.children = mutableListOf(
                        LabelTextModel("维修订单号", model.devCode),
                        LabelTextModel("备注信息", model.problemDesc,contentPosition = Gravity.RIGHT,maxLine = 3)
                    )

                    mNdvAdapter.addData(
                        navDetailsModel
                    )
                }, {
                    it.printStackTrace()
                    mEmptyView.post {
                        if (arrange_work_refreshView.isRefreshing) {
                            arrange_work_refreshView.isRefreshing = false
                        }
                        mNdvAdapter.loadMoreModule?.isEnableLoadMore = true;
                        val msg = ExceptionUtils.getExceptionMessage(it)
                        if (mPage.isFirst()) {
                            mEmptyView.showFailed(msg = msg) { emptyView ->
                                emptyView.showEmptyView(true)
                                loadData()
                            }
                        } else {
                            mNdvAdapter.loadMoreModule?.loadMoreFail()
                        }

                    }
                }, {
                    mNdvAdapter.loadMoreModule?.isEnableLoadMore = true;
                    if (arrange_work_refreshView.isRefreshing) {
                        arrange_work_refreshView.isRefreshing = false
                    }
                    mNdvAdapter.notifyDataSetChanged()
                }
            )
        RxApiManager.add(this, subscribe);
    }

    override fun onDetachedFromWindow() {
        RxApiManager.cancel(this)
        super.onDetachedFromWindow()
    }

    fun onRefresh() {
        mPage.reset()
        mNdvAdapter.loadMoreModule?.isEnableLoadMore = false
        loadData()
    }

    private fun initLoadMoreAndRefreshView() {
        if (!mNdvAdapter.loadMoreModule?.isAutoLoadMore!!) {
            mNdvAdapter.loadMoreModule?.isAutoLoadMore = true;
        }
        if (!mNdvAdapter.loadMoreModule?.isEnableLoadMore!!) {
            mNdvAdapter.loadMoreModule?.isEnableLoadMore = true
        }
    }



    interface ArrangeWorkControlListener {
        fun startFragment(frag: BaseFragment)
    }
}