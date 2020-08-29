package com.feiling.dasong.role

import com.feiling.dasong.model.QDItemDescription

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/27
 * @author ql
 */
interface IRole {

    /**
     * 获得按钮权限
     */
    fun getMenus(): MutableList<QDItemDescription>;


    /**
     * 是否有权限
     */
    fun hasPermissions(actionType: Action): Boolean;
}