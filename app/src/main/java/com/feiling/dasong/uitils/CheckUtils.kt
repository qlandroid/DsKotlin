package com.feiling.dasong.uitils

import java.util.regex.Pattern

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/18
 * @author ql
 */
object CheckUtils {


    fun isNumber(str: String): Boolean {
        var compile = Pattern.compile("[0-9]*")
        return compile.matcher(str).matches()
    }

}