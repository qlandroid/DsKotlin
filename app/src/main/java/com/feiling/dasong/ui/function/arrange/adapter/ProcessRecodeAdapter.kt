package com.feiling.dasong.ui.function.arrange.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommViewHolder
import com.feiling.dasong.ui.function.arrange.model.ProcessRecodeModel
import com.feiling.dasong.widget.StatusTagTextView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/20
 * @author ql
 */
class ProcessRecodeAdapter :
    BaseQuickAdapter<ProcessRecodeModel, CommViewHolder>(R.layout.item_process_recode),LoadMoreModule {
    override fun convert(helper: CommViewHolder, item: ProcessRecodeModel) {

        var tagView = helper.getView<StatusTagTextView>(R.id.itemProcessRecodeTag)
        tagView.text = item.status?.tagName
        tagView.setBgColor(ContextCompat.getColor(tagView.context, item.status!!.color))
        helper.setText(R.id.itemProcessRecodeDateTv, item.date.orEmpty())
            .setText(R.id.itemProcessRecodeContentTv, item.content)

    }
}