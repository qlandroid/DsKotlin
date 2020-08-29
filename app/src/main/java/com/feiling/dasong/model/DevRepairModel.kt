package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/30
 * @author ql
 */
class DevRepairModel {
    enum class RepairVerifyState(val state: String) {
        UNVERIFY("0"), VERIFY("1");

        companion object {
            fun getState(state: String?): RepairVerifyState {
                return when (state) {
                    RepairVerifyState.VERIFY.state -> {
                        RepairVerifyState.VERIFY
                    }
                    else -> {
                        RepairVerifyState.UNVERIFY
                    }
                }
            }
        }
    }

    /**
     * 维修状态
     * 0 -本地维修，1-委外维修
     */
    enum class RepairType(var state: String) {
        Location("0"), Outsources("1"), Other("");

        companion object {
            fun getState(state: String?): RepairType {
                return when (state) {
                    RepairType.Outsources.state -> {
                        RepairType.Outsources
                    }
                    RepairType.Location.state -> {
                        RepairType.Location
                    }
                    else -> {
                        RepairType.Other
                    }
                }
            }
        }
    }

    enum class RepairState(val stateValue: String) {
        OK("1"), FAILED("0");

        companion object {
            fun getState(state: String?): RepairState {
                return when (state) {
                    OK.stateValue -> {
                        OK
                    }
                    else -> {
                        FAILED;
                    }
                }
            }
        }
    }

    /**
     * 申请维修的照片
     */
    var devFileList: MutableList<FileModel>? = null

    /**
     * 审核人姓名
     */
    val verifybyName: String? = null;

    /**
     * 维修人员
     **/
    var accendant: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 设备编码
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
     * 创建人 姓名
     **/
    var createdName: String? = null
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
     * 单据编码 (待维修订单)
     **/
    var devCode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 单据状态(0
     **/
    var devState: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 指定维修人员(多个)
     **/
    var dmPersonnel: String? = null
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
     *维修工时(分钟)
     **/
    var mmh: Int? = null

    /**
     * 备注信息
     **/
    var postscript: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 问题描述
     **/
    var problemDesc: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 维修状态(0
     **/
    var repairState: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *
     **/
    var stan1: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *
     **/
    var stan10: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *
     **/
    var stan2: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *
     **/
    var stan3: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *  stan4 (string): 维修类型(0:本厂维修, 1:外部维修) ,
     **/
    var stan4: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *  stan5 (string): 维修的供应商
     **/
    var stan5: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *
     **/
    var stan6: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *
     **/
    var stan7: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *
     **/
    var stan8: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     *
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
     * 审核状态(0:未审核;1:已审核)
     **/
    var vstatus: String? = "0"
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    /**
     * 指定设备维修人员 列表
     */
    var dmPersonnelList: MutableList<EmployeeModel>? = null;

    /**
     * 使用配件列表
     */
    var partsList: MutableList<PartsModel>? = null

}