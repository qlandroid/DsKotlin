package com.feiling.dasong.comm

import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
interface IViewToast {

    fun showToast(message: String?, duration: Int = Toast.LENGTH_SHORT)

    fun showToast(@StringRes msg: Int, duration: Int = Toast.LENGTH_SHORT)
}