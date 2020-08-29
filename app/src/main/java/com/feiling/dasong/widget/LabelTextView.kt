package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.feiling.dasong.R
import com.feiling.dasong.ui.model.LabelTextModel
import com.qmuiteam.qmui.util.QMUIDisplayHelper

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/3
 * @author ql
 */
class LabelTextView : LinearLayoutCompat {
    open lateinit var labelTv: AppCompatTextView
    open lateinit var textTv: AppCompatTextView

    var text: CharSequence? = null
        set(value) {
            field = value
            textTv.text = value
        }
    var label: CharSequence? = null
        set(value) {
            field = value
            labelTv.text = value
        }


    var labelTextModel: LabelTextModel? = null
        set(value) {
            field = value;
            if (value == null) {
                return;
            }
            textTv.text = value.txt
            labelTv.text = value.label
            textTv.gravity = value.contentPosition
            value.textColor?.let {
                textTv.setTextColor(it)
            }
            value.labelColor?.let {
                labelTv.setTextColor(it)
            }
            if (value.maxLine == null) {
                textTv.maxLines = 1;
            } else {
                textTv.maxLines = value.maxLine!!;
            }
        }


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
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.LabelTextView, defStyleAttr, 0
        )

        val labelTxt = a.getString(R.styleable.LabelTextView_label)
        val textTxt = a.getString(R.styleable.LabelTextView_text)
        val textMarginLef = a.getDimensionPixelSize(
            R.styleable.LabelTextView_textMarginLeft,
            QMUIDisplayHelper.dp2px(context, 10)
        );
        val labelSize = a.getDimensionPixelSize(
            R.styleable.LabelTextView_labelSize,
            QMUIDisplayHelper.sp2px(context, 14)
        )

        val labelColor = a.getColor(
            R.styleable.LabelTextView_labelColor,
            ContextCompat.getColor(context, R.color.label_color)
        )
        val labelPosition = a.getInt(R.styleable.LabelTextView_labelPositions, 0);
        val textColor = a.getColor(
            R.styleable.LabelTextView_textColor,
            ContextCompat.getColor(context, R.color.text_color)
        )
        val textSize = a.getDimensionPixelSize(
            R.styleable.LabelTextView_textSize,
            QMUIDisplayHelper.sp2px(context, 14)
        )
        var labelWidth = a.getDimensionPixelSize(R.styleable.LabelTextView_labelWidth, -1);
        val textContentPosition = a.getInt(R.styleable.LabelTextView_textContentPosition, 0);
        a.recycle()

        if (labelPosition == 0) {
            orientation = LinearLayoutCompat.HORIZONTAL
        }


        labelTv = AppCompatTextView(context)
        textTv = AppCompatTextView(context)

        if (labelWidth == -1) {
            labelWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        val labelLayoutParams = LinearLayoutCompat.LayoutParams(
            labelWidth.toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val textLayoutParams = LinearLayoutCompat.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textLayoutParams.weight = 1f
        addView(labelTv, labelLayoutParams)
        textLayoutParams.leftMargin = textMarginLef.toInt()
        addView(textTv, textLayoutParams)

        labelTv.setTextColor(labelColor)
        labelTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelSize.toFloat())
        textTv.setTextColor(textColor)
        textTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())

        when (textContentPosition) {
            0 -> textTv.gravity = Gravity.LEFT
            2 -> textTv.gravity = Gravity.CENTER
            3 -> textTv.gravity = Gravity.RIGHT
            else -> {
                textTv.gravity = Gravity.LEFT
            }
        }
        textTv.text = textTxt
        labelTv.text = labelTxt

        textTv.ellipsize = TextUtils.TruncateAt.END
    }

}