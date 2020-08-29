package com.feiling.dasong.comm

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
interface IView : IDialogTip, IViewToast, IController, IEmptyView {

    /**
     * 关闭当前页面
     */
    fun shutDown() {}
}