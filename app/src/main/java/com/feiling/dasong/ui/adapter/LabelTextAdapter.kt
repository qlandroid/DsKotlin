package com.feiling.dasong.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommViewHolder
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.widget.LabelTextView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class LabelTextAdapter :
    BaseQuickAdapter<LabelTextModel, CommViewHolder>(R.layout.item_label_text), LoadMoreModule {
    override fun convert(helper: CommViewHolder, item: LabelTextModel) {
        var view = helper.getView<LabelTextView>(R.id.item_label_text)
        item.textColor?.let {
            view.textTv.setTextColor(it)
        }
        helper.setLabelView(R.id.item_label_text, item.label, item.txt)
    }
}