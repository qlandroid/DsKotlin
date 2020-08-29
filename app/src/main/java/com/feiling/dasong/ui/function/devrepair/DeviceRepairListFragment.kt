package com.feiling.dasong.ui.function.devrepair

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator
import com.qmuiteam.qmui.widget.tab.QMUITabSegment
import kotlinx.android.synthetic.main.fragment_arrange_work.*

/**
 * 描述：派工
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/2
 * @author ql
 */
open class DeviceRepairListFragment : BaseFragment() {

    companion object {
        //跳转到详情
        const val REQUEST_DETAILS = 1000
    }

    private val mPageMap: HashMap<DeviceRepairListContentPage, DeviceRepairListView> =
        HashMap<DeviceRepairListContentPage, DeviceRepairListView>()
    private val mDestPage: DeviceRepairListContentPage = DeviceRepairListContentPage.ALL
    private val mPageType: HashMap<Int, DeviceRepairListContentPage> =
        HashMap<Int, DeviceRepairListContentPage>();
    private val mPagerAdapter: PagerAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return mPageType.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val page: DeviceRepairListContentPage = mPageType[position]!!

            val view: View = getPageView(page)
            view.tag = position
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            container.addView(view, params)
            return view
        }

        override fun destroyItem(
            container: ViewGroup,
            position: Int,
            `object`: Any
        ) {
            container.removeView(`object` as View)
        }

        override fun getItemPosition(`object`: Any): Int {
            val view = `object` as View
            val page = view.tag
            if (page is Int) {
                return if (page >= mPageType.size) {
                    POSITION_NONE
                } else POSITION_UNCHANGED
            }
            return POSITION_NONE
        }
    }


    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_arrange_work, null);
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("设备维修单")
        var backBtn = topbar.addLeftBackImageButton()
        backBtn.setOnClickListener {
            popBackStack()
        }


        topbar.addRightImageButton(R.drawable.icon_scanning, R.id.topbar_right_scanning_button)
            .setOnClickListener {
                toScanning()
            }

        initTabAndPager()
    }

    private fun initTabAndPager() {

        val tabBuilder = tabSegment.tabBuilder()
        tabSegment.addTab(
            tabBuilder.setText("全部")
                .build(context)
        )
        mPageType[0] = DeviceRepairListContentPage.ALL

        tabSegment.addTab(tabBuilder.setText("待维修").build(context))
        mPageType[1] = DeviceRepairListContentPage.DEFAULT

        tabSegment.addTab(tabBuilder.setText("已开工").build(context))
        mPageType[2] = DeviceRepairListContentPage.STARTED

        tabSegment.addTab(tabBuilder.setText("已暂停").build(context))
        mPageType[3] = DeviceRepairListContentPage.PAUSED

        tabSegment.addTab(tabBuilder.setText("已完成").build(context))
        mPageType[4] = DeviceRepairListContentPage.END

        val space = QMUIDisplayHelper.dp2px(context, 16)
        tabSegment.setIndicator(
            QMUITabIndicator(
                QMUIDisplayHelper.dp2px(context, 2), false, true
            )
        )
        tabSegment.mode = QMUITabSegment.MODE_SCROLLABLE
        tabSegment.setItemSpaceInScrollMode(space)
        tabSegment.setupWithViewPager(contentViewPager, false)
        tabSegment.setPadding(space, 0, space, 0)
        tabSegment.addOnTabSelectedListener(object : QMUITabSegment.OnTabSelectedListener {
            override fun onTabSelected(index: Int) {
            }

            override fun onTabUnselected(index: Int) {
            }

            override fun onTabReselected(index: Int) {
            }

            override fun onDoubleTap(index: Int) {
            }
        })

        contentViewPager.adapter = mPagerAdapter

        contentViewPager.setCurrentItem(getPosition(mDestPage), false)
    }

    private fun getPosition(pageView: DeviceRepairListContentPage): Int {
        var keys = mPageType.keys
        for (key in keys) {
            if (mPageType[key] == mDestPage) {
                return key
            }
        }
        return 0;
    }

    private fun getPageView(page: DeviceRepairListContentPage): View {
        var view = mPageMap[page]
        if (view == null) {
            val ctlListener = object : DeviceRepairListView.ArrangeWorkControlListener {
                override fun startFragment(frag: BaseFragment) {
                    this@DeviceRepairListFragment.startFragmentForResult(frag, REQUEST_DETAILS)
                }
            }


            view = DeviceRepairListView(context, page, ctlListener)
            mPageMap[page] = view
        }
        return view
    }


    override fun onScanningCodeResult(code: String?, requestCode: Int?) {
        super.onScanningCodeResult(code, requestCode)
        //TODO 跳转 详情
//        startFragmentForResult(TaskDetailsFragment.instance(code!!), REQUEST_DETAILS)
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_DETAILS -> {
                if (resultCode == QMUIFragment.RESULT_OK) {
                    mPageMap.forEach {
                        it.value.let { ctl ->
                            ctl.onRefresh()
                        }
                    }
                }
            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }

    }

}