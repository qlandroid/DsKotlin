package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * 描述： 组相关操作
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/28
 * @author ql
 */
interface GroupService {

    /**
     * 分页查询
     */
    @GET("opGroup/getPageList")
    fun loadPage(@Query("pageNo") pageNo: Int? = 1, @Query("pageSize") pageSize: Int? = 20, @QueryMap map: MutableMap<String, String>? = mutableMapOf()): Observable<ResponseModel>


    /**
     * 获得详情
     */
    @GET("opGroup/getById")
    fun loadGroupDetails(@Query("code") code: String): Observable<ResponseModel>

    //获得指定组
    @GET("opGroup/getGroupById")
    fun getGroupById(@QueryMap map: MutableMap<String, String>): Observable<ResponseModel>

    //获得组以及组的成员
    @GET("opGroup/getOpGroupVo")
    fun getOpGroupAll(): Observable<ResponseModel>
}