package com.feiling.dasong.ui.function.arrange.model

import androidx.annotation.ColorInt

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/3
 * @author ql
 */
data class TaskModel(
    var title: String,//工序名称
    var clientName: String,//客户名称
    var contract: String,//合同号
    var statusName: String,
    @ColorInt var statusColor: Int,
    var date: String?//创建时间
) {
    /**
     * 生产订单号
     */
    var productNo: CharSequence? = null

    /**
     * 是否显示置顶按钮
     */
    var moveTopVisible: Boolean = false
}