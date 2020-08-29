package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommViewHolder
import com.feiling.dasong.ui.adapter.LabelTextAdapter
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import kotlinx.android.synthetic.main.view_nav_details.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/4
 * @author ql
 */
class NavDetailsView : LinearLayout {

    var details: NavDetailsModel? = null
        set(value) {
            field = value

            nav_details_title.text = value?.title
            value?.hint?.let {
                hint = it
            }
            value?.hintColor?.let {
                hintColor = it
            }
            value?.subTitle?.let {
                subTitle = it
            }
            value?.leftIcon?.let {
                leftIcon = it
            }
            value?.children?.let { setDetailsData(it) }
            value?.showTag?.let {
                showTag = it
            }

            value?.tagBackgroundColor?.let {
                tagBackgroundColor = it
            }
            value?.tagText?.let {
                tagText = it;
            }
        }

    var hint: CharSequence? = null
        set(value) {
            field = value
            nav_details_hint.text = value
        }

    @ColorInt
    var hintColor: Int? = null
        set(value) {
            field = value
            field?.let { nav_details_hint.setTextColor(it) };
        }

    var title: String? = null
        set(value) {
            field = value
            nav_details_title.text = field.orEmpty()
        }
    var subTitle: String? = null
        set(value) {
            field = value
            if (nav_details_sub_title.visibility != View.VISIBLE) {
                nav_details_sub_title.visibility = View.VISIBLE
            }
            nav_details_sub_title.text = value.orEmpty()
        }

    var showTag: Boolean = false
        set(value) {
            field = value
            nav_details_left_tag.visibility = if (value) View.VISIBLE else View.GONE
        }
    var tagText: CharSequence
        set(value) {
            nav_details_left_tag.text = value
        }
        get() {
            return nav_details_left_tag.text
        }

    @ColorInt
    var tagBackgroundColor: Int? = null
        set(value) {
            if (value != null) {
                nav_details_left_tag.setBgColor(value)
            }
            field = value
        }

    @DrawableRes
    var leftIcon: Int? = null
        set(value) {
            field = value
            value?.let {
                nav_details_left_iv.visibility = View.VISIBLE
                nav_details_left_iv.setImageResource(it)
            }

        }

    @DrawableRes
    var rightIcon: Int? = null
        set(value) {
            field = value
            value?.let {
                nav_details_title_arrow_iv.setImageResource(it)
            }

        }
    var hideRightIcon: Boolean = false
        set(value) {
            field = value
            nav_details_title_arrow_iv.visibility = if (value) View.GONE else View.VISIBLE
        }

    var rightImageView: ImageView? = null
        get() = nav_details_title_arrow_iv

    private var mLabelTxtAdapter: LabelTextAdapter? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.style.StatusTag
    )

    @SuppressLint("WrongConstant")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {


        LayoutInflater.from(context).inflate(R.layout.view_nav_details, this, true)
        this.orientation = LinearLayout.VERTICAL
        ButterKnife.bind(this);


        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.NavDetailsView, defStyleAttr, 0
        )
        val title = a.getString(R.styleable.NavDetailsView_title)
        val titleSize = a.getDimensionPixelSize(
            R.styleable.NavDetailsView_titleSize,
            QMUIDisplayHelper.sp2px(context, 14)
        )
        var titleBackgroundRes = a.getResourceId(
            R.styleable.NavDetailsView_title_background,
            0
        )
        nav_details_title_group.setBackgroundResource(titleBackgroundRes)

        var childrenBg =
            a.getResourceId(R.styleable.NavDetailsView_children_background, 0)
        nav_details_children.setBackgroundResource(childrenBg)
        val titleColor = a.getColor(R.styleable.NavDetailsView_titleColor, Color.BLACK)
        val leftIcon = a.getResourceId(R.styleable.NavDetailsView_leftIcon, -1)
        val rightIcon =
            a.getResourceId(R.styleable.NavDetailsView_rightIcon, R.drawable.forward_arrow)
        val rightIconHide = a.getBoolean(R.styleable.NavDetailsView_rightIconHide, true)
        val navClickable = a.getBoolean(R.styleable.NavDetailsView_navClickable, false)
        val hint = a.getString(R.styleable.NavDetailsView_hint)
        a.recycle()



        this.title = title;
        nav_details_title.setTextColor(titleColor)
        nav_details_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())

        if (leftIcon != -1) {
            this.leftIcon = leftIcon;
        }
        nav_details_title_arrow_iv.visibility = if (rightIconHide) View.GONE else View.VISIBLE

        if (rightIcon != -1) {
            nav_details_title_arrow_iv.setImageResource(rightIcon)
        }
        nav_details_title_group.isClickable = navClickable
        nav_details_hint.text = hint;


    }

    fun setNavTitleClickListener(e: ((view: View) -> Unit)?) {
        if (e == null) {
            nav_details_title_group.setOnClickListener(null)
        } else {
            nav_details_title_group.setOnClickListener {
                e(it)
            }
        }

    }

    fun setDetailsData(list: MutableList<LabelTextModel>?) {
        if (mLabelTxtAdapter == null) {
            initRv()
        }
        mLabelTxtAdapter?.setNewData(list)
        mLabelTxtAdapter?.notifyDataSetChanged()
    }


    private fun initRv() {
        navDetailsChildrenRv.layoutManager = object : GridLayoutManager(context, 4) {
            override fun canScrollVertically(): Boolean {
                return false;
            }
        }
        mLabelTxtAdapter = LabelTextAdapter();
        mLabelTxtAdapter?.setGridSpanSizeLookup { gridLayoutManager, viewType, position ->
            var labelTextModel = mLabelTxtAdapter!!.data[position]
            return@setGridSpanSizeLookup labelTextModel.span;
        }
        navDetailsChildrenRv.adapter = mLabelTxtAdapter
    }

    private fun addDetailsData(model: LabelTextModel) {
        val label: LabelTextView = LabelTextView(context)
        label.labelTextModel = model;
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val paddinglef =
            context.resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin)
        val paddingtop = context.resources.getDimensionPixelOffset(R.dimen.margin_normal)
        label.setPadding(paddinglef, paddingtop, paddinglef, paddingtop)
        nav_details_children.addView(label, layoutParams)
    }


    open fun addRightButton(@IdRes id: Int, btnName: String): AppCompatTextView {
        val btn = AppCompatTextView(context)
        btn.setText(btnName)
        btn.id = id;
        btn.isClickable = true


        btn.setTextColor(ContextCompat.getColorStateList(context, R.color.s_btn_gray))
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        nav_details_btn_group.addView(btn, lp)

        return btn
    }


    inner class DetailsAdapter :
        BaseQuickAdapter<LabelTextModel, CommViewHolder>(R.layout.item_label_text) {
        override fun convert(helper: CommViewHolder, item: LabelTextModel) {
            helper.setLabelView(R.id.item_label_text, item.label, item.txt)
        }

    }

}