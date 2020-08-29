package com.feiling.dasong.model.os

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
class OutsourceModel : OsProcessModel() {


//    /**
//     * 委外单号
//     **/
//    var ccode: String? = null
//        get() {
//            if (field == null) {
//                return ""
//            }
//            return field
//        }
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
     * 修改人
     **/
    var changedbys: String? = null
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
     * 创建人
     **/
    var createdbys: String? = null
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
     * 委外日期
     **/
    var ddate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 部门编码
     **/
    var depcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 部门名称
     **/
    var description: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 接收仓库
     **/
    var dwhcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 接收仓库���
     **/
    var dwhname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 预计完工日期
     **/
    var fdate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
//    /**
//     *主表id
//     **/
//    var id: Int? = null
    /**
     * 备注
     **/
    var remarks: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
//    /**
//     *选择标识
//     **/
//    var selcol: Int? = null
//    /**
//     * 状态
//     **/
//    var state: String? = null
//        get() {
//            if (field == null) {
//                return ""
//            }
//            return field
//        }
    /**
     * 供应商简称
     **/
    var suppabbname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 供应商编码
     **/
    var suppcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 供应商名称
     **/
    var suppname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 发出仓库
     **/
    var swhcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 发出仓库名
     **/
    var swhname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *u8id
     **/
    var u8id: Int? = null
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
     * 审核人
     **/
    var verifybys: String? = null
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
     *审核状态
     **/
    var vstatus: Int? = null
    /**
     * 审核状态
     **/
    var vstatuss: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *委外类型
     **/
    var vtype: Int? = null
    /**
     *
     **/
    var vtype2: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 委外类型
     **/
    var vtypes: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 工序委外子表
     */
    var boutsources: MutableList<OsProcessModel>? = null

}