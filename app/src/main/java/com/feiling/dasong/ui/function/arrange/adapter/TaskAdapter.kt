package com.feiling.dasong.ui.function.arrange.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommViewHolder
import com.feiling.dasong.ui.function.arrange.model.TaskModel
import com.feiling.dasong.widget.StatusTagTextView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/3
 * @author ql
 */
class TaskAdapter : BaseQuickAdapter<TaskModel, CommViewHolder>,
    LoadMoreModule {

    constructor():super(R.layout.item_task){
        addChildClickViewIds(R.id.item_task_move_top_tv)

    }


    override fun convert(helper: CommViewHolder, item: TaskModel) {
        helper.getView<AppCompatTextView>(R.id.item_task_title_tv).text = item.title;
        helper.setLabelViewText(R.id.item_task_date_ltv, item.date)
        helper.setLabelViewText(R.id.item_task_product_no_ltv, item.productNo)
        val statusTagTv = helper.getView<StatusTagTextView>(R.id.item_task_status_tag)
        statusTagTv.text = item.statusName
        val color = item.statusColor
        statusTagTv.setBgColor(
            color
        )
        helper.setLabelViewText(R.id.item_task_client_name, item.clientName)
            .setLabelViewText(R.id.item_task_contract, item.contract)

        var moveTopView = helper.getView<View>(R.id.item_task_move_top_tv)
        var moveTopVisible = if (item.moveTopVisible) View.VISIBLE else View.GONE
        if (moveTopView.visibility != moveTopVisible) {
            moveTopView.visibility = moveTopVisible
        }

    }

}