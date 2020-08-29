package com.feiling.dasong.comm

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.widget.LabelTextView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/4
 * @author ql
 */
class CommViewHolder(view: View) : BaseViewHolder(view) {


    override fun setText(@IdRes viewId: Int, value: CharSequence?): CommViewHolder {
        getView<TextView>(viewId).text = value
        return this
    }

    override fun setText(@IdRes viewId: Int, @StringRes strId: Int): CommViewHolder? {
        getView<TextView>(viewId).setText(strId)
        return this
    }

    override fun setTextColor(@IdRes viewId: Int, @ColorInt color: Int): CommViewHolder {
        getView<TextView>(viewId).setTextColor(color)
        return this
    }

    override fun setTextColorRes(@IdRes viewId: Int, @ColorRes colorRes: Int): CommViewHolder {
        getView<TextView>(viewId).setTextColor(itemView.resources.getColor(colorRes))
        return this
    }

    override fun setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): CommViewHolder {
        getView<ImageView>(viewId).setImageResource(imageResId)
        return this
    }

    override fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?): CommViewHolder {
        getView<ImageView>(viewId).setImageDrawable(drawable)
        return this
    }

    override fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?): CommViewHolder {
        getView<ImageView>(viewId).setImageBitmap(bitmap)
        return this
    }

    override fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): CommViewHolder {
        getView<View>(viewId).setBackgroundColor(color)
        return this
    }

    override fun setBackgroundResource(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): CommViewHolder {
        getView<View>(viewId).setBackgroundResource(backgroundRes)
        return this
    }

    override fun setVisible(@IdRes viewId: Int, isVisible: Boolean): CommViewHolder {
        val view = getView<View>(viewId)
        view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        return this
    }

    override fun setGone(@IdRes viewId: Int, isGone: Boolean): CommViewHolder {
        val view = getView<View>(viewId)
        view.visibility = if (isGone) View.GONE else View.VISIBLE
        return this
    }

    override fun setEnabled(@IdRes viewId: Int, isEnabled: Boolean): CommViewHolder {
        getView<View>(viewId).isEnabled = isEnabled
        return this
    }


    open fun setAppText(@IdRes viewId: Int, value: CharSequence?): CommViewHolder {
        getView<AppCompatTextView>(viewId).text = value
        return this
    }

    open fun setAppText(@IdRes viewId: Int, @StringRes strId: Int): CommViewHolder? {
        getView<AppCompatTextView>(viewId).setText(strId)
        return this
    }

    open fun setAppTextColor(@IdRes viewId: Int, @ColorInt color: Int): CommViewHolder {
        getView<AppCompatTextView>(viewId).setTextColor(color)
        return this
    }

    open fun setAppTextColorRes(@IdRes viewId: Int, @ColorRes colorRes: Int): CommViewHolder {
        getView<AppCompatTextView>(viewId).setTextColor(itemView.resources.getColor(colorRes))
        return this
    }


    open fun setLabelViewText(@IdRes viewId: Int, value: CharSequence?): CommViewHolder {
        getView<LabelTextView>(viewId).textTv.text = value
        return this
    }

    open fun setLabelViewText(@IdRes viewId: Int, @StringRes strId: Int): CommViewHolder? {
        getView<LabelTextView>(viewId).textTv.setText(strId)
        return this
    }

    open fun setLabelViewTextColor(@IdRes viewId: Int, @ColorInt color: Int): CommViewHolder {
        getView<LabelTextView>(viewId).textTv.setTextColor(color)
        return this
    }

    open fun setLabelViewLabel(@IdRes viewId: Int, value: CharSequence?): CommViewHolder {
        getView<LabelTextView>(viewId).labelTv.text = value
        return this
    }

    open fun setLabelViewLabel(@IdRes viewId: Int, @StringRes strId: Int): CommViewHolder? {
        getView<LabelTextView>(viewId).labelTv.setText(strId)
        return this
    }

    open fun setLabelViewLabelColor(@IdRes viewId: Int, @ColorInt color: Int): CommViewHolder {
        getView<LabelTextView>(viewId).labelTv.setTextColor(color)
        return this
    }

    open fun setLabelView(
        @IdRes viewId: Int, labelTxt: CharSequence?,
        txt: String?
    ): CommViewHolder? {
        val lt = getView<LabelTextView>(viewId)
        lt.labelTv.text = labelTxt
        lt.textTv.text = txt
        return this
    }

    open fun setLabelView(@IdRes viewId: Int, @StringRes labelTxt: Int, txt: String?): CommViewHolder? {
        val lt = getView<LabelTextView>(viewId)
        lt.labelTv.setText(labelTxt)
        lt.textTv.text = txt
        return this
    }


}