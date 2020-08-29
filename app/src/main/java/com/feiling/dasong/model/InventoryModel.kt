package com.feiling.dasong.model

/**
 * 描述：存货基础档案
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/28
 * @author ql
 */
class InventoryModel {

    /**
     * 修改日期
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
    var changeddate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 建档日期
     **/
    var createdby: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 建档人
     **/
    var createddate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *盘点日
     **/
    var days: Int? = null

    /**
     *盘点周期
     **/
    var frequency: Int? = null

    /**
     * 盘点周期单位
     **/
    var frequencyunit: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 存货代码
     **/
    var invaddcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 分类编码
     **/
    var invccode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 分类名称
     **/
    var invcname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 存货编码
     **/
    var invcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 英文名称
     **/
    var invengname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 存货名称
     **/
    var invname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 规格型号
     **/
    var invstd: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 计量单位编码
     **/
    var invunitcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 计量单位名称
     **/
    var invunitname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *批次管理标识
     **/
    var isbatch: Int? = null

    /**
     *生成耗用标识
     **/
    var iscomsume: Int? = null

    /**
     *外销标识
     **/
    var isexpsale: Int? = null

    /**
     *采购标识
     **/
    var ispurchase: Int? = null

    /**
     *内销标识
     **/
    var issale: Int? = null

    /**
     *自制标识
     **/
    var isself: Int? = null

    /**
     * 最后盘点日期
     **/
    var lastdate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *包装批量
     **/
    var lots: Float? = null

    /**
     * 来源
     **/
    var resource: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * UPC编码
     **/
    var upccode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
}