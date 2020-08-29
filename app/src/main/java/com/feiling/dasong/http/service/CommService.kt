package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/16
 * @author ql
 */
interface CommService {

    @Multipart
    @POST("file/upload")
    fun uploadFile(
        @Query("mkno") mkno: String,
        @Part file: MultipartBody.Part
    ): Observable<ResponseModel>


    /**
     *  存货档案查询接口
     */
    @GET("binventory/getPageList")
    fun getInventoryPage(
        @Query("pageNo") page: Int,
        @Query("pageSize") pageSize: Int?,
        @QueryMap map: Map<String, String> = mutableMapOf()
    ): Observable<ResponseModel>

    /**
     *  存货档案查询接口
     */
    @GET("/binventory/getById")
    fun getInventoryGetById(
        @Query("code") code: String
    ): Observable<ResponseModel>
}