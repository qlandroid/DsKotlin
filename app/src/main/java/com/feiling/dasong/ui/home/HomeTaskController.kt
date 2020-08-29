package com.feiling.dasong.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.BindView
import com.feiling.dasong.R
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import kotlinx.android.synthetic.main.home_task.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/2
 * @author ql
 */
class HomeTaskController(context: Context) :
    HomeCommController(context) {


    override fun bindView(context: Context, layoutInflater: LayoutInflater): View? {
        return layoutInflater.inflate(R.layout.home_task, this);
    }

    override fun initWeight() {
        super.initWeight()
        topbar.setTitle("待办任务")
        homeTaskRv.layoutManager = LinearLayoutManager(context)
        emptyView.showEmpty()
    }
}