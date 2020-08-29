package com.feiling.dasong.http.service

import com.feiling.dasong.model.base.ResponseModel
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 描述：报工相关接口
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/15
 * @author ql
 */
interface ProcessService {

    /**
     * 获得加工工序的设备列表
     */
    @GET("opProcess/getBprocessresoursList")
    fun getDevListByPCode(@Query("pmcode") pmcode: String): Observable<ResponseModel>

    /**
     * 工序分页查询
     */
    @GET("opProcess/getPageList")
    fun loadTask(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("opState") opState: String? = null
    ): Observable<ResponseModel>

    /**
     * 加载工序详情
     */
    @GET("opProcess/getById")
    fun processDetailsById(@Query("code") code: String): Observable<ResponseModel>

    /**
     * 开工
     */
    @GET("opProcess/kaigong")
    fun startProcess(
        @Query("code") code: String,
        @Query("actualCode") actualCode: String?
    ): Observable<ResponseModel>

    /**
     * 完工
     */
    @GET("opProcess/wangong")
    fun endProcess(@Query("code") code: String): Observable<ResponseModel>

    /**
     * 取消报工操作
     */
    @GET("opProcess/quxiaobg")
    fun actionProcessCancel(@Query("code") code: String): Observable<ResponseModel>

    /**
     * 下达
     * code         工序编码
     * actualCode   实际使用设备编码
     * opType       操作类型    0-人，1-组
     * employeeCode 如果是员工，必填员工编组
     * groupCode    如果是组，组编码必填
     */
    @GET("opProcess/xiada")
    fun sendProcess(@QueryMap() params: MutableMap<String, String>): Observable<ResponseModel>

    /**
     * 暂停
     */
    @GET("opProcess/zanting")
    fun pauseProcess(@Query("code") code: String): Observable<ResponseModel>


    /**
     * 质检
     */
    @GET("qualityProcess/getPageList")
    fun qualityProcessPageList(
        @Query("pageNo") pageNo: Int, @Query("pageSize") pageSize: Int, @Query(
            "opState"
        ) opState: String? = null
    ): Observable<ResponseModel>


    /**
     * 质检置顶操作
     */
    @POST("qualityProcess/StickOpProcess")
    fun actionQualityProcessMoveTop(@Body requestBody: RequestBody): Observable<ResponseModel>


    /**
     * 质检合格
     */
    @GET("qualityProcess/qualified")
    fun qualityProcessOk(@Query("code") code: String): Observable<ResponseModel>

    /**
     * 质检不合格
     */
    @GET("qualityProcess/noqualified")
    fun qualityProcessFailed(
        @Query("code") code: String,
        @Query("erroinfo") info: String? = null
    ): Observable<ResponseModel>

    /**
     * 质检让步接收
     */
    @GET("qualityProcess/degrade")
    fun actionQualityConcession(
        @Query("code") code: String,
        @Query("stan8") info: String? = null
    ): Observable<ResponseModel>


    /**
     * 获得质检详情
     */
    @GET("qualityProcess/getById")
    fun qualityProcessGetById(@Query("code") code: String): Observable<ResponseModel>

    /**
     * 质检开工
     */
    @GET("qualityProcess/kaigong")
    fun qualityProcessStart(@Query("code") code: String): Observable<ResponseModel>

    /**
     * 质检暂停
     */
    @GET("qualityProcess/zanting")
    fun qualityProcessPause(@Query("code") code: String): Observable<ResponseModel>


    /**
     * 工序查询操作记录
     */
    @GET("opProcessRecode/getOPPRList")
    fun processRecord(@Query("precessCode") code: String?): Observable<ResponseModel>

    /**
     * 工序工时统计
     */
    @GET("workHourSta/getByProcessCcode")
    fun processStatistics(@Query("processCcode") code: String?): Observable<ResponseModel>


    /**
     * 查询员工的工时记录
     */
    @GET("workHourSta/getGSJLList")
    fun getEmployeeWorkingRecode(
        @Query("userId") code: String, @Query("satrtDate") startDate: String?
        , @Query("closeDate") endDate: String?
    ): Observable<ResponseModel>


    /**
     * 查询员工列表，相应的统计工时
     */
    @GET("workHourSta/getGSJLListByDepCode")
    fun getEmployeeWorkingList(
        @QueryMap params: MutableMap<String, String> = mutableMapOf()
    ): Observable<ResponseModel>

    /**
     * 通过工时统计表的唯一编码查询 某一条的工序的工时
     */
    @GET("workHourSta/getById")
    fun getEmployeeWorkingHourFormProcess(
        @Query("code") code: String
    ): Observable<ResponseModel>

    /**
     * 工段长审核工序 工时
     */
    @POST("workHourSta/RYGSSH")
    fun actionWorkingHourFormProcess(
        @Query("code") code: String,
        @Query("repairTimer") repairTimer: Int
    ): Observable<ResponseModel>


    /**
     * 所有工时列表
     */
    @GET("workHourSta/getPageList")
    fun getWorkHourStaPage(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @QueryMap map: MutableMap<String, String>? = mutableMapOf()
    ): Observable<ResponseModel>

    /**
     * 工序交接
     * @param code 工序编码
     * @param opType 操作类型 0-人员 employeeCode必填，1-组 groupCode 必填
     * @param employeeCode 人员编号
     * @param groupCode 组编号
     */
    @GET("opProcess/connect")
    fun actionHandover(
        @Query("code") code: String?,
        @Query("opType") opType: String?,
        @Query("employeeCode") employeeCode: String? = null,
        @Query("groupCode") groupCode: String? = null
    ): Observable<ResponseModel>

    /**
     * 获得下一道工序详情
     * @param code 当前工序编码
     */
    @GET("opProcess/getBpordertByCcode")
    fun getNextProcessDetails(
        @Query("code") code: String
    ): Observable<ResponseModel>

    /**
     * 获得全部工序
     * @param code 当前工序编码
     */
    @GET("opProcess/getBpordertListByCcode")
    fun getProcessAll(
        @Query("code") code: String
    ): Observable<ResponseModel>

    /**
     * 根据工序状态 获得不同状态的的工序，分组
     * @param opState 操作状态
     */
    @GET("opProcess/getOpProcessVo2List")
    fun getProcessGroupList(
        @Query("opState") opState: String? = null
    ): Observable<ResponseModel>

    /**
     * 查询负责指定工序下的人或组
     *
     * @param pmcode 工序的编码
     */
    @GET("opProcess/getDevicetestu8ListByPmcode")
    fun getMemberListFromProcessDev(
        @Query("pmcode") pmcode: String? = null
    ): Observable<ResponseModel>

    /**
     * 工序置顶操作
     */
    @POST("opProcess/StickOpProcess")
    fun actionMoveTop(@Body requestBody: RequestBody): Observable<ResponseModel>


}