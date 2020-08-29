package com.feiling.dasong.ui.function.devrepair

import com.feiling.dasong.comm.*
import com.feiling.dasong.ui.adapter.LabelInputModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/30
 * @author ql
 */
interface DeviceRepairDetailsController {

    interface View : IView, IViewToast, IDialogTip, IEmptyView {

        fun resetView();

        /**
         * 初始化未审核状态
         */
        fun initUnVerify()

        /**
         * 初始化 默认状态
         */
        fun initDefaultView();

        /**
         * 初始化 初始化已经开始状态
         */
        fun initStartedView();

        /**
         * 初始化 已经暂停状态
         */
        fun initPausedView()

        /**
         * 初始化 已经结束状态
         */
        fun initEndOkView()

        /**
         * 初始化 已经结束状态
         */
        fun initEndFailedView()


        /**
         * 设置当前状态
         */
        fun setStateText(txt: CharSequence?)

        /**
         * 设置设备详情信息
         */
        fun setDeviceDetails(model: MutableList<LabelTextModel>)

        /**
         * 申请人员填写的设备 故障说明
         */
        fun setFaultRemark(fRemark: CharSequence?)

        /**
         * 维修人员
         */
        fun setRepairMembers(members: MutableList<NavDetailsModel>)

        /**
         * 维修完成填写的备注信息
         */
        fun setRemark(remark: CharSequence?)

        /**
         * 申请维修人员姓名
         */
        fun setApplyMember(member: MutableList<LabelTextModel>?)

        /**
         * 审核信息
         */
        fun setAuditDetails(auditDetail: MutableList<LabelTextModel>?)


        /**
         * 设置显示 加载设备loading
         */
        fun setShowDeviceProgress(isShow: Boolean)

        /**
         * 加载信息失败
         */
        fun setLoadDeviceDetailsFailed(it: CharSequence?)

        /**
         * 设置申请时上传的图片
         */
        fun setImages(imgs: List<String>)

        /**
         * 是否显示 配件信息
         */
        fun showInvList(boolean: Boolean)

        /**
         * 设置配件信息
         */
        fun setInvList(list: MutableList<LabelTextModel>?)

        /**
         * 设置配件信息
         */
        fun setInvInputList(list: MutableList<LabelInputModel>?)

        /**
         * 设置显示维修类型
         */
        fun setRepairTypeText(type: String?)

    }

    interface Presenter : IPresenter<View> {


        /**
         * @param devRepairCode 设备维修单编码
         */
        fun bindDevRepairCode(devRepairCode: String?)

        /**
         * 开工
         */
        fun actionStart()

        /**
         * 维修完成
         */
        fun actionEndOk();

        /**
         * 暂停
         */
        fun actionPause()

        /**
         * 维修完成 没有修理成功
         */
        fun actionEndFailed()

        /**
         * 审核通过
         */
        fun actionVerifyOk()


        /**
         * 重新加载设备信息
         */
        fun reloadDeviceDetails();


        fun removeRepairMember(position: Int)

        /**
         * 查看设备详情
         */
        fun actionLookDeviceDetails()

        /**
         * 需要跳转到 维修描述信息页面
         */
        fun actionToInputRemark()

        /**
         * 跳转选择操作人员
         */
        fun actionToSelectRepairMembers()

        /**
         * 跳转图片详情
         */
        fun actionImgPage(position: Int)


        /**
         * 设置维修类别
         */
        fun setSelectRepairType(tag: String?)

        /**
         * 跳转配件选择
         */
        fun actionToInventorySelect()


    }
}