package com.feiling.dasong.ui.function.arrange.adapter.process

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.uitils.LogUtils
import com.feiling.dasong.widget.NavDetailsView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/22
 * @author ql
 */

class ProcessFirstNodeProvider : BaseNodeProvider() {

    var selectGroupListener: OnSelectGroupListener? = null;
    override val itemViewType: Int = ProcessNodeAdapter.NODE_PROCESS_PARENT;
    override val layoutId: Int = R.layout.item_nav_details;
    override fun convert(helper: BaseViewHolder, data: BaseNode) {
        if (data is ProcessNode) {
            var ndView = helper.itemView as NavDetailsView
            ndView.title = data.name
            ndView.leftIcon = R.drawable.icon_process_next
            ndView.hideRightIcon = false;
        }
    }

    override fun convert(helper: BaseViewHolder, data: BaseNode, payloads: List<Any>) {
        super.convert(helper, data, payloads)
        for (payload in payloads) {
            if (payload is Int && payload == ProcessNodeAdapter.EXPAND_COLLAPSE_PAYLOAD) {
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
        val entity: ProcessNode = data as ProcessNode
        var navDetailsView = helper.itemView as NavDetailsView
        val imageView = navDetailsView.rightImageView!!

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
            ProcessNodeAdapter.EXPAND_COLLAPSE_PAYLOAD
        )

    }

    interface OnSelectGroupListener {
        fun onClick(position: Int)
    }
}