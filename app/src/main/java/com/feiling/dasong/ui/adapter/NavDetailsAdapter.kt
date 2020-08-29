package com.feiling.dasong.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommViewHolder
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.widget.NavDetailsView
import kotlinx.android.synthetic.main.view_nav_details.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class NavDetailsAdapter :
    BaseQuickAdapter<NavDetailsModel, CommViewHolder>(R.layout.item_nav_details), LoadMoreModule {
    override fun convert(helper: CommViewHolder, item: NavDetailsModel) {
        val navDetailsView = helper.itemView as NavDetailsView
        navDetailsView.details = item;
    }
}