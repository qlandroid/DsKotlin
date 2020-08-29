package com.feiling.dasong.comm

import android.app.Dialog
import android.view.View
import androidx.appcompat.app.AppCompatDialog

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
interface IDialogTip {

    fun showTipLoading(msg: String? = null)

    fun cancelTipLoading()

    fun showTipFailed(
        hint: CharSequence?,
        timer: Long = 2_000,
        block: ((AppCompatDialog?) -> Unit)? = null
    )

    fun showTipSuccess(
        hint: String?,
        timer: Long = 2_000,
        block: ((AppCompatDialog?) -> Unit)? = null
    )

    fun displayMsgDialog(msg: CharSequence?, title: String? = "提示")

    fun displayAskMsgDialog(
        msg: CharSequence?,
        title: String? = "提示",
        okBtnName: String? = "确定",
        okBlock: ((Dialog) -> Unit)? = null,
        cancelBtnName: String? = "取消",
        cancelBlock: ((Dialog) -> Unit)? = null
    )
}