package com.feiling.dasong.ui.function.quality

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.comm.toBody
import com.feiling.dasong.decorator.DividerItemDecoration2
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.ui.function.arrange.adapter.TaskAdapter
import com.feiling.dasong.model.process.ProcessModel
import com.feiling.dasong.ui.DialogTipHelper
import com.feiling.dasong.ui.function.arrange.model.ProcessQuery
import com.feiling.dasong.ui.function.arrange.model.StatusTypeEnum
import com.feiling.dasong.ui.function.arrange.model.TaskModel
import com.feiling.dasong.uitils.DateUtils
import com.feiling.dasong.uitils.DialogUtils
import com.feiling.dasong.uitils.LogUtils
import com.feiling.dasong.uitils.StringUtils
import com.feiling.dasong.widget.CustomToast
import com.feiling.dasong.widget.EmptyView
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
class ProcessQualityController(
    context: Context?,
    var page: ContentPage,
    var ctlListener: CheckWorkControlListener
) :
    QMUIWindowInsetLayout(context) {
    private lateinit var mEmptyView: EmptyView


    private val mTaskAdapter = TaskAdapter()

    private val mPageParams: ProcessQuery by lazy { ProcessQuery() }

    private val mAllList: MutableList<ProcessModel> by lazy { mutableListOf<ProcessModel>() }
    private var mDialogHelper:DialogTipHelper? = null;

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.arrange_work, this)
        ButterKnife.bind(view)
        initView()
    }

    private fun initView() {

        mDialogHelper = DialogTipHelper(context)
        mTaskAdapter.animationEnable = true
        mTaskAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPageParams.nextPage()
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

        mEmptyView = EmptyView(context)
        mEmptyView.isClickable = true
        mEmptyView.onClick {
            if (it is EmptyView) {
                it.showEmptyView(true)
            }
            loadData()
        }
        mEmptyView.showEmptyView(true)
        mTaskAdapter.setEmptyView(mEmptyView)


        mPageParams.opState = when (page) {
            ContentPage.ALL -> {
                null
            }
            ContentPage.DEFAULT -> {
                ProcessModel.StatusType.PENDING.type
            }
            ContentPage.STARTING -> {
                ProcessModel.StatusType.STARTED.type
            }
            ContentPage.PAUSE -> {
                ProcessModel.StatusType.PAUSED.type
            }
            ContentPage.END -> {
                ProcessModel.StatusType.ENDED.type
            }
            else ->
                null
        }


        arrange_work_rv.layoutManager = LinearLayoutManager(context)
        arrange_work_rv.addItemDecoration(
            DividerItemDecoration2(
                context,
                LinearLayoutManager.VERTICAL,
                QMUIDisplayHelper.dp2px(context, 10),
                ContextCompat.getColor(context, R.color.div_color)
            )
        )
        arrange_work_rv.adapter = mTaskAdapter

        mTaskAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            val item = mAllList[i]
            ctlListener.startFragment(ProcessQualityDetailsFragment.instance(item.ccode!!))
        }

        //点击子控件事件
        mTaskAdapter.setOnItemChildClickListener { _: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            if (view.id == R.id.item_task_move_top_tv) {
                val needMoveTopProcess = mAllList[i]

                //客户名称
                val custabbname = needMoveTopProcess.custabbname
                val pmname = needMoveTopProcess.pmname
                var ddate = needMoveTopProcess.ddate
                val contractnum = needMoveTopProcess.contractnum

                val pmNameSpan = StringUtils.getSpanColorString(pmname)
                val custabbnameSpan = StringUtils.getSpanColorString(custabbname)
                val contractnumSpan = StringUtils.getSpanColorString(contractnum)

                val labelColor = ContextCompat.getColor(context, R.color.color_number_9)
                val msg =
                    SpannableStringBuilder().append(
                        StringUtils.getSpanColorString(
                            "工序名称:",
                            labelColor
                        )
                    )
                        .append(pmNameSpan).append("\n")
                        .append(StringUtils.getSpanColorString("客户名称:", labelColor))
                        .append(custabbnameSpan)
                        .append("\n")
                        .append(StringUtils.getSpanColorString("合同号:", labelColor))
                        .append(contractnumSpan)
                        .append("\n")
                        .append("确定进行置顶操作吗?")
                DialogUtils.displayAskMsgDialog(context, msg, "置顶操作", okBlock = {
                    it.dismiss()
                    actionMoveTop(needMoveTopProcess.ccode)
                })
            }
        }


        loadData();
    }

    /**
     * 网络提交置顶操作
     * @param processCode 需要置顶的 工序唯一编码
     */
    private fun actionMoveTop(processCode: String?) {
        mDialogHelper?.showLoading("提交中")
        var params = mutableMapOf<String, Any?>()
        params["code"] = processCode
        RxApiManager.add(this, ServiceBuild.processService.actionQualityProcessMoveTop(params.toBody())
            .response()
            .subscribe(
                {
                    //提交成功
                    onRefresh()
                }, {
                    it.printStackTrace()
                    mDialogHelper?.dismissLoading()
                    mDialogHelper?.displayMsgDialog(msg = it.getDsMsg())
                },
                {
                    mDialogHelper?.dismissLoading()
                    CustomToast.show(context, "置顶成功")
                }
            )
        )
    }

    fun onRefresh() {
        mPageParams.reset()
        mTaskAdapter.loadMoreModule?.isEnableLoadMore = false
        loadData()
    }

    private fun loadData() {
        mEmptyView.showEmptyView(true);
        val subscribe = ServiceBuild.processService.qualityProcessPageList(
            mPageParams.pageNo,
            mPageParams.pageSize,
            mPageParams.opState
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnNext {
                if (it.isFailed) {
                    throw DsException(it)
                }
                if (arrange_work_refreshView.isRefreshing) {
                    arrange_work_refreshView.isRefreshing = false;
                }

            }
            .map {
                it.getPageModel()
            }
            .doOnNext {
                val data = mTaskAdapter.data
                if (it.isFirst) {
                    mAllList.clear()
                    mTaskAdapter.data.clear()
                    mTaskAdapter.notifyDataSetChanged()
                }
                if (it.isFirst && it.isEmpty) {
                    mEmptyView.showEmpty(
                        title = context.getString(R.string.no_find_data), msg = context.getString(
                            R.string.click_refresh_data
                        )
                    ) {
                        loadData()
                    }
                    arrange_work_refreshView.isEnabled = false;
                } else if (it.isNotLoadSize) {
                    //如果不够一页,显示没有更多数据布局
                    initLoadMoreAndRefreshView()
                    mTaskAdapter.loadMoreModule?.loadMoreEnd()
                    arrange_work_refreshView.isEnabled = true;


                } else {
                    initLoadMoreAndRefreshView()
                    mTaskAdapter.loadMoreModule?.loadMoreComplete()
                }
            }
            .filter {
                it.list != null && it.list!!.size() != 0
            }
            .flatMap {
                Observable.fromIterable(it.list)
            }.map {
                val sGson = C.sGson
                sGson.fromJson<ProcessModel>(sGson.toJson(it), ProcessModel::class.java)
            }.map {
                mAllList.add(it)
                var name: String
                @ColorRes var color: Int
                var moveTopVisible = false;
                when (it.opState) {
                    ProcessModel.StatusType.PENDING.type -> {
                        name = context.getString(R.string.un_start_process)
                        moveTopVisible = true;
                        color = StatusTypeEnum.DEFAULT.color
                    }
                    ProcessModel.StatusType.STARTED.type -> {
                        name = context.getString(R.string.start_process)
                        color = StatusTypeEnum.START.color
                    }
                    ProcessModel.StatusType.PAUSED.type -> {
                        name = context.getString(R.string.pause_process)
                        color = StatusTypeEnum.PAUSE.color
                    }
                    ProcessModel.StatusType.ENDED.type -> {
                        when (it.qualityState) {
                            ProcessModel.CheckStatus.OK.status -> {
                                name = context.getString(R.string.ok_process)
                                color = R.color.app_color_theme_8
                            }
                            ProcessModel.CheckStatus.FAILED.status -> {
                                name = context.getString(R.string.failed_process)
                                color = R.color.app_color_theme_2
                            }
                            ProcessModel.CheckStatus.CONCESSION.status -> {
                                name = context.getString(R.string.quality_btn_concession)
                                color = R.color.app_color_theme_2
                            }
                            else -> {
                                name = context.getString(R.string.end_process)
                                color = R.color.app_color_theme_2
                            }
                        }
                    }
                    else -> {
                        name = context.getString(R.string.un_start_process)
                        color = StatusTypeEnum.DEFAULT.color
                    }
                }
                val taskModel = TaskModel(
                    it.pmname.orEmpty(),
                    it.custabbname.orEmpty(),
                    it.contractnum.orEmpty(),
                    name,
                    ContextCompat.getColor(context, color),
                    DateUtils.replaceYYYYMMdd(it.createDate)
                )
                taskModel.moveTopVisible = moveTopVisible
                taskModel

            }
            .subscribe(
                { it ->
                    mTaskAdapter.addData(it)
                },
                {
                    it.printStackTrace()
                    mEmptyView.post {
                        if (mPageParams.isFirst()) {
                            mEmptyView.showFailed(msg = it.getDsMsg()) {
                                it.showEmptyView(true);
                                loadData()
                            }
                        } else {
                            mTaskAdapter.loadMoreModule?.loadMoreFail()
                        }

                    }
                }, {
                    mTaskAdapter.notifyDataSetChanged()
                }
            )
        RxApiManager.instance.add(this, subscribe);
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        RxApiManager.instance.remove(this)
    }

    private fun initLoadMoreAndRefreshView() {
        if (!arrange_work_refreshView.isEnabled) {
            arrange_work_refreshView.isEnabled = true;
        }

        if (!mTaskAdapter.loadMoreModule?.isAutoLoadMore!!) {
            mTaskAdapter.loadMoreModule?.isAutoLoadMore = true;
        }
        if (!mTaskAdapter.loadMoreModule?.isEnableLoadMore!!) {
            mTaskAdapter.loadMoreModule?.isEnableLoadMore = true
        }
    }


    interface CheckWorkControlListener {
        fun startFragment(frag: BaseFragment)
    }
}