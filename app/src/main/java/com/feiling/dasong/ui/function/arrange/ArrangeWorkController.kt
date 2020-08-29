package com.feiling.dasong.ui.function.arrange

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
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
import com.feiling.dasong.uitils.*
import com.feiling.dasong.widget.CustomToast
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
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
class ArrangeWorkController(
    context: Context?,
    var page: ContentPage,
    var ctlListener: ArrangeWorkControlListener
) :
    QMUIWindowInsetLayout(context), IArrangeWorkController {


    private val mTaskAdapter = TaskAdapter()

    private val mPageParams: ProcessQuery by lazy { ProcessQuery() }

    private val mAllList: MutableList<ProcessModel> by lazy { mutableListOf<ProcessModel>() }
    private var mDialogHelper: DialogTipHelper? = null

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
        mPageParams.opState = when (page) {
            ContentPage.ALL -> {
                null
            }
            ContentPage.PENDING -> {
                ProcessModel.StatusType.PENDING.type
            }
            ContentPage.STARTED -> {
                ProcessModel.StatusType.STARTED.type
            }
            ContentPage.PAUSED -> {
                ProcessModel.StatusType.PAUSED.type
            }
            ContentPage.END -> {
                ProcessModel.StatusType.ENDED.type
            }
            ContentPage.DEFAULT -> {
                ProcessModel.StatusType.DEFAULT.type
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

        //item的点击事件
        mTaskAdapter.setOnItemClickListener { _: BaseQuickAdapter<Any?, BaseViewHolder>, _: View, i: Int ->
            val item = mAllList[i]
            ctlListener.startFragment(TaskDetailsFragment.instance(item.ccode!!))
        }

        //点击子控件事件
        mTaskAdapter.setOnItemChildClickListener { _: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            LogUtils.d("click item position = ${i}")
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
                    SpannableStringBuilder().append(StringUtils.getSpanColorString("工序名称:", labelColor))
                        .append(pmNameSpan).append("\n")
                        .append(StringUtils.getSpanColorString("客户名称:", labelColor)).append(custabbnameSpan)
                        .append("\n")
                        .append(StringUtils.getSpanColorString("合同号:", labelColor)).append(contractnumSpan)
                        .append("\n")
                        .append("确定进行置顶操作吗?")
                DialogUtils.displayAskMsgDialog(context,msg, "置顶操作", okBlock = {
                    it.dismiss()
                    actionMoveTop(needMoveTopProcess.ccode)
                })
            }
        }

        emptyView.showEmptyView(true)
        loadData();
    }



    private fun loadData() {
        mTaskAdapter.loadMoreModule?.isEnableLoadMore = false
        val subscribe = ServiceBuild.processService.loadTask(
            mPageParams.pageNo,
            mPageParams.pageSize,
            mPageParams.opState
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
                emptyView.hideEmpty();
                if (it.isFirst) {
                    mAllList.clear()
                    mTaskAdapter.data.clear()
                }
                if (it.isFirst && it.isEmpty) {
                    arrange_work_refreshView.isEnabled = false;
                    emptyView.showEmpty { emptyView ->
                        emptyView.showEmptyView(true)
                        loadData();
                    }
                } else if (it.isNotLoadSize) {
                    arrange_work_refreshView.isEnabled = true;
                    //如果不够一页,显示没有更多数据布局
                    initLoadMoreAndRefreshView()
                    mTaskAdapter.loadMoreModule?.loadMoreEnd()

                } else {
                    initLoadMoreAndRefreshView()
                    mTaskAdapter.loadMoreModule?.loadMoreComplete()
                }
                Observable.fromIterable(it.list)
            }
            .map {
                JsonUtils.fromJson<ProcessModel>(it.asJsonObject, ProcessModel::class.java)
            }
            .subscribe(
                { model ->
                    mAllList.add(model)
                    var moveTopVisible = false;
                    val type = when (model.opState) {
                        ProcessModel.StatusType.DEFAULT.type -> {
                            //待分配
                            StatusTypeEnum.DEFAULT
                        }
                        ProcessModel.StatusType.PENDING.type -> {
                            //待开工
                            moveTopVisible = true
                            StatusTypeEnum.PENDING
                        }
                        ProcessModel.StatusType.STARTED.type -> {
                            //已开工
                            moveTopVisible = true
                            StatusTypeEnum.START
                        }
                        ProcessModel.StatusType.PAUSED.type -> {
                            //已暂停
                            moveTopVisible = true
                            StatusTypeEnum.PAUSE
                        }
                        ProcessModel.StatusType.ENDED.type -> {
                            //已完成
                            StatusTypeEnum.END
                        }
                        else -> {
                            StatusTypeEnum.DEFAULT
                        }
                    }
                    val date = DateUtils.replaceYYYYMMdd(model.createDate)
                    val data = TaskModel(
                        model.pmname.orEmpty(),
                        model.custabbname.orEmpty(),
                        model.contractnum.orEmpty(),
                        context.getString(type.label),
                        ContextCompat.getColor(context, type.color),
                        date
                    )
                    data.moveTopVisible = moveTopVisible
                    data.productNo = model.porder
                    mTaskAdapter.addData(
                        data
                    )
                }, {
                    it.printStackTrace()
                    emptyView.post {
                        if (arrange_work_refreshView.isRefreshing) {
                            arrange_work_refreshView.isRefreshing = false
                        }
                        mTaskAdapter.loadMoreModule?.isEnableLoadMore = true;
                        val msg = ExceptionUtils.getExceptionMessage(it)
                        if (mPageParams.isFirst()) {
                            emptyView.showFailed(msg = msg) { emptyView ->
                                emptyView.showEmptyView()
                                loadData()
                            }
                        } else {
                            mTaskAdapter.loadMoreModule?.loadMoreFail()
                        }

                    }
                }, {
                    mTaskAdapter.loadMoreModule?.isEnableLoadMore = true;
                    if (arrange_work_refreshView.isRefreshing) {
                        arrange_work_refreshView.isRefreshing = false
                    }
                    mTaskAdapter.notifyDataSetChanged()
                }
            )
        RxApiManager.add(this, subscribe);
    }

    override fun onDetachedFromWindow() {
        RxApiManager.cancel(this)
        super.onDetachedFromWindow()

    }

    override fun onRefresh() {
        mPageParams.reset()
        mTaskAdapter.loadMoreModule?.isEnableLoadMore = false
        loadData()
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

    /**
     * 网络提交置顶操作
     * @param processCode 需要置顶的 工序唯一编码
     */
    private fun actionMoveTop(processCode: String?) {
        mDialogHelper?.showLoading("提交中")
        var params = mutableMapOf<String, Any?>()
        params["code"] = processCode
        RxApiManager.add(this, ServiceBuild.processService.actionMoveTop(params.toBody())
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



    interface ArrangeWorkControlListener {
        fun startFragment(frag: BaseFragment)
    }
}