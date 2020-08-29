package com.feiling.dasong.uitils

import android.app.Dialog
import android.content.Context
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/28
 * @author ql
 */
object DialogUtils {

    fun displayAskMsgDialog(
        context:Context?,
        msg: CharSequence?,
        title: String? = "提示",
        okBtnName: String? = "确认",
        okBlock: ((Dialog) -> Unit)? = null,
        cancelBtnName: String? = "取消",
        cancelBlock: ((Dialog) -> Unit)? = null
    ) {
        QMUIDialog.MessageDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .addAction(
                0,
                okBtnName,
                QMUIDialogAction.ACTION_PROP_POSITIVE
            ) { qmuiDialog: QMUIDialog, i: Int ->
                if (okBlock == null) {
                    qmuiDialog.dismiss()
                    return@addAction
                }
                okBlock(qmuiDialog)
            }
            .addAction(
                cancelBtnName
            ) { qmuiDialog: QMUIDialog, i: Int ->
                if (cancelBlock == null) {
                    qmuiDialog.dismiss()
                    return@addAction
                }
                cancelBlock(qmuiDialog)
            }.setCancelable(false)
            .show()
    }
}