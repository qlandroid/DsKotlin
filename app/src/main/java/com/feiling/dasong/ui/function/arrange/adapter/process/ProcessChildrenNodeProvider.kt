package com.feiling.dasong.ui.function.arrange.adapter.process

import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.uitils.LogUtils
import com.feiling.dasong.widget.LabelTextView
import com.feiling.dasong.widget.StatusTagTextView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/22
 * @author ql
 */

class ProcessChildrenNodeProvider : BaseNodeProvider() {
    override val itemViewType: Int = ProcessNodeAdapter.NODE_PROCESS_CHILDREN;
    override val layoutId: Int = R.layout.item_task_children;

    var childrenClickBlock: ((BaseNode) -> Unit)? = null

    override fun convert(helper: BaseViewHolder, data: BaseNode) {
        if (data is ProcessChildrenNode) {
            val item = data as ProcessChildrenNode;
            helper.getView<AppCompatTextView>(R.id.item_task_title_tv).text = "";

            item.clientName?.let {
                helper.getView<AppCompatTextView>(R.id.item_task_title_tv).text = it;
            }
            item.date?.let {
                helper.getView<LabelTextView>(R.id.item_task_date_ltv).text = it;
            }
            item.productNo?.let {
                helper.getView<LabelTextView>(R.id.item_task_product_no_ltv).text = it;
            }
            item.contract?.let {
                helper.getView<LabelTextView>(R.id.item_task_contract).text = it;
            }
            val statusTagTv = helper.getView<StatusTagTextView>(R.id.item_task_status_tag)
            item.statusName?.let {
                statusTagTv.text = item.statusName
            }
            item.statusColor?.let {
                statusTagTv.setBgColor(it)
            }
        }
        helper.itemView.setOnClickListener {
            childrenClickBlock?.let {
                it(data)
            }
        }
    }


}