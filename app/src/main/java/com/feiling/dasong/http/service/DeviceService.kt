package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * 描述： 设备相关的接口
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/21
 * @author ql
 */
interface DeviceService {

    /**
     * 获得设备详情
     */
    @GET("devicetestu8/getById")
    fun getById(@Query("code") code: String?): Observable<ResponseModel>

    /**
     *  获得设备列表
     *  @param ceqcode  设备编号
     *  @param ceqname  设备名称
     */
    @GET("devicetestu8/getPageList")
    fun getPageList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("ceqname") ceqname: String? = null,
        @Query("ceqcode") ceqcode: String? = null,
        @QueryMap map: MutableMap<String, String>? = mutableMapOf()
    ): Observable<ResponseModel>


}