package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 描述：设备维修
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/30
 * @author ql
 */
interface DeviceRepairService {


    /**
     * 维修 新增
     */
    @POST("dev/save")
    fun actionInsert(@Body params: RequestBody): Observable<ResponseModel>

    /**
     * 维修审核通过
     */
    @GET("dev/RepairAudit")
    fun actionVerifyOk(
        @Query("devCode") devCode: String,
        @Query("dmPersonnel") dmPersonnel: String,
        @Query("stan4") type: String
    ): Observable<ResponseModel>


    /**
     * 维修失败操作
     */
    @GET("dev/RepairFailure")
    fun actionFailed(
        @Query("devCode") code: String?,
        @Query("postscript") postscript: String?
    ): Observable<ResponseModel>

    /**
     * 维修暂停操作
     */
    @GET("dev/RepairPause")
    fun actionPause(@Query("devCode") code: String?): Observable<ResponseModel>

    /**
     * 维修开工操作
     */
    @GET("dev/RepairStartWork")
    fun actionStart(@Query("devCode") code: String?): Observable<ResponseModel>

    /**
     * 维修成功操作
     */
    @GET("dev/RepairSucceed")
    fun actionOk(
        @Query("devCode") code: String?,
        @Query("postscript") postscript: String?
    ): Observable<ResponseModel>

    /**
     * 维修订单查询详情
     */
    @GET("dev/getById")
    fun getById(@Query("code") code: String?): Observable<ResponseModel>

    /**
     *  获得设备列表
     *  @param ceqcode  设备编号
     *  @param ceqname  设备名称
     */
    @GET("dev/getPageList")
    fun getPageList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap map: MutableMap<String, String>
    ): Observable<ResponseModel>

}