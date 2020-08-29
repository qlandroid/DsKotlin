package com.feiling.dasong

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import butterknife.ButterKnife
import com.feiling.dasong.comm.DsCommActivity
import com.feiling.dasong.role.RoleUtils
import com.feiling.dasong.ui.HomeActivity
import com.feiling.dasong.ui.LoginFragment
import com.feiling.dasong.uitils.LoginUtils
import com.qmuiteam.qmui.util.QMUIPackageHelper
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : DsCommActivity(), EasyPermissions.PermissionCallbacks {
    //string[]数组设置需要的权限


    val MAX_COUNT = 3;
    var CAMERA_REQUEST_CODE = 1000
    var lastCount: Int = 0
    val mHandler: Handler = Handler()
    override fun createView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        checkCameraPermission()
    }

    override fun initWidget() {
        super.initWidget()
        val appVersion = QMUIPackageHelper.getAppVersion(this)
        mainVersion.text = "上海婓灵制作 版本：$appVersion"

    }


    private fun checkCameraPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            mHandler.postDelayed(object : Runnable {
                override fun run() {
                    lastCount++
                    if (lastCount == MAX_COUNT) {
                        toHome();
                    } else {
                        mHandler.postDelayed(this, 1_000);
                    }
                }

            }, 1_000)
        } else {
            EasyPermissions.requestPermissions(
                this,
                "应用程序需要访问您的相机,您需要在下个弹窗中允许我们使用照相机",
                CAMERA_REQUEST_CODE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    fun toHome() {


        val intent = if (LoginUtils.isLogin(this)!!) {
            DsAPI.role = RoleUtils.getRoleByType(LoginUtils.getUserModel(this));
            Intent(this, HomeActivity::class.java);
        } else {
            HomeActivity.of(this, LoginFragment::class.java)
        }
        startActivity(intent)
        finish()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //拒绝
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "您拒绝授权,并勾选了不在提醒" + CAMERA_REQUEST_CODE, Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this).setTitle("打开应用程序设置修改应用程序权限").build().show()
        } else {
            Toast.makeText(this, "您拒绝授权" + CAMERA_REQUEST_CODE, Toast.LENGTH_SHORT).show()
            checkCameraPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //同意了全选
//        Toast.makeText(this, "您同意了授权" + CAMERA_REQUEST_CODE, Toast.LENGTH_SHORT).show()
        checkCameraPermission()


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            checkCameraPermission()
        }
    }

}
