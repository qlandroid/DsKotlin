package com.feiling.dasong.model.process

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/20
 * @author ql
 */
class OProcessRecodeModel {
    companion object {
        const val TYPE_EMPLOYEE = "0"
        const val TYPE_GROUP = "1"

        const val PROCESS_TYPE_CHECK = "2"
    }


    /**
     * 使用设备编码
     **/
    var ceqcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 设备名称
     **/
    var ceqname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 修改人
     **/
    var changedby: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 修改日期
     **/
    var changeddate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 合同号
     **/
    var contractnum: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 创建人
     **/
    var createdby: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 创建日期
     **/
    var createddate: Long? = null

    /**
     * 创建人-操作人员
     */
    var createdname: String? = null


    /**
     * 单据日期
     **/
    var ddate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 员工姓名
     **/
    var firstname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 组的编码
     **/
    var groupCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 组的名称
     **/
    var gruopName: String? = null
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
     * 员工编码
     **/
    var memberCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 报工操作记录编码
     **/
    var opPRCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 分配类型 0-人员，1-组
     **/
    var opType: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 报工操作编码
     **/
    var precessCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 操作类型0-派工1-开工2-暂停3-完工4-家人
     **/
    var type: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 审核人
     **/
    var verifyby: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 审核日期
     **/
    var verifydate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 审核状态
     **/
    var vstatus: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 质检类型 工序类型(1:加工,2:质检) ,
     */
    var stan1: String? = null;

    var isCheck: Boolean = false
        get() {
            return PROCESS_TYPE_CHECK == stan1
        }

}