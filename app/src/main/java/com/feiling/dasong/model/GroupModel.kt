package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/22
 * @author ql
 */
class GroupModel {
    var cancelRemark: String? = null
        //取消的备注信息
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var changeBy: String? = null
        //修改人
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var changeDate: String? = null
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
    var createBy: String? = null
        //创建人
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var createDate: String? = null
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
    var groupCode: String? = null
        //操作组编码
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var gruopName: String? = null
        //操作组名称
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var gruopType: String? = null
        //组的类型 默认0-正常1-为临时组2废弃组
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var id: Int? = null//ID
    var opGroupMembers: MutableList<GroupMemberModel>? = null;
    var optType: String? = null
        //当前操作单的状态1-待分配2-已分配3-已取消4-已完成
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var stan1: String? = null
        //备用字段1
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