package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/26
 * @author ql
 */
class PCProcessModel {
    /**
     * 接收日期
     **/
    var accdate: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 接受人
     **/
    var accuser: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *合同子表ID
     **/
    var cdid: Int? = null

    /**
     * 类别编码
     **/
    var cgcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *合同ID
     **/
    var cid: Int? = null

    /**
     * 描述
     **/
    var descriptions: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *生产订单子表ID
     **/
    var did: Int? = null

    /**
     *
     **/
    var edid: Int? = null

    /**
     *生产订单主表ID
     **/
    var id: Int? = null

    /**
     *组合打印
     **/
    var iscomprint: Int? = null

    /**
     *是否需要修正器
     **/
    var iscorrection: Int? = null

    /**
     *删除
     **/
    var isdelete: Int? = null

    /**
     *是否报工
     **/
    var isindustry: Int? = null

    /**
     *委外
     **/
    var isoutsourcing: Int? = null

    /**
     *质检
     **/
    var isqualitytesting: Int? = null

    /**
     *层级
     **/
    var levels: Int? = null

    /**
     * 操作依据
     **/
    var operating: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *生产订单父级Id
     **/
    var pdid: Int? = null

    /**
     * 产品编码
     **/
    var pinvcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 工序名称
     **/
    var pmName: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 工序编码
     **/
    var pmcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 生产订单号
     **/
    var porder: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *准备时间
     **/
    var preparationtime: Float? = null

    /**
     *加工顺序
     **/
    var priority: Int? = null

    /**
     *标准工时
     **/
    var processingtime: Float? = null

    /**
     *报废标识
     **/
    var scrapflag: Int? = null

    /**
     *生产订单工序表Id
     **/
    var sdid: Int? = null

    /**
     *选择标识
     **/
    var selcol: Int? = null

    /**
     *行号
     **/
    var seq: Int? = null

    /**
     *状态 0 未开工 1待开工 2已完工
     **/
    var state: Int? = null

    /**
     *
     **/
    var tempseq: Int? = null

    /**
     * 齿形
     **/
    var toothprofile: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 工序类别
     **/
    var vtype: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
}