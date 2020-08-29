package com.feiling.dasong.ui.function.devrepair

import android.content.Context
import android.net.Uri
import com.feiling.dasong.comm.IDialogTip
import com.feiling.dasong.comm.IPresenter
import com.feiling.dasong.comm.IView
import com.feiling.dasong.comm.IViewToast
import com.feiling.dasong.ui.model.LabelTextModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
interface DeviceRepairApplyController {

    interface Presenter : IPresenter<View> {
        /**
         * 点击提交按钮
         */
        fun actionSubmit();

        /**
         * 点击选择设备
         */
        fun actionSelectDevice();

        fun loadDeviceDetailsByCode(code: String?)

        fun onResultSelectDevCode(ceqcode: String?)

        /**
         *  查看设备详情
         */
        fun clickLookDeviceDetails()

        /**
         * 选择图片
         */
        fun onResultSelectImg(mSelected: List<Uri>?,context:Context)

    }

    interface View : IView, IViewToast, IDialogTip {

        /**
         * 获得备注信息
         */
        fun getInputRemark(): String

        /**
         *  设置设备详情
         */
        fun setDeviceView(list: MutableList<LabelTextModel>);

        /**
         * 显示dev选择项
         */
        fun displayDeviceSelectMoreMenu()

        /**
         * 扫描二维码
         */
        fun toScanDeviceCode()


        /**
         *  跳转到设备详情页面
         */
        fun toDeviceDetails(devCode: String)
        fun setShowImages(list: MutableList<String>)


    }
}