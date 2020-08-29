package com.feiling.dasong.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/22
 * @author ql
 */
class MediaGridInset : RecyclerView.ItemDecoration {

    private var mSpanCount = 0
    private var mSpacing = 0
    private var mIncludeEdge = false

    constructor(
        spanCount: Int,
        spacing: Int,
        includeEdge: Boolean
    ) : super() {
        mSpanCount = spanCount
        mSpacing = spacing
        mIncludeEdge = includeEdge
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % mSpanCount // item column
        if (mIncludeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = mSpacing - column * mSpacing / mSpanCount
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * mSpacing / mSpanCount
            if (position < mSpanCount) { // top edge
                outRect.top = mSpacing
            }
            outRect.bottom = mSpacing // item bottom
        } else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = column * mSpacing / mSpanCount
            // spacing - (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount
            if (position >= mSpanCount) {
                outRect.top = mSpacing // item top
            }
        }
    }

}