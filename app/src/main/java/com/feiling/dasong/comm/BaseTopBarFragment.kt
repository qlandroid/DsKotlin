package com.feiling.dasong.comm

import android.app.Dialog
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDialog
import com.feiling.dasong.R
import com.feiling.dasong.widget.CustomToast
import com.feiling.dasong.widget.EmptyView
import com.feiling.dasong.widget.ViewUtils
import com.qmuiteam.qmui.skin.QMUISkinHelper
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
abstract class BaseTopBarFragment : BaseFragment(), IViewToast, IEmptyView, IDialogTip {

    var mEmptyView: IEmptyView? = null;
    var mTopBar: QMUITopBarLayout? = null;

    override fun createView(): View {

        var rootGroup = ViewUtils.createRootView(context)

        var contentView = createContentView()
        contentView.fitsSystemWindows = true
        var moreTextColor =
            QMUISkinValueBuilder.acquire().background(R.attr.app_skin_common_background)
        QMUISkinHelper.setSkinValue(contentView, moreTextColor)

        var layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val typedValue = TypedValue()
        context!!.theme.resolveAttribute(R.attr.qmui_topbar_height, typedValue, true)
        layoutParams.topMargin = typedValue.getDimension(resources.displayMetrics).toInt()
        contentView.layoutParams = layoutParams;

        rootGroup.addView(contentView)

        var topbar = createTopBar()
        topbar?.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (topbar is QMUITopBarLayout) {
            mTopBar = topbar
        }
        topbar?.fitsSystemWindows = true
        rootGroup.addView(topbar)

        var emptyView = createEmptyView();
        if (emptyView != null) {
            if (emptyView is IEmptyView) {
                mEmptyView = emptyView;
            }
            QMUISkinHelper.setSkinValue(emptyView, moreTextColor)

            var emptyViewLayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            emptyViewLayoutParams.topMargin =
                typedValue.getDimension(resources.displayMetrics).toInt() + QMUIDisplayHelper.getStatusBarHeight(
                    context
                )
            emptyView.layoutParams = emptyViewLayoutParams;
            emptyView.fitsSystemWindows = true;
            rootGroup.addView(emptyView)
        }
        return rootGroup
    }

    open fun createTopBar(): View? {
        return QMUITopBarLayout(context)
    }

    open fun createEmptyView(): View? {
        return EmptyView(context)
    }

    abstract fun createContentView(): View

    override fun showToast(message: String?, duration: Int) {
        context?.let { CustomToast.show(it, message, duration) }
    }

    override fun showToast(msg: Int, duration: Int) {
        context?.let { CustomToast.show(it, getString(msg), duration) }
    }


    override fun showEmpty(title: CharSequence?, msg: CharSequence?, block: ((IEmptyView) -> Unit)?) {
        mEmptyView?.showEmpty(title, msg, block)
    }

    override fun showEmptyView(
        loading: Boolean,
        title: CharSequence?,
        msg: CharSequence?,
        buttonText: CharSequence?,
        block: ((View) -> Unit)?
    ) {
        mEmptyView?.showEmptyView(loading, title, msg, buttonText, block)
    }

    override fun showFailed(title: CharSequence?, msg: CharSequence?, block: ((EmptyView) -> Unit)?) {
        mEmptyView?.showFailed(title, msg, block)
    }

    override fun showTipLoading(msg: String?) {
        mDialogTipHelper.showLoading(msg.orEmpty())
    }

    override fun showTipFailed(
        hint: CharSequence?,
        timer: Long,
        block: ((AppCompatDialog?) -> Unit)?
    ) {
        mDialogTipHelper.showFailTip(hint, mTopBar, timer, block)
    }

    override fun showTipSuccess(hint: String?, timer: Long, block: ((AppCompatDialog?) -> Unit)?) {
        mDialogTipHelper.showSuccessTip(hint, mTopBar, timer, block)
    }

    override fun hideEmpty() {
        mEmptyView?.hideEmpty()
    }

    override fun cancelTipLoading() {
        mDialogTipHelper.dismissLoading()
    }

    override fun displayMsgDialog(msg: CharSequence?, title: String?) {
        mDialogTipHelper.displayMsgDialog(title, msg)
    }

    override fun displayAskMsgDialog(
        msg: CharSequence?,
        title: String?,
        okBtnName: String?,
        okBlock: ((Dialog) -> Unit)?,
        cancelBtnName: String?,
        cancelBlock: ((Dialog) -> Unit)?
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