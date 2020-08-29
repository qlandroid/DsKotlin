package com.feiling.dasong.model.base

import com.feiling.dasong.C
import com.feiling.dasong.comm.ParameterizedTypeImpl
import com.feiling.dasong.comm.toBeanList
import com.github.salomonbrys.kotson.gsonTypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.ql.comm.utils.JsonUtils

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/14
 * @author ql
 */
class ResponseModel {
    var code: Int? = -1;
    var data: JsonElement? = null
    var message: String? = null
        get() {
            return field.orEmpty()
        }

    val isSuccess: Boolean
        get() {
            return code == 0;
        }

    val isFailed: Boolean
        get() {
            return code != 0;
        }

    open fun <T> getData(clazz: Class<T>): T {
        return C.sGson.fromJson(data?.asJsonObject, clazz)
    }

    open fun getPageModel(): PageModel {
        val gson = C.sGson
        val pageModel = gson.fromJson<PageModel>(data?.asJsonObject, PageModel::class.java)
        return pageModel;
    }

    inline fun <reified T> getList(clazz: Class<*>): MutableList<T> {
        return Gson().fromJson<MutableList<T>>(data, ParameterizedTypeImpl(T::class.java))
    }


}


