package com.feiling.dasong.model

/**
 * 描述：设备保养单
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/15
 * @author ql
 */
class DevMaintenOrderModel {
    /**
     * 保养日期
     **/
    var begintime: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 保养日期
     **/
    var beginTime: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 操作人
     **/
    var createdby: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 建档时间
     **/
    var createddate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 创建人
     **/
    var createname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 创建时间
     **/
    var createtime: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 单据时间
     **/
    var ddate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 描述
     **/
    var describe: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 设备编码
     **/
    var devicecode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 设备名称
     **/
    var devicename: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *子表id
     **/
    var did: Int? = null
    /**
     * 结束日期
     **/
    var endtime: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 开关(是,非)
     **/
    var flag: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *id
     **/
    var id: Int? = null
    /**
     * 保养单号
     **/
    var keepcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 类型(0,手动 1,自动)
     **/
    var leixing: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备注
     **/
    var remark: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 修改人
     **/
    var updatename: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 修改时间
     **/
    var updatetime: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 保养基础编码()
     **/
    var upkeepcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 周期
     **/
    var zhouqi: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }


    /**
     *  保养项
     */
    var list: MutableList<DevMaintenItemModel>? = null

}