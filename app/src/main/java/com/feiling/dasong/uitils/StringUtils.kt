package com.feiling.dasong.uitils

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.annotation.ColorInt
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/20
 * @author ql
 */
object StringUtils {


    fun replaceNumber(numb: String): String {
        if (numb.isEmpty()) {
            return "";
        }
        var bigDecimal = BigDecimal(numb)
        return DecimalFormat("#").format(bigDecimal.toDouble())
    }

    fun replaceDouble(numb: Double): String {
        var bigDecimal = BigDecimal(numb)
        return DecimalFormat("#").format(bigDecimal.toDouble())
    }

    /**
     * 获得可变颜色的字符串
     */
    fun getSpanColorString(text: String?, @ColorInt color: Int = Color.RED): SpannableString {
        val spannableString = SpannableString(text)
        val foregroundColorSpan = ForegroundColorSpan(color)
        spannableString.setSpan(
            foregroundColorSpan,
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            UnderlineSpan(),
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }
}