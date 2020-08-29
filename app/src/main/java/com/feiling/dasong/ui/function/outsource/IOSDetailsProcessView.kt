package com.feiling.dasong.ui.function.outsource

import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
interface IOSDetailsProcessView {


    /**
     * 初始化 为 待出库
     */
    fun initUnout()

    /**
     * 初始化 已经出库
     */
    fun initOutEnd()

    /**
     * 正在入库
     */
    fun initIning()


    /**
     * 设置供应商信息
     */
    fun setSupplierDetails(supp: MutableList<LabelTextModel>?)

    /**
     * 设置当前订单中的商品
     */
    fun setProcessList(list: MutableList<NavDetailsModel>?)

    fun displayProductInOptionsPopupWindow(
        details: NavDetailsModel?,
        actionIn: () -> Unit
    )

    fun displayProductDetails(details: NavDetailsModel?)



}