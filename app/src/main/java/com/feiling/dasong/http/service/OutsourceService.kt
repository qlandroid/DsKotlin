package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
interface OutsourceService {

    /**
     * 委外查询详情
     */
    @GET("boutsource/getById")
    fun outsourceDetailsById(@Query("code") code: String?): Observable<ResponseModel>


    /**
     * 委外出库，待出库列表
     */
    @GET("boutsource/getNotOutPageList")
    fun outsourceUnoutPageList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap map: MutableMap<String, String>? = mutableMapOf()
    ): Observable<ResponseModel>

    /**
     * 委外出库，已经出库列表
     */
    @GET("boutsource/getOutPageList")
    fun outsourceOutPageList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap map: MutableMap<String, String>? = mutableMapOf()
    ): Observable<ResponseModel>


    /**
     * 委外出库，待入库列表
     */
    @GET("boutsource/getNotInPageList")
    fun outsourceUninPageList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap map: MutableMap<String, String>? = mutableMapOf()
    ): Observable<ResponseModel>

    /**
     * 委外出库，已经入库库列表
     */
    @GET("boutsource/getInPageList")
    fun outsourceInPageList(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap map: MutableMap<String, String> = mutableMapOf()
    ): Observable<ResponseModel>


    /**
     * 委外出库 操作
     */
    @POST("boutsource/getOut")
    fun outsourceOutAction(@Body params: RequestBody): Observable<ResponseModel>

    /**
     * 委外入库 操作
     */
    @POST("boutsource/getIn")
    fun outsourceInAction(@Body params: RequestBody): Observable<ResponseModel>
}