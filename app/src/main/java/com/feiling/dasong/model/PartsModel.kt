package com.feiling.dasong.model

import java.math.BigDecimal

/**
 * 描述：配件关联表
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/29
 * @author ql
 */
class PartsModel {
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
     *ID唯一键
     **/
    var id: Int? = null

    /**
     * 订单编码(维修 保养 报工)
     **/
    var pCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 配件类型0(维修);1(保养);2(报工)
     **/
    var pType: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 配件编码
     **/
    var partsCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 配件编码
     **/
    var partsName: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *数量
     **/
    var quantity: BigDecimal? = null

}