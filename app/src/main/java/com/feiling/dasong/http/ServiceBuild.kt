package com.feiling.dasong.http

import com.feiling.dasong.http.service.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/13
 * @author ql
 */
object ServiceBuild {
    /**
     * 用户相关接口
     */
    var userService: UserService = BaseApi.createRetrofit().create(UserService::class.java)
    /**
     * 工序相关接口
     */
    var processService: ProcessService = BaseApi.createRetrofit().create(ProcessService::class.java)
    /**
     * 公用的接口
     */

    var commService: CommService = BaseApi.createRetrofit().create(CommService::class.java)
    /**
     * 设备查询接口
     */
    var deviceService: DeviceService = BaseApi.createRetrofit().create(DeviceService::class.java)

    /**
     * 组相关操作
     */
    var groupService: GroupService = BaseApi.createRetrofit().create(GroupService::class.java)
    /**
     * 员工档案查询
     */
    var employeeService: EmployeeService =
        BaseApi.createRetrofit().create(EmployeeService::class.java)


    /**
     * 组相关操作
     */
    var outsourceService: OutsourceService =
        BaseApi.createRetrofit().create(OutsourceService::class.java)

    /**
     * 设备维修相关接口
     */
    var deviceRepairService: DeviceRepairService =
        BaseApi.createRetrofit().create(DeviceRepairService::class.java);

    /**
     * 设备点检相关接口
     */
    var deviceCheckService: DeviceCheckService =
        BaseApi.createRetrofit().create(DeviceCheckService::class.java);
    /**
     * 设备保养相关接口
     */
    var deviceMainService: DeviceMaintenanceService =
        BaseApi.createRetrofit().create(DeviceMaintenanceService::class.java);

    fun reset() {
        /**
         * 用户相关接口
         */
        userService = BaseApi.createRetrofit().create(UserService::class.java)
        /**
         * 工序相关接口
         */
        processService = BaseApi.createRetrofit().create(ProcessService::class.java)
        /**
         * 公用的接口
         */

        commService = BaseApi.createRetrofit().create(CommService::class.java)
        /**
         * 设备查询接口
         */
        deviceService = BaseApi.createRetrofit().create(DeviceService::class.java)

        /**
         * 组相关操作
         */
        groupService = BaseApi.createRetrofit().create(GroupService::class.java)


        /**
         * 组相关操作
         */
        outsourceService =
            BaseApi.createRetrofit().create(OutsourceService::class.java)

        /**
         * 员工档案查询
         */
        employeeService =
            BaseApi.createRetrofit().create(EmployeeService::class.java)

        deviceCheckService =
            BaseApi.createRetrofit().create(DeviceCheckService::class.java);

        deviceMainService =
            BaseApi.createRetrofit().create(DeviceMaintenanceService::class.java);
    }

}