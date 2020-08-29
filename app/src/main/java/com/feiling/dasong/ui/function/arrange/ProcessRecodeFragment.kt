package com.feiling.dasong.ui.function.arrange

import android.os.Bundle
import android.view.View
import com.feiling.dasong.C
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.process.OProcessRecodeModel
import com.feiling.dasong.ui.function.arrange.adapter.ProcessRecodeAdapter
import com.feiling.dasong.ui.function.arrange.model.ProcessRecodeModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：工序的操作记录
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/20
 * @author ql
 */
class ProcessRecodeFragment : CommSelectFragment() {


    companion object {
        const val KEY_PMCODE = "KEY-PMCODE"
        fun instance(pmcode: String?): ProcessRecodeFragment {

            val bundle = Bundle()
            bundle.putString(KEY_PMCODE, pmcode)

            val processRecodeFragment = ProcessRecodeFragment()
            processRecodeFragment.arguments = bundle
            return processRecodeFragment
        }
    }


    private val mAdapter = ProcessRecodeAdapter()
    private lateinit var mProcessCode: String
    private var mDataList: MutableList<OProcessRecodeModel>? = null

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        comm_select_rv.adapter = mAdapter
        mProcessCode = arguments?.getString(KEY_PMCODE).orEmpty();
        topbar.setTitle("操作记录")

        mAdapter.loadMoreModule?.setOnLoadMoreListener {

        }
        setLoadMoreListener {
            loadData()
        }
        emptyView.showEmptyView()
        loadData()
    }


    private fun loadData() {
        mDataList?.clear()
        mAdapter.data.clear()
        mAdapter.loadMoreModule?.isEnableLoadMore = false
        val subscribe = ServiceBuild.processService.processRecord(mProcessCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter {
                if (it.isFailed) {
                    throw DsException(it)
                }
                it.data is JsonArray
            }
            .map {
                setIsRefresh(false)
                val type = object : TypeToken<MutableList<OProcessRecodeModel>>() {}.getType();
                C.sGson.fromJson<MutableList<OProcessRecodeModel>>(it.data, type)
            }
            .flatMap {
                mDataList = it;
                Observable.fromIterable(it)
            }
            .map {
                when {
                    it.isCheck -> {
                        //当前是质检工序
                        ProcessRecodeModel.BuilderCheck()
                            .optUser(it.createdname, it.createdby)
                            .date(it.createddate)
                            .status(ProcessRecodeModel.Status.getStatus(it.type))
                            .build()
                    }
                    it.opType == OProcessRecodeModel.TYPE_EMPLOYEE -> {
                        ProcessRecodeModel.BuilderEmployee()
                            .optUser(it.createdname, it.createdby)
                            .employee(it.firstname, it.memberCode)
                            .date(it.createddate)
                            .optDev(it.ceqname, it.ceqcode)
                            .status(ProcessRecodeModel.Status.getStatus(it.type))
                            .build()
                    }
                    else -> {
                        ProcessRecodeModel.BuilderGroup()
                            .optUser(it.createdname, it.createdby)
                            .optGroup(it.gruopName, it.groupCode)
                            .date(it.createddate)
                            .optDev(it.ceqname, it.ceqcode)
                            .status(ProcessRecodeModel.Status.getStatus(it.type))
                            .build()
                    }
                }

            }
            .subscribe({
                mAdapter.addData(it)
            }, {
                it.printStackTrace()
                runUiThread {
                    setIsRefresh(false)
                    emptyView.showFailed(msg = ExceptionUtils.getExceptionMessage(it)) {
                        emptyView.showEmptyView()
                    }
                }
            }, {
                if (mDataList.isNullOrEmpty()) {
                    emptyView.showEmpty {
                        loadData()
                    }
                } else {
                    if (emptyView.isShowing) {
                        emptyView.hideEmpty()
                    }
                    mAdapter.loadMoreModule?.isEnableLoadMore = true
                    mAdapter.notifyDataSetChanged()
                    mAdapter.loadMoreModule!!.loadMoreEnd()

                }

            })

        addDisposable(subscribe)


    }
}