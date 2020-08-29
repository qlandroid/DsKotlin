package com.feiling.dasong.ui.function.outsource

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
interface IOSDetailsProcessPresenter {
    /**
     * 查看详情
     */
    fun onClickToProcessDetails(position: Int)

    /**
     * 点击了发货
     */
    fun onClickOutSend()
}