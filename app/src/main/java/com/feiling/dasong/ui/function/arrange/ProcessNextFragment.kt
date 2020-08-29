package com.feiling.dasong.ui.function.arrange

import android.os.Bundle
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseTopBarFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.http.ServiceBuild
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.fragment_progress_next_details.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/26
 * @author ql
 */
class ProcessNextFragment : BaseTopBarFragment() {

    private var processCode: String? = null;

    companion object {
        const val KEY_PROCESS_CODE = "KEY-PROCESS-CODE"
        fun instance(processCode: String?): ProcessNextFragment {

            var bundle = Bundle()
            bundle.putString(KEY_PROCESS_CODE, processCode)

            var processNextFragment = ProcessNextFragment()
            processNextFragment.arguments = bundle

            return processNextFragment
        }
    }


    override fun createContentView(): View {
        return layoutInflater.inflate(R.layout.fragment_progress_next_details, null);
    }

    override fun initData() {
        super.initData()
        arguments?.let {
            processCode = it.getString(KEY_PROCESS_CODE)
        }
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        processCode?.let {
            loadNextProcess(it)
        }
        nextProcessOk.onClick {
            popBackStack()
        }
    }

    private fun loadNextProcess(code: String) {
        showTipLoading("加载中")
        var disposable = ServiceBuild.processService.getNextProcessDetails(code)
            .response()
            .map {
                var nextProcessName: String? = null;
                it.data?.apply {
                    if (this.isJsonNull) {
                        return@apply;
                    }
                    nextProcessName = this.asJsonObject.get("pmName").asString

                }
                nextProcessName
            }
            .subscribe({
                nextProcessName.text = it;
            }, {
                it.printStackTrace()
                runUiThread {
                    cancelTipLoading()
                    displayMsgDialog(it.getDsMsg())
                }
            }, {
                cancelTipLoading()
            })
        addDisposable(disposable)
    }

}