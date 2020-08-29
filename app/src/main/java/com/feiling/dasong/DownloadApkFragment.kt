package com.feiling.dasong

import android.view.View
import com.feiling.dasong.comm.BaseFragment

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/23
 * @author ql
 */
class DownloadApkFragment : BaseFragment() {
    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_download_apk, null)
    }

    override fun popBackStack() {
//        super.popBackStack()
    }
}