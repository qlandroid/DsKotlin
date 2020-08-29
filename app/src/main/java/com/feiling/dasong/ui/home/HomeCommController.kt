package com.feiling.dasong.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import butterknife.ButterKnife
import com.feiling.dasong.comm.BaseFragment
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/2
 * @author ql
 */


abstract class HomeCommController(context: Context) :
    QMUIWindowInsetLayout(context) {
    open lateinit var mHomeControllerListener: HomeControlListener

    init {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val view = bindView(context, layoutInflater);
        if (view != null) {
            ButterKnife.bind(view!!);
        }

        initData();
        initWeight();
    }


    interface HomeControlListener {
        fun startFragment(fragment: BaseFragment)
        fun startActivity(intent: Intent)
        fun logout();
    }

    open fun initData() {

    }

    open fun initWeight() {

    }

    open fun bindView(
        context: Context,
        layoutInflater: LayoutInflater
    ): View? {
        return null;
    }


}