package com.feiling.dasong.role

import com.feiling.dasong.R
import com.feiling.dasong.model.QDItemDescription
import com.feiling.dasong.ui.function.arrange.ArrangeWorkFragment
import com.feiling.dasong.ui.function.dev.DeviceListFragment
import com.feiling.dasong.ui.function.devcheck.DevCheckListFragment
import com.feiling.dasong.ui.function.devmaintenance.DeviceMaintenanceListFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairApplyFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairListFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairVerifyListFragment
import com.feiling.dasong.ui.function.outsource.OutsourceInFragment
import com.feiling.dasong.ui.function.outsource.OutsourceOutFragment
import com.feiling.dasong.ui.function.quality.ProcessQualityFragment
import com.feiling.dasong.ui.function.statistics.EmplWorkingStatListFragment
import com.feiling.dasong.ui.function.statistics.EmployeeWorkingStatisticsFragment
import com.feiling.dasong.ui.function.statistics.WorkHourPageFragment

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/27
 * @author ql
 */
class RootRole : IRole {


    override fun getMenus(): MutableList<QDItemDescription> {
        return mutableListOf(
            QDItemDescription(ArrangeWorkFragment::class.java, "工序", R.drawable.fun_arrange, ""),
            QDItemDescription(
                ProcessQualityFragment::class.java,
                "质检",
                R.drawable.fun_p_checked,
                ""
            ),
//            QDItemDescription(
//                OutsourceInFragment::class.java,
//                "委外入库",
//                R.drawable.fun_os_in,
//                ""
//            ),
//            QDItemDescription(
//                OutsourceOutFragment::class.java,
//                "委外出库",
//                R.drawable.fun_os_out,
//                ""
//            ),
            QDItemDescription(
                DeviceRepairVerifyListFragment::class.java,
                "维修审核",
                R.drawable.fun_verify,
                ""
            ),
            QDItemDescription(
                DeviceRepairApplyFragment::class.java,
                "维修申请",
                R.drawable.fun_apply,
                ""
            ),
            QDItemDescription(
                DeviceRepairListFragment::class.java,
                "维修",
                R.drawable.fun_repair,
                ""
            ),
            QDItemDescription(DeviceListFragment::class.java, "设备", R.drawable.fun_device, ""),
            QDItemDescription(
                EmployeeWorkingStatisticsFragment::class.java,
                "我的工时",
                R.drawable.fun_my_work_stat,
                ""
            ),
            QDItemDescription(
                DevCheckListFragment::class.java,
                "设备点检",
                R.drawable.fun_dev_check,
                ""
            ),
            QDItemDescription(
                DeviceMaintenanceListFragment::class.java,
                "设备保养",
                R.drawable.fun_dev_main,
                ""
            ),

            QDItemDescription(
                EmplWorkingStatListFragment::class.java,
                "工时统计",
                R.drawable.fun_month_stat,
                ""
            ),
            QDItemDescription(
                WorkHourPageFragment::class.java,
                "工时审核",
                R.drawable.fun_work_stat_audit,
                ""
            )
        )
    }

    override fun hasPermissions(actionType: Action): Boolean {
        return when (actionType) {
            Action.PROCESS_BTN_CANCEL -> {
                true;
            }
        }
    }

}