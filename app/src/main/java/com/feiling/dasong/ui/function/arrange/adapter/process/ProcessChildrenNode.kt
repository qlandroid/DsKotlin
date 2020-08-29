package com.feiling.dasong.ui.function.arrange.adapter.process

import androidx.annotation.ColorInt
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/22
 * @author ql
 */
class ProcessChildrenNode : BaseNode() {


    var title: String? = null//工序名称
    var clientName: String? = null//客户名称
    var contract: String? = null//合同号
    var statusName: String? = null

    @ColorInt
    var statusColor: Int? = null
    var date: String? = null//创建时间

    /**
     * 生产订单号
     */
    var productNo: CharSequence? = null

    var key: Any? = null;


    override val childNode: MutableList<BaseNode>? = null
}