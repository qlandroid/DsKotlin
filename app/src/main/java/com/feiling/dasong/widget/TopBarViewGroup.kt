package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.feiling.dasong.R
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/30
 * @author ql
 */
class TopBarViewGroup : QMUIWindowInsetLayout {

    var topBar: QMUITopBarLayout? = null;
    var emptyView: EmptyView? = null;

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    @SuppressLint("WrongConstant")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {


        var layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val typedValue = TypedValue()
        context!!.theme.resolveAttribute(R.attr.qmui_topbar_height, typedValue, true)
        layoutParams.topMargin = typedValue.getDimension(resources.displayMetrics).toInt()

        var topbar = createTopBar()
        topbar?.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (topbar is QMUITopBarLayout) {
            this.topBar = topbar
        }
        topbar?.fitsSystemWindows = true
        addView(topbar)

        var emptyView = createEmptyView();
        if (emptyView != null) {
            this.emptyView = emptyView;
            emptyView.layoutParams = layoutParams;
            addView(emptyView)
        }

        var childCount = childCount
        println(childCount)

    }


    private fun createTopBar(): View? {
        return QMUITopBarLayout(context)
    }

    private fun createEmptyView(): EmptyView? {
        return EmptyView(context)
    }
}