package com.feiling.dasong.uitils

import android.content.Context
import kotlin.math.roundToInt

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/22
 * @author ql
 */
object UIUtils {

    fun spanCount(context: Context, gridExpectedSize: Int): Int {
        val screenWidth: Int = context.resources.displayMetrics.widthPixels
        val expected = screenWidth.toFloat() / gridExpectedSize.toFloat()
        var spanCount = expected.roundToInt()
        if (spanCount == 0) {
            spanCount = 1
        }
        return spanCount
    }
}