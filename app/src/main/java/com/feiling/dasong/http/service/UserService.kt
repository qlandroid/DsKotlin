package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.POST

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
interface UserService {

    @POST("Users/getLogin")
    fun login(@Body params: RequestBody): @JvmSuppressWildcards Observable<ResponseModel>

    @POST("changePw")
    fun changePassword(@FieldMap params: Map<String, @JvmSuppressWildcards Any>): @JvmSuppressWildcards Observable<ResponseModel>


}