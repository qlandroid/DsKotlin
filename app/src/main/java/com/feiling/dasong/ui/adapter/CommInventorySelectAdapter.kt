package com.feiling.dasong.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommViewHolder
import com.feiling.dasong.ui.model.InventorySelectModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/28
 * @author ql
 */
class CommInventorySelectAdapter :
    BaseQuickAdapter<InventorySelectModel, CommViewHolder>(R.layout.item_inventory_select) ,LoadMoreModule{


    override fun convert(helper: CommViewHolder, item: InventorySelectModel) {
        var selectedIv = helper.getView<ImageView>(R.id.item_inventory_select_iv)
        selectedIv.isSelected = item.selected

        helper.setLabelViewText(R.id.item_inventory_code_ltv, item.code.orEmpty())
            .setLabelViewText(R.id.item_inventory_name_ltv, item.name.orEmpty())
            .setLabelViewText(R.id.item_inventory_std_ltv, item.std.orEmpty())
    }

}