package com.feiling.dasong.ui.function.outsource

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
class NavDetailsOsAdapter :
    BaseQuickAdapter<NavDetailsModel, CommViewHolder>(R.layout.item_os), LoadMoreModule {
    override fun convert(helper: CommViewHolder, item: NavDetailsModel) {
        val navDetailsView = helper.getView<NavDetailsView>(R.id.itemOsNdv);
        navDetailsView.details = item;
        helper.setText(R.id.itemOsIndexTv, "第${helper.adapterPosition + 1}个")

    }
}