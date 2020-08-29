package com.feiling.dasong.comm

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/7
 * @author ql
 */
class ParameterizedTypeImpl(val clz: Class<*>) : ParameterizedType {
    override fun getRawType(): Type = List::class.java

    override fun getOwnerType(): Type? = null

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf(clz)
    }
}