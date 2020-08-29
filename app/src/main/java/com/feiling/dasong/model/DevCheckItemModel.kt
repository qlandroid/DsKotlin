package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/10
 * @author ql
 */
class DevCheckItemModel {
    /**
     * 修改人
     **/
    var changeBy: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 修改日期
     **/
    var changeDate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 点检项编码
     **/
    var checkCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 点检项名称
     **/
    var checkName: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 操作单编码
     **/
    var checkSheetCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 创建人
     **/
    var createBy: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 创建日期
     **/
    var createDate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 单据编码
     **/
    var cspcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *ID
     **/
    var id: Int? = null
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
     * 标准值
     **/
    var standardValue: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

}