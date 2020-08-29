package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/22
 * @author ql
 */
class GroupMemberModel {
    var changedby: String? = null
        //修改人 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var changeddate: String? = null
        //修改日期 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var contractnum: String? = null
        //合同号 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var createdby: String? = null
        //创建人 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var createddate: String? = null
        //创建日期 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var ddate: String? = null
        //单据日期 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var employeeCode: String? = null
        //员工编码 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var firstname: String? = null
        //员工姓名 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var groupCode: String? = null
        //组的编码 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var id: Int? = null//ID
    var opMemberCode: String? = null
        //操作组-人 唯一标识编码 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var stan1: String? = null
        //备用字段 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var verifyby: String? = null
        //审核人 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var verifydate: String? = null
        //审核日期 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var vstatus: String? = null
        //审核状态 
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
}