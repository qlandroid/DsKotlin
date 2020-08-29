package com.feiling.dasong.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.feiling.dasong.R
import kotlin.math.ceil


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/22
 * @author ql
 */
class ResizableImageView : androidx.appcompat.widget.AppCompatImageView {
    var autoSize: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.ResizableImageView, defStyleAttr, 0
        )
        autoSize = a.getBoolean(R.styleable.ResizableImageView_autoSize, false)
        a.recycle();
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d = drawable
        if (d != null && autoSize) {
            // ceil not round - avoid thin vertical gaps along the left/right edges
            val sWidth = MeasureSpec.getSize(widthMeasureSpec)
            val sHeight = MeasureSpec.getSize(heightMeasureSpec)

            if (sWidth != sHeight) {
                layoutParams.width = sWidth
                layoutParams.height = sHeight;
                invalidate()
            }
            //高度根据使得图片的宽度充满屏幕计算而得

            var width = sWidth;
            var height = sHeight;

            if (sWidth / sHeight > d.intrinsicWidth / d.intrinsicHeight) {
                height = ceil(
                    sWidth.toFloat() * d.intrinsicHeight.toFloat() / d.intrinsicWidth.toDouble()
                ).toInt()
            } else {
                width = ceil(
                    sHeight.toFloat() * d.intrinsicWidth.toFloat() / d.intrinsicHeight.toDouble()
                ).toInt()
            }



            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}