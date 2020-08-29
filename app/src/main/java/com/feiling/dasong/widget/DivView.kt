package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.feiling.dasong.R

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/3
 * @author ql
 */
class DivView:View{

    constructor(context: Context) : this(context,null)
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
        val colorDrawable = ColorDrawable()
        colorDrawable.color = ContextCompat.getColor(context, R.color.div_6_color);
        background = colorDrawable
    }
}