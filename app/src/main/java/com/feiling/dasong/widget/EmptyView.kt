package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import butterknife.ButterKnife
import butterknife.Unbinder
import com.feiling.dasong.R
import com.feiling.dasong.comm.IEmptyView
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.view_empty.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/26
 * @author ql
 */
class EmptyView : FrameLayout, IEmptyView {
    lateinit var unbinder: Unbinder

    var isShowing: Boolean = false
        get() = (visibility == View.VISIBLE)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    @SuppressLint("WrongConstant")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_empty, this)
        unbinder = ButterKnife.bind(view)
        hideEmpty()
    }

    override fun showEmptyView(
        loading: Boolean,
        title: CharSequence?,
        msg: CharSequence?,
        buttonText: CharSequence?,
        block: ((View) -> Unit)?
    ) {
        visibility = View.VISIBLE
        empty_view_failed_iv.visibility = View.GONE
        empty_view_empty_iv.visibility = View.GONE
        if (loading) {
            empty_view_loading.visibility = View.VISIBLE;
        } else {
            empty_view_loading.visibility = View.GONE;
        }

        if (title == null) {
            empty_view_title.visibility = View.GONE;
        } else {
            empty_view_title.visibility = View.VISIBLE;
            empty_view_title.text = title;
        }

        if (msg == null) {
            empty_view_msg.visibility = View.GONE
        } else {
            empty_view_msg.text = msg;
            empty_view_msg.visibility = View.VISIBLE;
        }
        if (buttonText == null) {
            empty_view_button.visibility = View.GONE;
        } else {
            empty_view_button.visibility = View.VISIBLE;

            empty_view_button.text = buttonText
            empty_view_button.onClick {
                if (block != null) {
                    block(it)
                }
            }
        }

    }


    override fun hideEmpty() {
        this.visibility = View.GONE;
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unbinder.unbind();
    }


    override fun showEmpty(
        title: CharSequence?,
        msg: CharSequence?,
        block: ((IEmptyView) -> Unit)?
    ) {
        this.visibility = View.VISIBLE

        empty_view_failed_iv.visibility = View.GONE
        empty_view_empty_iv.visibility = View.VISIBLE;
        empty_view_button.visibility = View.GONE
        empty_view_loading.visibility = View.GONE;
        empty_view_title.visibility = View.VISIBLE;
        empty_view_msg.visibility = View.VISIBLE;
        empty_view_title.text = title;
        empty_view_msg.text = msg
        if (block != null) {
            this.onClick {
                block(this@EmptyView)
            }
        } else {
            this.setOnClickListener(null)
        }
    }

    override fun showFailed(
        title: CharSequence?,
        msg: CharSequence?,
        block: ((EmptyView) -> Unit)?
    ) {
        visibility  = View.VISIBLE
        empty_view_failed_iv.visibility = View.VISIBLE
        empty_view_empty_iv.visibility = View.GONE;
        empty_view_button.visibility = View.GONE
        empty_view_loading.visibility = View.GONE;
        empty_view_title.visibility = View.VISIBLE;
        empty_view_msg.visibility = View.VISIBLE;
        empty_view_title.text = title;
        empty_view_msg.text = msg;
        if (block != null) {
            this.onClick {
                block(this@EmptyView)
            }
        } else {
            this.setOnClickListener(null)
        }
    }

}