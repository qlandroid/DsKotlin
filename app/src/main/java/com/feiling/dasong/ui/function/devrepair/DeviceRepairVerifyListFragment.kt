package com.feiling.dasong.ui.function.devrepair

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.BaseTopBarFragment
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator
import com.qmuiteam.qmui.widget.tab.QMUITabSegment
import kotlinx.android.synthetic.main.fragment_arrange_work.*

/**
 * 描述：设备维修申请
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/7
 * @author ql
 */
class DeviceRepairVerifyListFragment : BaseFragment() {
    private val mPageMap: HashMap<DeviceRepairVerifyListContentPage, DeviceRepairVerifyListView> =
        HashMap<DeviceRepairVerifyListContentPage, DeviceRepairVerifyListView>()
    private val mDestPage: DeviceRepairVerifyListContentPage =
        DeviceRepairVerifyListContentPage.UNVERIFY
    private val mPageType: HashMap<Int, DeviceRepairVerifyListContentPage> =
        HashMap<Int, DeviceRepairVerifyListContentPage>();

    private val mPagerAdapter: PagerAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return mPageType.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val page: DeviceRepairVerifyListContentPage = mPageType[position]!!

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
        return layoutInflater.inflate(R.layout.fragment_arrange_work, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("维修审核")
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

        tabSegment.addTab(tabBuilder.setText("待审核").build(context))
        mPageType[0] = DeviceRepairVerifyListContentPage.UNVERIFY

        tabSegment.addTab(tabBuilder.setText("已审核").build(context))
        mPageType[1] = DeviceRepairVerifyListContentPage.VERIFY


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

    private fun getPosition(pageView: DeviceRepairVerifyListContentPage): Int {
        var keys = mPageType.keys
        for (key in keys) {
            if (mPageType[key] == mDestPage) {
                return key
            }
        }
        return 0;
    }

    private fun getPageView(page: DeviceRepairVerifyListContentPage): View {
        var view = mPageMap[page]
        if (view == null) {
            val ctlListener = object : DeviceRepairVerifyListView.ArrangeWorkControlListener {
                override fun startFragment(frag: BaseFragment) {
                    startFragmentForResult(
                        frag,
                        DeviceRepairListFragment.REQUEST_DETAILS
                    )
                }
            }

            view = DeviceRepairVerifyListView(context, page, ctlListener)
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
            DeviceRepairListFragment.REQUEST_DETAILS -> {
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

    override fun onDestroy() {
        mPageMap.clear()
        super.onDestroy()
    }
}