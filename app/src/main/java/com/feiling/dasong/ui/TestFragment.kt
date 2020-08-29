package com.feiling.dasong.ui

import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/2
 * @author ql
 */
class TestFragment : BaseFragment() {

    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_test, null)
    }
}