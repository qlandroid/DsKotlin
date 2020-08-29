package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 描述：设备点检
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/30
 * @author ql
 */
interface DeviceCheckService {

    /**
     * 点检 新增
     */
    @GET("checkSheet/orderGenerated")
    fun actionInsert(@Query("ceqCode") devCode: String): Observable<ResponseModel>

    /**
     * 点检 提交
     */
    @POST("checkSheetProject/submint")
    fun actionSubmit(@Body body: RequestBody): Observable<ResponseModel>

    /**
     * 点检订单查询详情
     */
    @GET("checkSheet/getById")
    fun getById(@Query("code") code: String?): Observable<ResponseModel>

    /**
     *  获得设备列表
     *  @param ceqcode  设备编号
     *  @param ceqname  设备名称
     */
    @GET("checkSheet/getPageList")
    fun getPageList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap map: MutableMap<String, String>
    ): Observable<ResponseModel>

}