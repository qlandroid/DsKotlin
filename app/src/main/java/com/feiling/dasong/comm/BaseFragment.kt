package com.feiling.dasong.comm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.acker.simplezxing.activity.CaptureActivity
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.ui.DialogTipHelper
import com.feiling.dasong.widget.CustomToast
import com.qmuiteam.qmui.arch.QMUIFragment
import io.reactivex.disposables.Disposable


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/3/25
 * @author ql
 */
abstract class BaseFragment : QMUIFragment() {

    lateinit var unbinder: Unbinder
    open lateinit var mDialogTipHelper: DialogTipHelper
    lateinit var rootView: ViewGroup
    var scanRequestCode: Int? = null;

    override fun onCreateView(): View {
        rootView = createView() as ViewGroup;
        unbinder = ButterKnife.bind(this, rootView)
        return rootView;
    }


    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)
        mDialogTipHelper = DialogTipHelper(context)
        initData();
        initWidget(rootView);
    }


    open fun load() {

    }


    open fun initWidget(rootView: View) {

    }

    open fun initData() {

    }

    abstract fun createView(): View

    fun onChange() {

    }
    fun toScanning(requestCode: Int? = null) {
        scanRequestCode = requestCode
        val intent: Intent = Intent(context, CaptureActivity::class.java)
        val bundle: Bundle = Bundle()
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP)
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION)
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE)
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF)
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO)
        bundle.putBoolean(
            CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN,
            CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN
        )
        bundle.putBoolean(
            CaptureActivity.KEY_NEED_SCAN_HINT_TEXT,
            CaptureActivity.VALUE_SCAN_HINT_TEXT
        )
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle)
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CaptureActivity.REQ_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val code =
                        data!!.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT)
                    onScanningCodeResult(code, scanRequestCode)
                }
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    open fun onScanningCodeResult(code: String?, requestCode: Int? = null) {

    }

    open fun runUiThread(block: () -> Unit) {
        activity?.runOnUiThread(block)
    }

    fun addDisposable(disposable: Disposable) {
        RxApiManager.add(this, disposable)
    }

    override fun onDestroyView() {
        unbinder.unbind()
        RxApiManager.cancel(this)
        super.onDestroyView()
    }
    override fun onDestroy() {
        unbinder.unbind()
        RxApiManager.cancel(this)
        super.onDestroy()
    }

    override fun popBackStack() {
        super.popBackStack()

    }

}