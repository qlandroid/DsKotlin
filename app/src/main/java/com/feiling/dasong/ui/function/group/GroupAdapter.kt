package com.feiling.dasong.ui.function.group

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.model.GroupModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/28
 * @author ql
 */
class GroupAdapter : BaseQuickAdapter<GroupModel, BaseViewHolder>(R.layout.item_group),
    LoadMoreModule {
    override fun convert(helper: BaseViewHolder, item: GroupModel) {
        helper.setText(R.id.item_group_name_tv, item.gruopName.orEmpty())
            .setText(R.id.item_group_code_tv, item.groupCode.orEmpty())

    }
}