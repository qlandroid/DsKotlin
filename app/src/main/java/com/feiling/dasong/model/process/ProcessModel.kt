package com.feiling.dasong.model.process

import androidx.annotation.StringRes
import com.feiling.dasong.R
import com.feiling.dasong.model.DevModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/15
 * @author ql
 */

class ProcessModel {

    //0 待分配 1待开工 2已开工 3已暂停 4完工
    enum class StatusType(val type: String, @StringRes val title: Int) {
        DEFAULT(
            "0",
            R.string.task_status_default
        ),
        PENDING(
            "1",
            R.string.task_status_pending
        ),
        STARTED("2", R.string.task_status_start),
        PAUSED(
            "3",
            R.string.task_status_pause
        ),
        ENDED("4", R.string.task_status_end);

        companion object {
            fun getStatus(status: String?): StatusType {
                return when (status) {
                    DEFAULT.type -> DEFAULT
                    PENDING.type -> PENDING
                    STARTED.type -> STARTED
                    PAUSED.type -> PAUSED
                    ENDED.type -> ENDED
                    else -> DEFAULT
                }
            }
        }
    }

    enum class CheckStatus(var status: String?) {
        /**
         * 0-合格
         * 1-不合格
         * 2-让步接收
         */
        OK("0"), FAILED("1"), CONCESSION("2")
    }

    var bprocesschecks: MutableList<ProcessCheckModel>? = null

    /**
     * 实际使用设备
     */
    var devicetestu8Actual: DevModel? = null

    /**
     * 计划使用设备
     */
    var devicetestu8Plan: DevModel? = null

    /**
     * 实际使用设备编码
     */
    var actualCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 实际完成工时总时间，以分钟为单位
     */
    var actualTimer: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 系统完成工时总时间，以分钟为单位
     */
    var srcTimer: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }


    /**
     * 编码唯一值
     */
    var ccode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 类别编码
     */
    var cgcode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 修改人
     */
    var changeBy: String? = null
        //
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

    /**
     * 合同号
     */
    var contractnum: String? = null
        //
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

    /**
     * 创建日期
     */
    var createDate: Long? = null

    /**
     * 客户名称
     */
    var custabbname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 客户编码
     */
    var custcode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 单据日期
     */
    var ddate: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 操作人员唯一键
     */
    var employeeCode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 操作组唯一键
     */
    var groupCode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 工序所属加工单 工序的唯一键
     */
    var id: Int? = null//

    /**
     * 产品名称
     */
    var invname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 是否报工
     */
    var isindustry: Int? = null//

    /**
     *  单据状态(0 待分配 1待开工 2已开工 3已暂停 4完工)
     */
    var opState: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 分配类型 0-人员，1-组
     */
    var opType: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 工序所属加工单 工序的唯一键
     */
    var pcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 产品编码
     */
    var pinvcode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 计划使用设备编码
     */
    var planCode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 计划完成工时总时间，以分钟为单位
     */
    var planTimer: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 工序编码
     */
    var pmcode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 工序名称
     */
    var pmname: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 生产订单号
     */
    var porder: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 加工顺序
     */
    var priority: Int? = null//

    /**
     * 当前工序的唯一键
     */
    var processCode: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 生产订单子表ID
     */
    var processDid: Int? = null//

    /**
     * 生产订单主表ID
     */
    var processId: Int? = null//

    /**
     * 生产订单工序表Id
     */
    var processSdid: Int? = null//

    /**
     * 报废标识
     */
    var scrapflag: Int? = null//

    /**
     * 备用字段
     */
    var stan1: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 失败，让步接收下显示 输入的内容
     */
    var stan8: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 审核人
     */
    var verifyby: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 审核日期
     */
    var verifydate: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 审核状态
     */
    var vstatus: String? = null
        //
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    var stan5: String? = null

    /**
     * 质量检测结果 合格-0/不合格-1
     */
    var qualityState: String? = null

}