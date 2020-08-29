package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.feiling.dasong.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/18
 * @author ql
 */
class LabelEditView : LinearLayout {

    private lateinit var mLabelTv: TextView
    private lateinit var mEditText: EditText
    var text: CharSequence
        get() = mEditText.text
        set(value) {
            mEditText.setText(value)
        }
    var label: CharSequence? = null
        set(value) {
            field = value
            mLabelTv.text = value
        }
    var hint: CharSequence
        get() = mEditText.hint
        set(value) {
            mEditText.hint = value
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
        initView(context)
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.LabelEditView, defStyleAttr, 0
        )

        val labelTxt = a.getString(R.styleable.LabelEditView_elabel)
        val textTxt = a.getString(R.styleable.LabelEditView_etext)
        val textMarginLef = a.getDimensionPixelSize(
            R.styleable.LabelEditView_etextMarginLeft,
            QMUIDisplayHelper.dp2px(context, 10)
        );
        val labelSize = a.getDimensionPixelSize(
            R.styleable.LabelEditView_elabelSize,
            QMUIDisplayHelper.sp2px(context, 14)
        )

        val labelColor = a.getColor(
            R.styleable.LabelEditView_elabelColor,
            ContextCompat.getColor(context, R.color.label_color)
        )
        val labelPosition = a.getInt(R.styleable.LabelEditView_elabelPositions, 0);
        val textColor = a.getColor(
            R.styleable.LabelEditView_etextColor,
            ContextCompat.getColor(context, R.color.text_color)
        )
        val textSize = a.getDimensionPixelSize(
            R.styleable.LabelEditView_etextSize,
            QMUIDisplayHelper.sp2px(context, 14)
        )
        var labelWidth = a.getDimensionPixelSize(R.styleable.LabelEditView_elabelWidth, -1);
        val textContentPosition = a.getInt(R.styleable.LabelEditView_etextContentPosition, 0);
        a.recycle()

        label = labelTxt
        text = textTxt
        if (labelWidth == -1) {
            labelWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        val labelLp = mLabelTv.layoutParams as LayoutParams
        labelLp.width = labelWidth
        mLabelTv.layoutParams = labelLp

        val editLp: LinearLayout.LayoutParams = mEditText.layoutParams as LayoutParams
        editLp.leftMargin = textMarginLef
        mEditText.layoutParams = editLp
        when (textContentPosition) {
            0 -> mEditText.gravity = Gravity.LEFT
            2 -> mEditText.gravity = Gravity.CENTER
            3 -> mEditText.gravity = Gravity.RIGHT
            else -> {
                mEditText.gravity = Gravity.RIGHT
            }
        }
        mLabelTv.setTextColor(labelColor)
        mLabelTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelSize.toFloat())
        mEditText.setTextColor(textColor)
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        if (labelPosition == 0) {
            orientation = LinearLayoutCompat.HORIZONTAL
        }


    }

    private fun initView(context: Context) {
        gravity = Gravity.CENTER_VERTICAL
        orientation = HORIZONTAL
        mLabelTv = TextView(context)
        mEditText = EditText(context)
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        mEditText.setBackgroundResource(R.drawable.custom_edittext_background)
        val dimensionPixelSize = context.resources.getDimensionPixelSize(R.dimen.margin_normal)
//        mEditText.setPadding(
//            dimensionPixelSize,
//            dimensionPixelSize,
//            dimensionPixelSize,
//            dimensionPixelSize
//        )
        val layoutParams = LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.weight = 1f;
        layoutParams.leftMargin = QMUIDisplayHelper.dp2px(context, 10)
        addView(
            mLabelTv,
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        )
        addView(mEditText, layoutParams)

        val left =
            context.resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        val top = context.resources.getDimensionPixelSize(R.dimen.margin_normal)

        setPadding(left, top, left, top)
    }
}