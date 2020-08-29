package com.feiling.dasong.widget

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.feiling.dasong.R

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
object CustomToast {
    fun show(context: Context, message: String?, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(context, message, duration)
        val v = toast.view
        val padding = context.resources.getDimension(R.dimen.spacing_large).toInt()
        v.setPadding(padding, padding, padding, padding)
        toast.setGravity(Gravity.CENTER, 0, 0)
        v.setBackgroundResource(R.drawable.toast_custom)
        val text = v.findViewById<TextView>(android.R.id.message)
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        text.setTextColor(Color.WHITE)
        toast.show()
    }

    fun show(context: Context, @StringRes resId: Int) {
        show(context, context.getString(resId))
    }
}