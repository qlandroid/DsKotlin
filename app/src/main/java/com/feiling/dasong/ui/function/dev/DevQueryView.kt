package com.feiling.dasong.ui.function.dev

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import butterknife.ButterKnife
import com.feiling.dasong.R
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.dev_query.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/18
 * @author ql
 */
class DevQueryView : LinearLayout {

    var onDevQueryListener: OnDevQueryListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        orientation = LinearLayout.VERTICAL
        val inflate = LayoutInflater.from(context).inflate(R.layout.dev_query, this)

        ButterKnife.bind(this, inflate)
        this.isClickable = true
        this.isFocusable = true
        devQueryActionBtn.onClick {

            onDevQueryListener?.onQuery(
                DevQuery(
                    devQueryCodeEt.text.toString(),
                    devQueryNameEt.text.toString()
                )
            )
        }
        devQueryAllActionBtn.onClick {
            onDevQueryListener?.onQuery(
                DevQuery(
                    null,
                    null
                )
            )
            clear()
        }
    }

    fun clear() {
        devQueryCodeEt.setText("")
        devQueryNameEt.setText("")
    }


    interface OnDevQueryListener {
        fun onQuery(query: DevQuery)
    }

    data class DevQuery(var devCode: String?, var devName: String?)
}