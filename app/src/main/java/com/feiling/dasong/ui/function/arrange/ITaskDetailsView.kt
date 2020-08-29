package com.feiling.dasong.ui.function.arrange

import com.feiling.dasong.ui.function.model.NavData

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/8
 * @author ql
 */
interface ITaskDetailsView {

    fun initDefault()
    fun initPended();
    fun initStarted()
    fun initPaused()
    fun initEnded()

    fun setProcessDetails(model: NavData.ProcessViewModel)
    fun setProductDetails(model: NavData.ProductViewModel)
    fun setPlanDevDetails(model: NavData.PlanDevViewModel)
    fun setPlanEmployeeDetails(model: NavData.EmployeeViewModel)
    fun setOptDevDetails(model: NavData.DevViewModel)
    fun setOptEmployeeDetails(model: NavData.EmployeeViewModel)


    fun setSendBtnClickable(isClickable: Boolean);

}