package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
data class LoginModel(var code: String?, var password: String?, var userId: String?) {
    constructor(ac: String, pw: String) : this(ac, pw, null)
}