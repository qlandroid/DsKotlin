package com.feiling.dasong.uitils

import com.orhanobut.logger.Logger

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/13
 * @author ql
 */
object LogUtils {

    public fun d(msg: String,vararg arg:String) {
        Logger.d(msg,arg)
    }

    public fun d(obj:Any) {
        Logger.d(obj)
    }
}