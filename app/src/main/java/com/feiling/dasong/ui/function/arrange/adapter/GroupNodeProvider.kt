package com.feiling.dasong.ui.function.arrange.adapter

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/22
 * @author ql
 */

class GroupNodeProvider : BaseNodeProvider() {

    var selectGroupListener: OnSelectGroupListener? = null;
    override val itemViewType: Int = GroupSelectAdapter.ITEM_GROUP;
    override val layoutId: Int = R.layout.item_group_slt;
    override fun convert(helper: BaseViewHolder, data: BaseNode) {
        if (data is GroupNode) {
            helper.getView<AppCompatTextView>(R.id.item_group_code).text = data.code
            helper.getView<AppCompatTextView>(R.id.item_group_name).text = data.name
            val view = helper.getView<View>(R.id.item_group_select_btn)
            view.tag = helper.adapterPosition
            if (!view.hasOnClickListeners()) {
                view.setOnClickListener {
                    selectGroupListener?.onClick(it.tag as Int)
                }
            }
        }
    }

    override fun convert(helper: BaseViewHolder, data: BaseNode, payloads: List<Any>) {
        super.convert(helper, data, payloads)
        for (payload in payloads) {
            if (payload is Int && payload == GroupSelectAdapter.EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                setArrowSpin(helper, data, true)
            }
        }
    }

    private fun setArrowSpin(
        helper: BaseViewHolder,
        data: BaseNode,
        isAnimate: Boolean
    ) {
        val entity: GroupNode = data as GroupNode
        val imageView = helper.getView<ImageView>(R.id.item_group_iv)
        if (entity.isExpanded) {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(90f)
                    .start()
            } else {
                imageView.rotation = 90f
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(0f)
                    .start()
            } else {
                imageView.rotation = 0f
            }
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter()!!.expandOrCollapse(
            position,
            true,
            true,
            GroupSelectAdapter.EXPAND_COLLAPSE_PAYLOAD
        )

    }

    interface OnSelectGroupListener {
        fun onClick(position: Int)
    }
}