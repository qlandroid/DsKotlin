package com.feiling.dasong.ui.function.statistics

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.comm.toJsonString
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.model.base.PageModel
import com.feiling.dasong.model.process.ProcessTimeStatisticsModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/22
 * @author ql
 */
class WorkHourPageFragment : CommSelectFragment() {

    private val mPageModel: BasePage = BasePage()
    private val mParamsMap: MutableMap<String, String> = mutableMapOf();
    private val mAllList: MutableList<ProcessTimeStatisticsModel> = mutableListOf()

    companion object {
        const val REQUEST_CODE = 200;
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        setTitle("员工工序工时列表")
            .setShowInputGroup(false)

        mCommAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPageModel.nextPage();
            loadPage();
        }

        setOpenRefresh(false)
        setLoadMoreListener {
            mPageModel.reset()
            loadPage() {
                it.isRefreshing = false;
            }
        }

//        topbar.addRightTextButton("查询", R.id.topbar_right_search_button)
//            .onClick {
//                startFragmentForResult(WorkHourParamsFragment(), REQUEST_CODE)
//            }

        mCommAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            var processTimeStatisticsModel = mAllList[i]
            var instance =
                StatProcessWorkingDetailsFragment.instance(processTimeStatisticsModel.ccode!!)
            startFragment(instance)
        }
        showLoading();
        loadPage()
    }


    private fun loadPage(block: (() -> Unit)? = null) {
        var subscribe = ServiceBuild.processService.getWorkHourStaPage(
            mPageModel.pageNo,
            mPageModel.pageSize,
            mParamsMap
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isFailed) {
                    throw DsException(it)
                }
                it.getData(PageModel::class.java);
            }
            .doAfterNext {
                loadMoreEndInit(it)
                block?.let {
                    it()
                }
            }
            .map {

                if (it.isFirst) {
                    mAllList.clear()
                    mCommAdapter.data.clear()
                }
                if (it.isFirst && it.isEmpty) {
                    setOpenRefresh(false)
                } else {
                    setOpenRefresh(true)
                }
                val type =
                    object : TypeToken<MutableList<ProcessTimeStatisticsModel>>() {}.type;

                var toJsonString = it.list?.toJsonString()
                var fromJson = C.sGson.fromJson<MutableList<ProcessTimeStatisticsModel>>(
                    toJsonString,
                    type
                )
                fromJson
            }
            .map {
                mAllList.addAll(it)
                replaceUi(it)
            }
            .subscribe({
                mCommAdapter.data.addAll(it)
            }, {
                it.printStackTrace()
                runUiThread {
                    var msg = ExceptionUtils.getExceptionMessage(it)
                    if (mPageModel.isFirst()) {
                        showFailed(msg = msg.toString()) { emptyView ->
                            emptyView.showEmptyView(true)
                        }
                    } else {
                        mCommAdapter.loadMoreModule?.loadMoreFail();
                    }
                }
            }, {

            })

        addDisposable(subscribe)
    }

    private fun loadMoreEndInit(it: PageModel) {
        if (mCommAdapter.data.isEmpty()) {
            emptyView.showEmpty {
                it.showEmpty()
                loadPage()
            }
        } else {
            hideEmpty()
        }
        comm_slt_pull_layout.isRefreshing = false
        mCommAdapter.loadMoreModule?.loadMoreComplete();
        if (it.isNotLoadSize) {
            mCommAdapter.loadMoreModule?.loadMoreEnd()
        } else {
            mCommAdapter.loadMoreModule?.isEnableLoadMore = true;
        }
        mCommAdapter.notifyDataSetChanged()

    }

    private fun replaceUi(list: MutableList<ProcessTimeStatisticsModel>): MutableList<NavDetailsModel> {
        var mutableListOf = mutableListOf<NavDetailsModel>()
        list.forEach {
            var navDetailsModel = NavDetailsModel()
            navDetailsModel.title = it.memberName
            navDetailsModel.subTitle = it.memberCode
            navDetailsModel.showTag = true;
            if (it.isAuditWorkingTimer) {
                //已审核
                navDetailsModel.tagText = "已审核"
                navDetailsModel.tagBackgroundColor =
                    ContextCompat.getColor(context!!, R.color.audit_ok)
            } else {
                navDetailsModel.tagText = "未审核"
                navDetailsModel.tagBackgroundColor =
                    ContextCompat.getColor(context!!, R.color.audit_un)

            }

            navDetailsModel.children = mutableListOf(
                LabelTextModel(
                    label = "统计工时",
                    txt = "${it.srcTimer} 分钟",
                    textColor = ContextCompat.getColor(context!!, R.color.app_color_red)
                ),
                LabelTextModel(
                    label = "审核工时",
                    txt = "${it.repairTimer} 分钟",
                    textColor = ContextCompat.getColor(context!!, R.color.app_color_red)
                )
            )
            mutableListOf.add(navDetailsModel)
        }
        return mutableListOf;
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE -> {
            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }


    }
}