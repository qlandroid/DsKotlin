package com.feiling.dasong.ui.function.statistics

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.C
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.comm.toStringMap
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.process.ProcessTimeStatisticsModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.google.gson.reflect.TypeToken
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.kotlin.onClick
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*
import kotlinx.android.synthetic.main.fragment_employee_working_stat.emptyView
import kotlinx.android.synthetic.main.fragment_employee_working_stat.topbar
import java.util.*

/**
 * 描述：员工列表 并统计工时
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/16
 * @author ql
 */
class EmplWorkingStatListFragment : CommSelectFragment() {


    private data class Params(
        var code: String? = null,//部门编码
        var startDate: String? = null,//查询开始时间
        var endDate: String? = null //查询结束时间
    )

    private lateinit var mParams: Params;

    private var mSrcList: MutableList<ProcessTimeStatisticsModel>? = null


    private var mFilterList: MutableList<ProcessTimeStatisticsModel>? = null;

    private var mLoadDisposable: Disposable? = null

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)

        topbar.addLeftBackImageButton().onClick { popBackStack() }
        topbar.setTitle("员工工时列表")


        mParams = Params()
        setOpenRefresh(true)
        setLoadMoreListener {
            loadData()
        }
        setLoadMoreListener {
            loadData()
        }
        setShowInputGroup(true, "查询员工名称/编码")
        setInputListener { filterTxt ->
            mFilterList = mutableListOf();
            mLoadDisposable?.let {
                if (it.isDisposed) {
                    it.dispose()
                }
            }
            mLoadDisposable = Observable.fromIterable(mSrcList)
                .filter {
                    (it.memberName.orEmpty().contains(filterTxt)
                            || it.memberCode.orEmpty().contains(filterTxt))
                }
                .subscribe({
                    mFilterList?.add(it)
                }, {
                    it.printStackTrace()
                }, {
                    var replaceUiDate = mFilterList?.let { replaceUiDate(it) }
                    mCommAdapter.setNewData(replaceUiDate)
                    mCommAdapter.notifyDataSetChanged()
                    mCommAdapter.loadMoreModule?.loadMoreEnd()
                })
            addDisposable(mLoadDisposable!!)

        }

        mCommAdapter.loadMoreModule?.setOnLoadMoreListener { }
        mCommAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            var itemModel = mFilterList?.get(i)
            if (itemModel == null) {
                mDialogTipHelper.displayMsgDialog(msg = "异常");
                return@setOnItemClickListener;
            }


            var instance =
                EmployeeWorkingStatisticsFragment.instance(itemModel?.memberCode.orEmpty())
            startFragment(instance)

        }

        var instance = Calendar.getInstance()
        var year = instance.get(Calendar.YEAR)
        var month = instance.get(Calendar.MONTH)
        var day = instance.get(Calendar.DAY_OF_MONTH);

        mParams.startDate = "${year}-${month + 1}-${1} 00:00:00"
        mParams.endDate = "${year}-${month + 1}-${day} 23:59:59"

        emptyView.showEmptyView(true);
        loadData()
    }

    private fun loadData() {
        var subscribe = ServiceBuild.processService.getEmployeeWorkingList(mParams.toStringMap())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                comm_slt_pull_layout.isRefreshing = false
                if (it.isFailed) {
                    throw DsException(it)
                }
                JsonUtils.toJson(it.data)
            }
            .map {
                val type =
                    object : TypeToken<MutableList<ProcessTimeStatisticsModel>>() {}.type;
                C.sGson.fromJson<MutableList<ProcessTimeStatisticsModel>>(it, type)
            }
            .subscribe({
                mSrcList = it;
                mFilterList = it;
                val replaceUiDate = replaceUiDate(it)
                mCommAdapter.setNewData(replaceUiDate)
                mCommAdapter.loadMoreModule?.loadMoreEnd()
            }, {
                it.printStackTrace()
                runUiThread {
                    comm_slt_pull_layout.isRefreshing = false
                    emptyView.showFailed(msg = ExceptionUtils.getExceptionMessage(it)) {

                        loadData()
                    }
                }
            }, {
                if (mSrcList.orEmpty().isEmpty()) {
                    emptyView.showEmpty { this.loadData() }
                } else {
                    emptyView.hideEmpty();
                }
            })
        addDisposable(subscribe)
    }

    private fun replaceUiDate(list: MutableList<ProcessTimeStatisticsModel>): MutableList<NavDetailsModel> {
        var rList = mutableListOf<NavDetailsModel>()
        var subscribe = Observable.fromIterable(list)
            .map {
                var ndm = NavDetailsModel()
                ndm.title = it.memberName
                ndm.subTitle = it.memberCode
                ndm.hint = "总计工时:\t${it.stan3}\t分钟"
                ndm
            }
            .subscribe {
                rList.add(it)
            }
        addDisposable(subscribe)


        return rList;
    }


}