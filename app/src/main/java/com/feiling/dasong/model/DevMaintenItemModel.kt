package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/15
 * @author ql
 */
class DevMaintenItemModel {
    /**
     * 建档人
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
     * 子子表
     **/
    var ddid: String? = null
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
     *子表id
     **/
    var did: Int? = null
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
     *主表id
     **/
    var id: Int? = null
    /**
     * 保养编码
     **/
    var keepcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 保养基础编码
     **/
    var upkeepcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 保养基础名称
     **/
    var upkeepname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 传值
     **/
    var value: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
}