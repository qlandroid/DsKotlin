package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 描述：员工档案查询
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/16
 * @author ql
 */
interface EmployeeService {


    /**
     * 获得详情
     */
    @GET("employees/getById")
    fun loadEmployeeDetailsByCode(@Query("code") code: String): Observable<ResponseModel>

    //获得员工列表
    @GET("opProcess/getEmployees")
    fun getEmployees(): Observable<ResponseModel>

}