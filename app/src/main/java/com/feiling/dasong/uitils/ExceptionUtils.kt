package com.feiling.dasong.uitils

import com.feiling.dasong.exception.DsException
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/26
 * @author ql
 */
object ExceptionUtils {
    fun getExceptionMessage(exception: Throwable): CharSequence {

        if (exception is SocketException) {
            return "连接服务器失败"
        }
        if (exception is SocketTimeoutException) {
            return "连接超时"
        }
        if (exception is DsException) {
            return exception.msg;
        }
        if (exception is RuntimeException) {
            return exception.message.orEmpty()
        }
        return "获取数据失败"
    }
}