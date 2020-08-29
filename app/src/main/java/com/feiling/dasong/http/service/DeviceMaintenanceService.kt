package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 描述： 设备的保养
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/15
 * @author ql
 */
interface DeviceMaintenanceService {


    /**
     * 保养单查询
     */
    @GET("keepint/getPageList")
    fun loadPage(@Query("pageNo") pageNo: Int, @Query("pageSize") pageSize: Int, @QueryMap map: MutableMap<String, String>): Observable<ResponseModel>

    /**
     * 查询详情
     */
    @GET("keepinm/getById")
    fun getById(@Query("did") did: Int):Observable<ResponseModel>

    /**
     * 提交
     */
    @POST("keepinm/submit")
    fun actionSubmit(@Body requestBody: RequestBody): Observable<ResponseModel>
}