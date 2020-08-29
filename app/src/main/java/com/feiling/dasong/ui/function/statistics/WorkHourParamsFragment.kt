package com.feiling.dasong.ui.function.statistics

import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.fragment_work_hour_params.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/22
 * @author ql
 */
class WorkHourParamsFragment : BaseFragment() {
    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_work_hour_params, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("查询信息")
        topbar.addLeftBackImageButton().onClick {
            popBackStack()
        }
        topbar.addRightTextButton("清空查询", R.id.topbar_right_clean_button).onClick {

        }
        topbar.addRightTextButton("查询", R.id.topbar_right_search_button).onClick {
            setFragmentResult(QMUIFragment.RESULT_OK, null);
            popBackStack()
        }
    }

}