package com.feiling.dasong.ui.function.model

import android.graphics.Color
import androidx.annotation.ColorInt
import com.feiling.dasong.ui.model.LabelTextModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/27
 * @author ql
 */
class NavData {


    /**
     * 工序详情
     * @param name 工序名称
     * @param code 工序编码
     * @param status 工序状态
     * @param porder 生产订单号
     * @param orderDate 单据日期
     * @param statusColor 状态颜色
     */
    data class ProcessViewModel(
        var name: String,
        var code: String,
        var status: String,
        var porder: String,
        var orderDate: String,
        @ColorInt var statusColor: Int = Color.BLUE
    ) {

        val view: MutableList<LabelTextModel>
            get() {
                return mutableListOf<LabelTextModel>(
                    LabelTextModel("工序名称", name),
                    LabelTextModel("工序编号", code),
                    LabelTextModel(
                        "工序状态", status, textColor = statusColor
                    ),
                    LabelTextModel("生产订单号", porder),
                    LabelTextModel("单据日期", orderDate)
                )
            }
    }

    /**
     * 产品详情
     * @param clientName    客户名称
     * @param contract      合同号
     * @param invcode       产品编号
     * @param invname       产品名称
     */
    data class ProductViewModel(
        var clientName: String,
        var contract: String,
        var invcode: String,
        var invname: String
    ) {
        val view: MutableList<LabelTextModel>
            get() {
                return mutableListOf<LabelTextModel>(
                    LabelTextModel("客户名称", clientName),
                    LabelTextModel("合同号", contract),
                    LabelTextModel(
                        "产品名称", invname
                    ),
                    LabelTextModel("产品编号", invcode)
                )
            }
    }

    data class PlanDevViewModel(var name: String, var code: String) {
        val view: MutableList<LabelTextModel>
            get() {
                return mutableListOf<LabelTextModel>(
                    LabelTextModel("设备编号", code),
                    LabelTextModel("设备名称", name)
                )
            }
    }

    data class DevViewModel(var name: String, var code: String) {
        val view: MutableList<LabelTextModel>
            get() {
                return mutableListOf<LabelTextModel>(
                    LabelTextModel("设备编号", code),
                    LabelTextModel("设备名称", name)
                )
            }
    }


    data class EmployeeViewModel(
        var name: String,
        var code: String,
        var type: Int = TYPE_EMPLOYEE
    ) {
        companion object {
            const val TYPE_GROUP = 1;
            const val TYPE_EMPLOYEE = 0
        }

        val view: MutableList<LabelTextModel>
            get() {
                if (type == TYPE_GROUP) {
                    return mutableListOf<LabelTextModel>(
                        LabelTextModel("操作组名称", name),
                        LabelTextModel("操作组编号", code)
                    )
                }

                return mutableListOf<LabelTextModel>(
                    LabelTextModel("人员姓名", name),
                    LabelTextModel("人员编号", code)
                )
            }
    }

    /**
     * 供应商信息
     */
    data class SupplierViewModel(
        var name: String,
        var code: String,
        var planOutDate: String,
        var planInDate: String
    )
}