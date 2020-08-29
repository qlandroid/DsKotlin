package com.feiling.dasong.ui.function.statistics

import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/16
 * @author ql
 */
interface IEmployeeWorkingStatView {


    fun setUserDetails(list: MutableList<LabelTextModel>)

    /**
     * 工序列表
     */
    fun setProcessList(list: MutableList<NavDetailsModel>)


    /**
     * 设置总工时
     */
    fun setTotalWorkingHour(totalWorkingTimer: Double)

    fun setStartDate(startDate: String)
    fun setEndDate(endDate: String)

    fun setShowAudit(isShow:Boolean)

}