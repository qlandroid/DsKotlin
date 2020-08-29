package com.feiling.dasong.ui.function.statistics

import com.feiling.dasong.ui.model.LabelTextModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/18
 * @author ql
 */
interface IStatProcessWorkingDetailsView {


    fun setProcessDetails(details: MutableList<LabelTextModel>)

    fun setUserDetails(details: MutableList<LabelTextModel>)

    /**
     * 设置计划工时
     *
     */
    fun setPlanWorkingTimer(timer: String);

    /**
     * 是否显示审核状态
     */
    fun setShowAudit(isShow: Boolean)

    /**
     * 设置审核状态
     * 0-未审核，1-已审核
     */
    fun setAuditStatus(auditStatue: String)

    /**
     * 设置用户审核的工时
     */
    fun setAuditWorkingTimer(auditTimer: String)
}