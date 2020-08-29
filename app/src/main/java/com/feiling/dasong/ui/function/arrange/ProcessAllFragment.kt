package com.feiling.dasong.ui.function.arrange

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.PCProcessModel
import com.feiling.dasong.ui.model.NavDetailsModel

/**
 * 描述：全部工序列表
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/26
 * @author ql
 */
class ProcessAllFragment : CommSelectFragment() {


    /**
     * 需要查询的工序编码
     */
    private var processCode: String? = null;

    /**
     * 工序唯一值
     */
    private var processSdid: Int? = null

    companion object {
        const val KEY_PROCESS_CODE = "KEY-PROCESS-CODE"
        const val KEY_PROCESS_ONLY_ID = "KEY-KEY_PROCESS_ONLY_ID-CODE"

        fun instance(code: String?, processSdid: Int): ProcessAllFragment {
            var bundle = Bundle()

            bundle.putString(KEY_PROCESS_CODE, code)
            bundle.putInt(KEY_PROCESS_ONLY_ID, processSdid)

            var processAllFragment = ProcessAllFragment()
            processAllFragment.arguments = bundle
            return processAllFragment
        }
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        setTitle("全部工序")
        setShowInputGroup(false)

        arguments?.let {
            processCode = it.getString(KEY_PROCESS_CODE)
            processSdid = it.getInt(KEY_PROCESS_ONLY_ID)
        }
        setOpenRefresh(false)
        loadAllProcess()
    }


    /**
     * 加载全部工序
     */
    private fun loadAllProcess() {
        showLoading()
        mCommAdapter.data.clear()
        mCommAdapter.notifyDataSetChanged()
        var disposable = ServiceBuild.processService.getProcessAll(processCode!!)
            .response()
            .map {
                var list: MutableList<PCProcessModel> =
                    it.getList<PCProcessModel>(MutableMap::class.java)
                list
            }
            .subscribe({ list ->
                list.sortBy { pcProcessModel ->
                    pcProcessModel.priority
                }
                //已经处理颜色
                var okColor = ContextCompat.getColor(context!!, R.color.green_0)
                //待处理颜色
                var waitColor = ContextCompat.getColor(context!!, R.color.app_color_theme_1)
                //当前工序颜色
                var currentColor = ContextCompat.getColor(context!!, R.color.app_color_theme_5)
                list.forEach { it ->
                    if (it.isdelete == 1) {
                        return@forEach;
                    }
                    var navDetailsModel = NavDetailsModel()
                    navDetailsModel.title = it.pmName
                    navDetailsModel.showTag = true;

                    //设置tag
                    if (it.sdid == processSdid) {
                        navDetailsModel.tagBackgroundColor = currentColor;
                        navDetailsModel.tagText = getString(R.string.tag_process_current)
                    } else {
                        //0 未开工 1待开工 2已完工 ,
                        when (it.state) {
                            2 -> {
                                navDetailsModel.tagBackgroundColor = okColor
                                navDetailsModel.tagText = getString(R.string.tag_process_end)
                            }
                            else -> {
                                navDetailsModel.tagBackgroundColor = waitColor
                                navDetailsModel.tagText = getString(R.string.tag_process_wait)
                            }
                        }
                    }




                    mCommAdapter.addData(navDetailsModel)
                }

            }, {
                it.printStackTrace()
                runUiThread {
                    setIsRefresh(false)
                    showFailed(it.getDsMsg()) { emptyView ->
                        emptyView.showEmptyView(true)
                        loadAllProcess()
                    }
                }
            }, {
                hideEmpty()
                setIsRefresh(false)
                mCommAdapter.loadMoreModule?.loadMoreEnd()
                mCommAdapter.notifyDataSetChanged()

                setOpenRefresh(true)
                setRefresh {
                    loadAllProcess();
                }
            })
        addDisposable(disposable)
    }

}