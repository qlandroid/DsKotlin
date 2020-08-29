package com.feiling.dasong.ui.function.arrange

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import butterknife.ButterKnife
import com.feiling.dasong.R
import kotlinx.android.synthetic.main.view_item_timer_stat.view.*

/**
 * 描述: 列表工时条目
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/25
 * @author ql
 */
class ItemTimeStatisView : LinearLayout {
    var code: CharSequence?
        set(value) {
            if (vItemTimerStatCodeTv.visibility != View.VISIBLE) {
                vItemTimerStatCodeTv.visibility = View.VISIBLE
            }
            vItemTimerStatCodeTv.text = value
        }
        get() = vItemTimerStatCodeTv.text

    var name: CharSequence?
        set(value) {
            vItemTimerStatNameTv.text = value
        }
        get() = vItemTimerStatNameTv.text
    var planTimer: CharSequence?
        set(value) {
            vItemTimerStatPlanTv.text = value
        }
        get() = vItemTimerStatPlanTv.text

    var nowTimer: CharSequence?
        set(value) {
            vItemTimerStatNowTv.text = value
        }
        get() = vItemTimerStatNowTv.text

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    @SuppressLint("WrongConstant")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        orientation = LinearLayout.HORIZONTAL
        val cView = LayoutInflater.from(context).inflate(R.layout.view_item_timer_stat, this)
        ButterKnife.bind(this, cView)
    }

}