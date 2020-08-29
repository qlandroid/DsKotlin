package com.feiling.dasong.ui.function.arrange

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.ParameterizedTypeImpl
import com.feiling.dasong.comm.response
import com.feiling.dasong.decorator.DividerItemDecoration2
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.ui.function.arrange.adapter.TaskAdapter
import com.feiling.dasong.model.process.ProcessModel
import com.feiling.dasong.ui.function.arrange.adapter.process.ProcessChildrenNode
import com.feiling.dasong.ui.function.arrange.adapter.process.ProcessNode
import com.feiling.dasong.ui.function.arrange.adapter.process.ProcessNodeAdapter
import com.feiling.dasong.ui.function.arrange.model.ProcessQuery
import com.feiling.dasong.ui.function.arrange.model.StatusTypeEnum
import com.feiling.dasong.ui.function.arrange.model.TaskModel
import com.feiling.dasong.uitils.DateUtils
import com.feiling.dasong.uitils.ExceptionUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.arrange_work.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/4
 * @author ql
 */
class ArrangeWorkGroupController(
    context: Context?,
    var page: ContentPage,
    var ctlListener: ArrangeWorkControlListener
) :
    QMUIWindowInsetLayout(context), IArrangeWorkController {

    companion object {
        /**
         * 分组返回值 工序类别下的list
         */
        const val RESULT_KEY_CHILDREN = "opProcessList"

        /**
         * 分组返回值 工序的编码
         */
        const val RESULT_KEY_PROCESS_CODE = "pmcode"

        /**
         * 分组返回值 工序的名称
         */
        const val RESULT_KEY_PROCESS_NAME = "pmname"
    }


    private val mTaskAdapter = ProcessNodeAdapter()

    private val mPageParams: ProcessQuery by lazy { ProcessQuery() }

    private val mAllList: MutableList<ProcessModel> by lazy { mutableListOf<ProcessModel>() }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.arrange_work, this)
        ButterKnife.bind(view)
        initView()
    }

    private fun initView() {
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
                QMUIDisplayHelper.dp2px(context, 1),
                ContextCompat.getColor(context, R.color.div_color)
            )
        )

        arrange_work_rv.adapter = mTaskAdapter
        mTaskAdapter.setChildrenClick {
            val childrenNode = it as ProcessChildrenNode
            var processModel = childrenNode.key as ProcessModel
            ctlListener.startFragment(TaskDetailsFragment.instance(processModel.ccode!!))
        }

        emptyView.showEmptyView(true)
        loadData();
    }

    private fun loadData() {
        mTaskAdapter.loadMoreModule?.isEnableLoadMore = false
        val subscribe = ServiceBuild.processService.getProcessGroupList(
            mPageParams.opState
        )
            .response()
            .map {
                var list = it.getList<MutableMap<String, Any?>>(MutableMap::class.java)
                list
            }.flatMap {
                emptyView.hideEmpty()
                Observable.fromIterable(it)
            }
            .subscribe(
                { group ->
                    val mutableMap = group[RESULT_KEY_CHILDREN]
                    val toJson = JsonUtils.toJson(mutableMap)
                    val childrenSrcList = Gson().fromJson<MutableList<ProcessModel>>(
                        toJson,
                        ParameterizedTypeImpl(ProcessModel::class.java)
                    )

                    val childrenList = mutableListOf<BaseNode>()
                    childrenSrcList.forEach { processModel ->
                        var processChildrenNode =
                            ProcessChildrenNode()
                        processChildrenNode.clientName = processModel.custabbname
                        processChildrenNode.contract = processModel.contractnum
                        processChildrenNode.productNo = processModel.porder
                        processChildrenNode.key = processModel
//                        processChildrenNode.statusName = context.getString(type.label)
                        childrenList.add(processChildrenNode)
                    }
                    var pmName = "";
                    var pmCode = "";
                    group[RESULT_KEY_PROCESS_CODE]?.let {
                        pmCode = it as String
                    }
                    group[RESULT_KEY_PROCESS_NAME]?.let {
                        pmName = it as String
                    }

                    var processNode = ProcessNode(pmName, pmCode)
                    processNode.childNode = childrenList
                    mTaskAdapter.addData(
                        processNode
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


    interface ArrangeWorkControlListener {
        fun startFragment(frag: BaseFragment)
    }
}