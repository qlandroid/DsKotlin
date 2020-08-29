package com.feiling.dasong.ui.function.group

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/6
 * @author ql
 */
interface IGroupDetailsPresenter {

    fun onClickEditGroupName()

    fun onClickAddMember()

    fun onClickRemoveMember(position: Int?)
}