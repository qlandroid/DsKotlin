package com.feiling.dasong.role

import com.feiling.dasong.model.UserModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/27
 * @author ql
 */
object RoleUtils {

    fun getRoleByType(userModel: UserModel?): IRole {
        return RootRole();
    }
}