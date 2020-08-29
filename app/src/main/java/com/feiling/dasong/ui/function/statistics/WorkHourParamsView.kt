package com.feiling.dasong.ui.function.statistics

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ScrollView
import butterknife.ButterKnife
import com.feiling.dasong.R

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/22
 * @author ql
 */
class WorkHourParamsView : ScrollView {
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
        var cView = LayoutInflater.from(context).inflate(R.layout.work_hour_parms, this)
        ButterKnife.bind(this, cView)
    }
}