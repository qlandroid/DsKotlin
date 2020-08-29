package com.feiling.dasong.ui.function.arrange

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.ui.function.dev.DevAllSelectFragment
import com.feiling.dasong.model.DevModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.google.gson.JsonElement
import com.qmuiteam.qmui.arch.QMUIFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class ProcessDevSelectFragment : CommSelectFragment() {
    private val mAllList: MutableList<DevModel> = mutableListOf<DevModel>()


    private lateinit var pmcode: String //工序的编码

    companion object {
        const val REQUEST_ALL_DEV = 200
        const val KEY_PM_CODE: String = "KEY_PM_CODE"
        private const val KEY_DATA = "DATA"
        fun instance(pmcode: String): ProcessDevSelectFragment {


            val devSelectFragment = ProcessDevSelectFragment()
            val args = Bundle();
            args.putString(KEY_PM_CODE, pmcode)

            devSelectFragment.arguments = args

            return devSelectFragment;
        }

        fun getDevModel(data: Intent): DevModel {
            val devJson = data.getStringExtra(KEY_DATA)
            return C.sGson.fromJson<DevModel>(
                devJson, DevModel::class.java
            )
        }
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("设备选择")
        pmcode = arguments!!.getString(KEY_PM_CODE)

        mCommAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            val intent = Intent()
            intent.putExtra(KEY_DATA, C.sGson.toJson(mAllList[i]))
            setFragmentResult(RESULT_OK, intent)
            popBackStack()
        }
        comm_slt_pull_layout.setOnRefreshListener {
            loadData()
        }

        emptyView.showEmptyView()
        loadData()
    }

    private fun loadData() {

        val subscribe = ServiceBuild.processService.getDevListByPCode(pmcode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap() { t ->
                if (t.isFailed) {
                    throw DsException(t.message!!)
                }
                if (comm_slt_pull_layout.isRefreshing) {
                    mCommAdapter.data.clear()
                    mAllList.clear()
                }
                Observable.fromIterable<JsonElement>(t.data?.asJsonArray)
            }
            .map { t ->
                C.sGson.fromJson(t, DevModel::class.java)
            }
            .subscribe({ devModel ->
                mAllList.add(devModel)
                mCommAdapter.addData(
                    NavDetailsModel(
                        devModel.ceqname,
                        mutableListOf(
                            LabelTextModel(getString(R.string.element_dev_code), devModel.ceqcode!!)
                        )
                    )
                )
            }, {
                it.printStackTrace()
                topbar?.post {
                    mDialogTipHelper.dismissLoading()
                    emptyView.showFailed(msg = ExceptionUtils.getExceptionMessage(it)) { empty ->
                        empty.showEmptyView()
                        loadData()
                    }
                }
            }, {
                if (emptyView.isShowing) {
                    emptyView.hideEmpty()
                }

                if (mAllList.size == 0) {
                    emptyView.showEmpty {
                        it.showEmptyView()
                        loadData()
                    }
                } else {
                    comm_slt_pull_layout.visibility = View.VISIBLE

                    if (comm_slt_pull_layout.isRefreshing) {
                        comm_slt_pull_layout.isRefreshing = false;
                    }

                    mCommAdapter.loadMoreModule?.loadMoreEnd()
                }

            })

        RxApiManager.instance.add(this, subscribe)
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ALL_DEV -> {
                if (resultCode == QMUIFragment.RESULT_OK) {
                    mDialogTipHelper.showSuccessTip(getString(R.string.select_success), topbar) {
                        mDialogTipHelper.dismissSuccess()
                        val devModel = DevAllSelectFragment.getData(data)
                        val intent = Intent()
                        intent.putExtra(KEY_DATA, C.sGson.toJson(devModel))
                        setFragmentResult(RESULT_OK, intent)
                        popBackStack()
                    }


                }
            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }

    }


}