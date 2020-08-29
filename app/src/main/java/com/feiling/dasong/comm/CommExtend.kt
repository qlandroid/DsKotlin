package com.feiling.dasong.comm

import com.feiling.dasong.C
import com.feiling.dasong.model.base.ResponseModel
import com.feiling.dasong.uitils.CheckUtils
import com.feiling.dasong.uitils.ExceptionUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.ql.comm.utils.JsonUtils
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
fun Any.toBody(): RequestBody = run {
    var toJson = JsonUtils.toJson(this)
    return RequestBody.create(MediaType.parse("application/json"), toJson);
}

fun Any.toStringMap(): MutableMap<String, String> = run {
    val toJson = C.sGson.toJson(this)
    return C.sGson.fromJson<MutableMap<String, String>>(toJson, MutableMap::class.java)
}

fun String.isNumber(): Boolean = run {
    return CheckUtils.isNumber(this)
}

fun Any.toJsonString(): String = run {
    return JsonUtils.toJson(this)
}
//重点
inline fun <reified T> String.toBeanList(): List<T> = Gson().fromJson<List<T>>(this, ParameterizedTypeImpl(T::class.java))



fun Throwable.getDsMsg(): CharSequence = run {
    return ExceptionUtils.getExceptionMessage(this)
}

fun Observable<ResponseModel>.response(): Observable<ResponseModel> = run {
    return RxHelper.takeRequestData(this)
}