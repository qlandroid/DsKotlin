package com.feiling.dasong.ui.function.arrange

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.decorator.DivUtils
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.process.ProcessTimeStatisticsModel
import com.feiling.dasong.ui.function.arrange.adapter.ProcessTimerAdapter
import com.feiling.dasong.ui.function.arrange.model.ProcessTimerItem
import com.feiling.dasong.uitils.ExceptionUtils
import com.feiling.dasong.widget.EmptyView
import com.google.gson.reflect.TypeToken
import com.qmuiteam.qmui.kotlin.onClick
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_process_timer.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/25
 * @author ql
 */
class ProcessTimerStatFragment : BaseFragment() {
    private var mAdapter = ProcessTimerAdapter()

    companion object {
        private const val KEY_CODE = "KEY-CODE"
        fun instance(code: String?): ProcessTimerStatFragment {
            val bundle = Bundle()
            bundle.putString(KEY_CODE, code)

            val processTimerStatFragment = ProcessTimerStatFragment()
            processTimerStatFragment.arguments = bundle
            return processTimerStatFragment
        }
    }

    private var mOptProcessCode: String? = null;

    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_process_timer, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)

        topbar.addLeftBackImageButton().onClick { popBackStack() }
        topbar.setTitle("工序工时统计")
        mOptProcessCode = arguments?.getString(KEY_CODE).orEmpty()

        processTimerRv.layoutManager = LinearLayoutManager(context)
        processTimerRv.addItemDecoration(DivUtils.getDivDefault(context, 1))
        processTimerRv.adapter = mAdapter
        var aemptyView = EmptyView(context)
        aemptyView.showEmpty() {
            loadData();
        }
        mAdapter.setEmptyView(aemptyView);

        emptyView.showEmptyView()
        loadData()

    }

    private fun loadData() {
        val subscribe = ServiceBuild.processService.processStatistics(mOptProcessCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isFailed) {
                    throw DsException(it)
                }
                val toJson = C.sGson.toJson(it.data)
                val type = object : TypeToken<MutableList<ProcessTimeStatisticsModel>>() {}.type
                C.sGson.fromJson<MutableList<ProcessTimeStatisticsModel>>(toJson, type)
            }
            .flatMap {
                Observable.fromIterable(it)
            }
            .map {
                var timer = it.repairTimer
                if (timer == null) {
                    timer = it.srcTimer
                }
                ProcessTimerItem(it.memberName, it.memberCode, it.srcTimer, timer)
            }
            .subscribe({
                mAdapter.addData(it)
            }, {
                it.printStackTrace()
                topbar?.post {
                    emptyView.showFailed(msg = ExceptionUtils.getExceptionMessage(it)) { empty ->
                        empty.showEmptyView()
                        loadData()
                    }
                }
            }, {
                emptyView.hideEmpty()
                mAdapter.notifyDataSetChanged()
            })
        RxApiManager.instance.add(this, subscribe)
    }
}