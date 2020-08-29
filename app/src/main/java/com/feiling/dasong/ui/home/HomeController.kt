package com.feiling.dasong.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import butterknife.BindView
import com.feiling.dasong.R
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import kotlinx.android.synthetic.main.home_first.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/2
 * @author ql
 */
class HomeController(context: Context) :
    HomeCommController(context) {
    @BindView(R.id.topbar)
    lateinit var mTopBar: QMUITopBarLayout


    override fun bindView(context: Context, layoutInflater: LayoutInflater): View? {
        return layoutInflater.inflate(R.layout.home_first, this);
    }

    override fun initWeight() {
        super.initWeight()
        mTopBar.setTitle("首页")

        emptyView.showEmpty()
    }
}