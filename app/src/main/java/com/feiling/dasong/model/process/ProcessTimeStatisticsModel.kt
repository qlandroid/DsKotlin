package com.feiling.dasong.model.process

/**
 * 描述：工序下人员 工时统计
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/25
 * @author ql
 */
class ProcessTimeStatisticsModel {
    /**
     * 人员工时统计编码
     **/
    var ccode: String? = null
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
     * 结束日期
     **/
    var closeDate: String? = null
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
    var createddate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
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
     * 操作人员类型，0-个人员工，1-组
     **/
    var gType: String? = null
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
     * 操作组名称
     **/
    var gruopName: String? = null
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
     * 备用字段
     **/
    var last: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 人的编码
     **/
    var memberCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 操作人姓名
     **/
    var memberName: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *报工详情
     **/
    var opProcessVo: ProcessModel? = null
    /**
     * 报工操作编码
     **/
    var processCcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 由管理人员输入的确定工时
     **/
    var repairTimer: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 开始日期
     **/
    var satrtDate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 系统统计人员的工时
     **/
    var srcTimer: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段
     **/
    var stan1: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段
     **/
    var stan10: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 工序审核标识 0-未审核 1-已审核
     **/
    var stan2: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段
     **/
    var stan3: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段
     **/
    var stan4: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段
     **/
    var stan5: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段
     **/
    var stan6: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用��段
     **/
    var stan7: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段
     **/
    var stan8: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段
     **/
    var stan9: String? = null
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
     * 审核人姓名
     **/
    var verifybyname: String? = null
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
     * 所属工序明细
     */
    var opProcess: ProcessModel? = null


    /**
     * 工时是否审核
     */
    var isAuditWorkingTimer: Boolean = false
        get() {
            return vstatus == "1"
        }

}