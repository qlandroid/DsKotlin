package com.feiling.dasong.ui.function.quality

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.acker.simplezxing.activity.CaptureActivity
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator
import com.qmuiteam.qmui.widget.tab.QMUITabSegment
import kotlinx.android.synthetic.main.fragment_process_check.*

/**
 * 描述：质检页面
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/14
 * @author ql
 */
class ProcessQualityFragment : BaseFragment() {
    companion object {
        const val REQUEST_DETAILS = 200
    }

    private val mPageMap: HashMap<ContentPage, ProcessQualityController> =
        HashMap<ContentPage, ProcessQualityController>()
    private val mPageType: HashMap<Int, ContentPage> = HashMap<Int, ContentPage>()
    private val mDestPage: ContentPage = ContentPage.ALL
    private val mPagerAdapter: PagerAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return mPageType.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val page: ContentPage = mPageType[position]!!;
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
        return layoutInflater.inflate(R.layout.fragment_process_check, null)
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("质量检测")
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
        mPageType[0] = ContentPage.ALL
        tabSegment.addTab(
            tabBuilder.setText(context?.getString(R.string.un_start_process)).build(
                context
            )
        )
        mPageType[1] = ContentPage.DEFAULT

        tabSegment.addTab(
            tabBuilder.setText(context?.getString(R.string.start_process)).build(
                context
            )
        )
        mPageType[2] = ContentPage.STARTING
        tabSegment.addTab(
            tabBuilder.setText(context?.getString(R.string.pause_process)).build(
                context
            )
        )
        mPageType[3] = ContentPage.PAUSE
        tabSegment.addTab(tabBuilder.setText(context?.getString(R.string.end_process)).build(context))
        mPageType[4] = ContentPage.END

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
        contentViewPager.setCurrentItem(mDestPage.position, false)
    }

    private fun getPageView(page: ContentPage): View {
        var view = mPageMap[page]
        if (view == null) {
            val ctlListener = object : ProcessQualityController.CheckWorkControlListener {
                override fun startFragment(frag: BaseFragment) {
                    this@ProcessQualityFragment.startFragmentForResult(frag, REQUEST_DETAILS)
                }
            }
            view = ProcessQualityController(context, page, ctlListener)
            mPageMap[page] = view
        }
        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_DETAILS) {
            if (resultCode == QMUIFragment.RESULT_OK) {
                mPageMap.forEach {
                    it.value.onRefresh();
                }
            }
        } else if (requestCode == CaptureActivity.REQ_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val code =
                        data!!.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT)
                    startFragment(ProcessQualityDetailsFragment.instance(code))
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)

    }
}