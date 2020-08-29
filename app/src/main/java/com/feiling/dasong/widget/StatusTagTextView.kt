package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.feiling.dasong.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/3
 * @author ql
 */
class StatusTagTextView : androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.style.StatusTag
    )

    @SuppressLint("WrongConstant")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.StatusTagTextView, defStyleAttr, 0
        )
        var bgColor =
            a.getColor(
                R.styleable.StatusTagTextView_backgroundColor,
                ContextCompat.getColor(context, R.color.app_color_red)
            )
        val cornerRadius = a.getDimension(
            R.styleable.StatusTagTextView_cornerRadius,
            QMUIDisplayHelper.dp2px(context, 6).toFloat()
        )
        a.recycle()
        val drawable: GradientDrawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE;
        drawable.gradientType = GradientDrawable.RECTANGLE;
        drawable.cornerRadius = cornerRadius
        drawable.setColor(bgColor)
        background = drawable
    }

    @SuppressLint("WrongConstant")
    open fun setBgColor(
        @ColorInt color: Int,
        cornerRadius: Float = QMUIDisplayHelper.dp2px(context, 6).toFloat()
    ) {

        val drawable: GradientDrawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE;
        drawable.gradientType = GradientDrawable.RECTANGLE;
        drawable.cornerRadius = cornerRadius
        drawable.setColor(color)
        background = drawable
    }


}