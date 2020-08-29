package com.feiling.dasong.exception

import com.feiling.dasong.model.base.ResponseModel
import java.lang.RuntimeException

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/16
 * @author ql
 */
class DsException(var code: String? = "0", var msg: CharSequence) :

    RuntimeException(msg.toString()) {
    constructor(model: ResponseModel?) : this(model?.code.toString(), model?.message.orEmpty())

    constructor(msg: CharSequence) : this(null, msg) {

    }
}