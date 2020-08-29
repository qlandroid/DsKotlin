package com.feiling.dasong.ui.function.quality

import com.feiling.dasong.ui.function.model.NavData

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/27
 * @author ql
 */
interface IProcessQualityDetailsView {

    fun initDefault();

    /**
     * 初始化已经开始
     */
    fun initStarted();

    /**
     * 初始化暂停
     */
    fun initPause()

    /**
     * 初始化完成
     */
    fun initStop();

    /**
     * 已结束 初始化合格
     */
    fun initOk();

    /**
     * 已结束 初始化不合格
     */
    fun initFailed();

    /**
     * 已结束 初始化让步接收
     */
    fun initConcession()

    /**
     * 设置工序详情
     */
    fun setProcessDetails(model: NavData.ProcessViewModel)

    /**
     * 设置产品详情
     */
    fun setProductDetails(model: NavData.ProductViewModel)

    /**
     * 设置显示异常信息
     */
    fun setShowFailedMsg(failedMsg: String?)

    /**
     * 是否显示异常信息
     */
    fun setShowFailedView(visible: Boolean)
}