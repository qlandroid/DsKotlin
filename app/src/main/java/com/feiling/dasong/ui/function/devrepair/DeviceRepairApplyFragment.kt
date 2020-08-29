package com.feiling.dasong.ui.function.devrepair

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.util.Log
import android.view.View
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseTopBarFragment
import com.feiling.dasong.ui.function.dev.DevAllSelectFragment
import com.feiling.dasong.ui.function.dev.DevDetailsFragment
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.uitils.LogUtils
import com.feiling.dasong.uitils.UriUtil
import com.feiling.dasong.uitils.imgload.DSGildeEngine
import com.feiling.dasong.widget.ImgSelectView
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.fragment_dev_apply_repair.*
import java.io.File


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
class DeviceRepairApplyFragment : BaseTopBarFragment(), DeviceRepairApplyController.View {

    private lateinit var mPresenter: DeviceRepairApplyController.Presenter

    companion object {
        const val SCAN_REQUEST_DEVICE_CODE: Int = 0x333
        const val REQUEST_DEV_ALL_SELECT = 0X444

        //图片选择
        const val REQUEST_CODE_CHOOSE = 0x9001;
    }


    private var dialogLabelScan: String? = null;
    private var dialogLabelDevAll: String? = null;
    private var mSelected: List<Uri>? = null;

    override fun createContentView(): View {
        return layoutInflater.inflate(R.layout.fragment_dev_apply_repair, null)
    }

    override fun initData() {
        super.initData()
        dialogLabelScan = getString(R.string.to_scan)
        dialogLabelDevAll = getString(R.string.dev_all_select)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        mTopBar?.setTitle("设备维修申请")
        mTopBar?.addLeftBackImageButton()?.onClick { popBackStack() }
        mPresenter = DeviceRepairApplyPresenter();
        mPresenter.attach(this)

        deviceApplyRepairView.setDeviceNavClick {
            mPresenter.actionSelectDevice()
        }
        deviceApplyRepairView.setSubmitClick {
            mPresenter.actionSubmit()
        }
        deviceApplyRepairView.setLookDeviceDetailsClick {
            mPresenter.clickLookDeviceDetails();
        }

        deviceApplyRepairView.setOnSelectImageClick(object : ImgSelectView.OnImgSelectListener {
            override fun onClickAddImg() {
                Matisse.from(this@DeviceRepairApplyFragment)
                    .choose(MimeType.ofImage())
                    .capture(true)
                    .captureStrategy(CaptureStrategy(true, "com.feiling.dasong"))
                    .countable(true)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .maxSelectable(9)
                    .theme(R.style.Matisse_Dracula)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .imageEngine(DSGildeEngine())
                    .forResult(REQUEST_CODE_CHOOSE)
            }

            override fun onClickImg(position: Int) {

            }

            override fun onClickRemove(position: Int) {

            }
        })
    }


    override fun getInputRemark(): String {
        return deviceApplyRepairView.inputRemark.toString()

    }

    override fun setDeviceView(list: MutableList<LabelTextModel>) {
        deviceApplyRepairView.setDeviceView(list)
    }

    override fun displayDeviceSelectMoreMenu() {
        QMUIBottomSheet.BottomListSheetBuilder(context)
            .setGravityCenter(true)
            .setTitle("操作选择")
            .setRadius(QMUIDisplayHelper.dp2px(context, 20))
            .addItem(dialogLabelScan)
            .addItem(dialogLabelDevAll)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                dialog.dismiss()
                when (tag) {
                    dialogLabelScan -> {
                        toScanning(SCAN_REQUEST_DEVICE_CODE)
                    }
                    dialogLabelDevAll -> {
                        var devAllSelectFragment = DevAllSelectFragment()
                        startFragmentForResult(devAllSelectFragment, REQUEST_DEV_ALL_SELECT)
                    }
                    else -> {
                    }
                }
            }
            .setAddCancelBtn(true)
            .build()
            .show()
    }

    override fun toScanDeviceCode() {
        scanRequestCode = SCAN_REQUEST_DEVICE_CODE;
        toScanning()
    }

    override fun toDeviceDetails(devCode: String) {
        var instance = DevDetailsFragment.instance(devCode)
        startFragment(instance)
    }

    override fun setShowImages(list: MutableList<String>) {
        deviceApplyRepairView.setSelectImgList(list)
    }

    override fun onScanningCodeResult(code: String?, requestCode: Int?) {

        when (scanRequestCode) {
            SCAN_REQUEST_DEVICE_CODE -> {
                mPresenter.loadDeviceDetailsByCode(code)
            }
            else -> {
                super.onScanningCodeResult(code, requestCode)
            }
        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_DEV_ALL_SELECT -> {
                if (resultCode != QMUIFragment.RESULT_OK) {
                    return
                }
                var result = DevAllSelectFragment.getData(data)
                mPresenter.onResultSelectDevCode(result?.ceqcode);
            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_CHOOSE -> {
                if (data == null) {
                    return;
                }
                mSelected = Matisse.obtainResult(data);
                mPresenter.onResultSelectImg(mSelected, context!!)
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun shutDown() {
        super.shutDown()
        popBackStack()
    }

    override fun onDestroy() {
        mPresenter.detach()
        super.onDestroy()
    }


}