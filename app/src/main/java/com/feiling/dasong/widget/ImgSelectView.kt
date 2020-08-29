package com.feiling.dasong.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.decorator.MediaGridInset
import com.feiling.dasong.uitils.imgload.ImgLoadConfig

/**
 * 描述：用于图片选择
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/11
 * @author ql
 */
class ImgSelectView : RelativeLayout {
    companion object {
        const val TYPE_ADD = "add-img"
    }

    var maxCount: Int = 9;

    /**
     * 是否显示添加按钮
     */
    var showAdd = true;

    /**
     * 是否显示删除按钮
     */
    var showDel = true;

    private lateinit var imgRv: RecyclerView;
    private var mImgAdapter: ImgSelectAdapter = ImgSelectAdapter()

    private var pathList: MutableList<String> = mutableListOf();
    var onImgSelectListener: OnImgSelectListener? = null;

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
        imgRv = RecyclerView(context)
        imgRv.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        imgRv.layoutManager = object : GridLayoutManager(context, 3) {
            override fun canScrollVertically(): Boolean {
                return false;
            }
        }
        mImgAdapter.setNewData(pathList)
        imgRv.adapter = mImgAdapter;

        imgRv.addItemDecoration(MediaGridInset(3, 10, false))

        mImgAdapter.setOnItemClickListener { adapter, view, position ->
            var s = pathList[position]
            if (s == TYPE_ADD) {
                onImgSelectListener?.onClickAddImg();
                return@setOnItemClickListener
            }
            onImgSelectListener?.onClickImg(position)
        }
        mImgAdapter.setOnItemChildClickListener { adapter, view, position ->
            onImgSelectListener?.onClickRemove(position)
        }
        addView(imgRv)
        notifyDataSetChanged(null)
    }


    fun notifyDataSetChanged(list: List<String>?) {
        pathList.clear()
        list?.forEach {
            pathList.add(it)
        }
        if (list.orEmpty().size < maxCount) {
            if (showAdd)
                pathList.add(TYPE_ADD)
        }
        mImgAdapter.setNewData(pathList)
        mImgAdapter.notifyDataSetChanged()
    }


    inner class ImgSelectAdapter :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_select_img) {
        override fun convert(helper: BaseViewHolder, item: String) {
            var iv = helper.getView<ResizableImageView>(R.id.itemSelectImgIv)
            var removeView = helper.getView<View>(R.id.itemSelectImgRemoveIv)
            if (!iv.adjustViewBounds) {
                iv.adjustViewBounds = true;
            }
            val parm: ViewGroup.LayoutParams = helper.itemView.layoutParams
            if (parm.width != parm.height) {
                var gridLayoutManager = imgRv.layoutManager as GridLayoutManager
                parm.height =
                    gridLayoutManager.width / gridLayoutManager.spanCount - 2 * helper.itemView.paddingLeft - 2 * (parm as MarginLayoutParams).leftMargin
            }


            if (item == TYPE_ADD) {
                //显示add图片
                removeView.visibility = View.GONE
                iv.setImageResource(R.drawable.btn_add)
                iv.autoSize = false;
                return;
            }
            iv.autoSize = true;
            //控制删除按钮的显示
            if (showDel && removeView.visibility != View.VISIBLE) {
                removeView.visibility = View.VISIBLE
            } else if (!showDel && removeView.visibility != View.GONE) {
                removeView.visibility = View.GONE
            }
            addChildClickViewIds(R.id.itemSelectImgRemoveIv)
            ImgLoadConfig.Builder(iv.context)
                .load(item)
                .into(iv)
        }
    }

    interface OnImgSelectListener {
        fun onClickAddImg()
        fun onClickImg(position: Int)
        fun onClickRemove(position: Int)

    }
}