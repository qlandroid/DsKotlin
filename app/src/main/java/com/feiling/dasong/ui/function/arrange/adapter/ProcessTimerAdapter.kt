package com.feiling.dasong.ui.function.arrange.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommViewHolder
import com.feiling.dasong.ui.function.arrange.ItemTimeStatisView
import com.feiling.dasong.ui.function.arrange.model.ProcessTimerItem

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/25
 * @author ql
 */
class ProcessTimerAdapter :
    BaseQuickAdapter<ProcessTimerItem, CommViewHolder>(R.layout.item_process_empl_timer) {
    override fun convert(helper: CommViewHolder, item: ProcessTimerItem) {
        val itemTimeStatisView = helper.itemView as ItemTimeStatisView
        itemTimeStatisView.code = item.code
        itemTimeStatisView.name = item.name
        itemTimeStatisView.planTimer = item.planTimer
        itemTimeStatisView.nowTimer = item.nowTimer
    }
}