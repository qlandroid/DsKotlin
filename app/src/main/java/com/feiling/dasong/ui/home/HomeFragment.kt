package com.feiling.dasong.ui.home

import android.content.Intent
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.ui.HomeActivity
import com.feiling.dasong.ui.LoginFragment
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/3
 * @author ql
 */
class HomeFragment : BaseFragment() {



    lateinit var mPages: HashMap<Pager, HomeCommController>
    private val mPagerAdapter: PagerAdapter = object : PagerAdapter() {
        private var mChildCount = 0
        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun getCount(): Int {
            return mPages.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): QMUIWindowInsetLayout {
            val page: QMUIWindowInsetLayout =
                mPages[Pager.getPagerFromPosition(
                    position
                )]!!;
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            container.addView(page, params)
            return page;
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
            container.removeView(`object` as View)
        }

        override fun getItemPosition(`object`: Any): Int {
            return if (mChildCount == 0) {
                POSITION_NONE
            } else super.getItemPosition(`object`)
        }

        override fun notifyDataSetChanged() {
            mChildCount = count
            super.notifyDataSetChanged()
        }
    }

    override fun createView(): View {
        return LayoutInflater.from(context).inflate(R.layout.fragment_home, null);
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        initTabs()
        initPagers()
    }


    private fun initTabs() {
        val builder = tabs.tabBuilder()
        builder
            .setTextSize(
                QMUIDisplayHelper.sp2px(context, 10),
                QMUIDisplayHelper.sp2px(context, 10)
            )
            .setDynamicChangeIconColor(true)
        val home = builder
            .setNormalDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nav_home_normal
                )
            )
            .setSelectedDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nav_home_selected
                )
            )
            .setText("首页")
            .build(context)
        val task = builder
            .setNormalDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nav_task_normal
                )
            )
            .setSelectedDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nav_task_selected
                )
            )
            .setText("待办任务")
            .build(context)
        val allFunction = builder
            .setNormalDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nav_all_function_normal
                )
            )
            .setSelectedDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nav_all_function_selected
                )
            )
            .setText("全部功能")
            .build(context)
        val my = builder
            .setNormalDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nav_my_normal
                )
            )
            .setSelectedDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nav_my_selected
                )
            )
            .setText("我的")
            .build(context)
        tabs.addTab(home)
            .addTab(task)
            .addTab(allFunction)
            .addTab(my)

    }

    private fun initPagers() {
        val listener: HomeCommController.HomeControlListener = object :
            HomeCommController.HomeControlListener {
            override fun startFragment(fragment: BaseFragment) {
                this@HomeFragment.startFragment(fragment)
            }

            override fun startActivity(intent: Intent) {
                this@HomeFragment.startActivity(intent)
            }

            override fun logout() {
                if (context != null) {
                    val intent = HomeActivity.of(context!!, LoginFragment::class.java)
                    startActivity(intent)
                    activity?.finish()
                }


            }
        }
        mPages = HashMap<Pager, HomeCommController>()
        val homeController: HomeCommController =
            HomeController(context!!);
        homeController.mHomeControllerListener = listener;
        mPages[Pager.HOME] = homeController;
        var taskController: HomeCommController =
            HomeTaskController(context!!);
        taskController.mHomeControllerListener = listener;
        mPages[Pager.TASK] = taskController;

        var allFunctionCommController: HomeCommController =
            HomeFunctionController(context!!);
        allFunctionCommController.mHomeControllerListener = listener;

        mPages[Pager.ALL_FUNCTION] = allFunctionCommController;
        var myController: HomeCommController =
            HomeMyController(context!!);
        myController.mHomeControllerListener = listener;
        mPages[Pager.MY] = myController;

        mPages[Pager.HOME] = homeController
        pager.adapter = mPagerAdapter
        tabs.setupWithViewPager(pager, false)
    }


    enum class Pager {
        HOME, TASK, ALL_FUNCTION, MY;

        companion object {
            fun getPagerFromPosition(position: Int): Pager {
                return when (position) {
                    0 -> HOME
                    1 -> TASK
                    2 -> ALL_FUNCTION
                    3 -> MY
                    else -> HOME
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}