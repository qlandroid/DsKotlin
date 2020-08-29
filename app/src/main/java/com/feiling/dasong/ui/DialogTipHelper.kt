package com.feiling.dasong.ui

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/15
 * @author ql
 */
class DialogTipHelper(var context: Context?) {
    private var loadingTip: QMUITipDialog? = null;
    private var successTip: QMUITipDialog? = null;

    private var failedTip: QMUITipDialog? = null;

    fun showFailTip(
        hint: CharSequence?,
        view: View? = null,
        timer: Long = 2_000,
        block: ((AppCompatDialog?) -> Unit)? = null
    ) {
        dismissFail()
        failedTip = QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
            .setTipWord(hint)
            .create()
        failedTip?.show()

        view?.postDelayed({
            if (block != null) {
                block(failedTip)
            } else {
                dismissFail()
            }
        }, timer)
    }

    fun displayMsgDialog(title: String? = "提示", msg: CharSequence?) {
        QMUIDialog.MessageDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .addAction("确定") { qmuiDialog: QMUIDialog, i: Int ->
                qmuiDialog.dismiss()
            }.show()
    }


    fun showSuccessTip(
        hint: String?,
        view: View? = null,
        timer: Long = 2_000,
        block: ((AppCompatDialog?) -> Unit)? = null
    ) {
        dismissSuccess()
        successTip = QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
            .setTipWord(hint)
            .create()
        successTip?.show()
        view?.postDelayed({
            if (block != null) {
                dismissSuccess()
                block(successTip)
            } else {
                dismissSuccess()
            }
        }, timer)
    }

    fun showLoading(hint: String = "") {
        dismissLoading()
        loadingTip = QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(hint)
            .create()
        loadingTip?.setCancelable(false)
        loadingTip?.show()
    }

    fun dismissLoading() {
        if (loadingTip != null && loadingTip!!.isShowing) {
            loadingTip!!.dismiss()
        }
    }

    fun dismissSuccess() {
        if (successTip != null && successTip!!.isShowing) {
            successTip!!.dismiss()
        }
    }

    fun dismissFail() {
        if (failedTip != null && failedTip!!.isShowing) {
            failedTip!!.dismiss()
        }
    }


}