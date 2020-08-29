package com.feiling.dasong.ui

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.feiling.dasong.DownloadApkFragment
import com.feiling.dasong.DownloadAppService
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.BaseFragmentActivity
import com.feiling.dasong.ui.comm.ImgPageFragment
import com.feiling.dasong.ui.function.arrange.ArrangeWorkFragment
import com.feiling.dasong.ui.function.arrange.EmployeeSelectFragment
import com.feiling.dasong.ui.function.dev.DeviceListFragment
import com.feiling.dasong.ui.function.devcheck.DevCheckListFragment
import com.feiling.dasong.ui.function.devmaintenance.DeviceMaintenanceListFragment
import com.feiling.dasong.ui.function.quality.ProcessQualityFragment
import com.feiling.dasong.ui.function.group.GroupManagerFragment
import com.feiling.dasong.ui.function.outsource.OutsourceInFragment
import com.feiling.dasong.ui.function.outsource.OutsourceOutFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairApplyFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairListFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairVerifyListFragment
import com.feiling.dasong.ui.function.statistics.EmplWorkingStatListFragment
import com.feiling.dasong.ui.function.statistics.EmployeeWorkingStatisticsFragment
import com.feiling.dasong.ui.function.statistics.WorkHourPageFragment
import com.feiling.dasong.ui.home.HomeFragment
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment
import com.qmuiteam.qmui.arch.annotation.FirstFragments
import com.qmuiteam.qmui.arch.annotation.LatestVisitRecord
import com.qmuiteam.qmui.skin.QMUISkinManager

@FirstFragments(
    value = [
        ArrangeWorkFragment::class
        , LoginFragment::class
        , EmployeeSelectFragment::class
        , ProcessQualityFragment::class
        , OutsourceInFragment::class
        , OutsourceOutFragment::class
        , GroupManagerFragment::class
        , DeviceListFragment::class
        , EmployeeWorkingStatisticsFragment::class
        , EmplWorkingStatListFragment::class
        , WorkHourPageFragment::class
        , DeviceRepairApplyFragment::class
        , DeviceRepairListFragment::class
        , DeviceRepairVerifyListFragment::class
        , DevCheckListFragment::class
        , DeviceMaintenanceListFragment::class
        , ImgPageFragment::class
    ]
)
@DefaultFirstFragment(HomeFragment::class)
@LatestVisitRecord
class HomeActivity : BaseFragmentActivity() {

    companion object {
        fun of(
            context: Context,
            firstFragment: Class<out BaseFragment>
        ): Intent? {
            return intentOf(
                context,
                HomeActivity::class.java, firstFragment
            )
        }

        fun of(
            context: Context,
            firstFragment: Class<out QMUIFragment?>,
            fragmentArgs: Bundle?
        ): Intent? {
            return intentOf(
                context,
                HomeActivity::class.java, firstFragment, fragmentArgs
            )
        }
    }

    override fun getContextViewId(): Int {
        return R.id.dasong
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setSkinManager(QMUISkinManager.defaultInstance(this))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 获取到Activity下的Fragment
        val fragments = supportFragmentManager.fragments;
        // 查找在Fragment中onRequestPermissionsResult方法并调用
        for (fragment in fragments) {
            if (fragment is QMUIFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }

        }
    }

    private fun startDownload() {
        startFragment(DownloadApkFragment())
        var intent = Intent(this, DownloadAppService::class.java)
        bindService(intent, object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                var uploadBinder = service as DownloadAppService.UploadBinder
                if (!uploadBinder.isStartDownload) {
                    var sendAction =
                        DownloadAppService.actionStartDownload(
                            "http://192.168.2.254:8096/bdrawing/exportExcel",
                            "version200"
                        )
                    sendBroadcast(sendAction)
                }

            }
        }, Service.BIND_AUTO_CREATE)
    }

}
