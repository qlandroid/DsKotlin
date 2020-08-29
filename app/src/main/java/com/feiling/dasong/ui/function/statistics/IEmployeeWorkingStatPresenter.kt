package com.feiling.dasong.ui.function.statistics

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/16
 * @author ql
 */
interface IEmployeeWorkingStatPresenter {

    fun actionSelectStartDate();

    fun actionSelectEndDate();

    /**
     * 一键审核
     */
    fun actionAuditAll()

    /**
     * 点击审核一条信息
     */
    fun actionAuditToPosition(position: Int)

}