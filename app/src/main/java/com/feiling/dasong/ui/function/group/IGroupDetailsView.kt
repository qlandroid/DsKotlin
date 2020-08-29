package com.feiling.dasong.ui.function.group

import com.feiling.dasong.model.GroupMemberModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/6
 * @author ql
 */
interface IGroupDetailsView {

    /**
     * 设置组的名称
     */
    fun setGroupName(name: String?)

    /**
     * 设置组的编号
     */
    fun setGroupCode(code: String?)

    /**
     * 组的成员
     */
    fun setGroupMember(memberList: MutableList<GroupMemberModel>)

    fun getGroupName():String?
    fun getGroupCode():String?
}