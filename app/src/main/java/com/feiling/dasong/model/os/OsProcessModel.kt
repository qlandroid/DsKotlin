package com.feiling.dasong.model.os

/**
 * 描述 : 工序委外明细
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
open class OsProcessModel {

    /**
     * 订单的状态值 未出库,已出库,正在入库,完全入库
     */
    enum class OrderState(var state: String?) {
        INING("正在入库"), IN("已入库"), UNOUT("未出库"), OUT("已出库"),END("完全入库");

        companion object {
            fun getStatus(state: String?): OrderState {
                return when (state) {
                    INING.state -> {
                        INING
                    }
                    IN.state -> {
                        IN
                    }
                    UNOUT.state -> {
                        UNOUT
                    }
                    OUT.state -> {
                        OUT
                    }
                    END.state ->{
                        END
                    }
                    else -> {
                        UNOUT
                    }
                }
            }
        }
    }

    /**
     * 每条工序的状态 0-未出库 ，1-已出库，2-已入库
     */
    enum class OsProcessState(var state: String?, val label: String?) {
        UNOUT("0", "未出库"),
        OUTEND("1", "已出库"),
        INEND("2", "已入库");

        companion object {
            fun getStatus(state: String?): OsProcessState {
                return when (state) {
                    UNOUT.state -> {
                        UNOUT
                    }
                    OUTEND.state -> {
                        OUTEND
                    }
                    INEND.state -> {
                        INEND
                    }
                    else -> {
                        UNOUT
                    }
                }
            }
        }
    }

    /**
     *金额
     **/
    var amount: Float? = null
    /**
     * 工序委外申请单号 code
     **/
    var appccode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *工序委外申请id
     **/
    var appid: Int? = null
    /**
     * 委外单号
     **/
    var ccode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 产品类别
     **/
    var cgcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 类别名称
     **/
    var cgname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 合同号
     **/
    var conno: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 客户简称
     **/
    var custabbname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 客户编码
     **/
    var custcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
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
     *直径上
     **/
    var diam: Float? = null
    /**
     *直径下
     **/
    var diam1: Float? = null
    /**
     *子表id
     **/
    var did: Int? = null
    /**
     * 委外后存货编码
     **/
    var dinvcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 委外后存货名称
     **/
    var dinvname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 委外后存货规格
     **/
    var dinvstd: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 委外后主计量
     **/
    var dinvunitname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *委外后存货数量
     **/
    var dquantity: Float? = null
    /**
     *孔数
     **/
    var holenum: Int? = null
    /**
     *主表id
     **/
    var id: Int? = null
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
     * 主计量
     **/
    var invunitname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *长度上
     **/
    var lengths: Float? = null
    /**
     *长度下
     **/
    var lengths1: Float? = null
    /**
     * 需求日期
     **/
    var pdate: String? = null
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
     * 工序名称
     **/
    var pmname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 生产单号
     **/
    var porder: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *单价
     **/
    var price: Float? = null
    /**
     *加工顺序号
     **/
    var priority: Int? = null
    /**
     *数量
     **/
    var quantity: Float? = null
    /**
     *选择标识
     **/
    var selcol: Int? = null
    /**
     * 机型��格
     **/
    var spec: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 状态值
     **/
    var state: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *累计到货金额
     **/
    var sumarramount: Float? = null
    /**
     *累计到货数量
     **/
    var sumarrquantity: Float? = null
    /**
     *累计开票金额
     **/
    var suminvamount: Float? = null
    /**
     *累计开票数量
     **/
    var suminvquantity: Float? = null
    /**
     *厚度
     **/
    var thinkness: Float? = null
    /**
     *U8请购单ID
     **/
    var u8did: Int? = null
    /**
     *基数值(重量)
     **/
    var weight: Float? = null

}