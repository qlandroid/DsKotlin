package com.feiling.dasong.ui.function.arrange.adapter

import androidx.appcompat.widget.AppCompatTextView
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

class ChildrenNodeProvider : BaseNodeProvider() {
    override val itemViewType: Int = GroupSelectAdapter.ITEM_CHILDREN;
    override val layoutId: Int = R.layout.item_group_children;
    override fun convert(helper: BaseViewHolder, data: BaseNode) {
        if (data is ChildrenNode) {
            helper.getView<AppCompatTextView>(R.id.item_group_c_code).text = data.code;
            helper.getView<AppCompatTextView>(R.id.item_group_c_name).text = data.name;
        }
    }
}