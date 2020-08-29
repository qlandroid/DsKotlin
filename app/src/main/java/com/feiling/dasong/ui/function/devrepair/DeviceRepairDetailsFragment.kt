package com.feiling.dasong.ui.function.devrepair

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.BaseTopBarFragment
import com.feiling.dasong.comm.IController
import com.feiling.dasong.model.DevRepairModel
import com.feiling.dasong.ui.adapter.LabelInputModel
import com.feiling.dasong.ui.comm.CommInventorySelectFragment
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.widget.ImgSelectView
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import kotlinx.android.synthetic.main.device_repair_details.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/30
 * @author ql
 */
class DeviceRepairDetailsFragment : BaseTopBarFragment(), DeviceRepairDetailsController.View {

    companion object {
        const val TAG_END_OK = "0"
        const val TAG_END_FAILED = "1"

        private const val KEY_CODE = "KEY-CODE";
        fun instance(code: String): DeviceRepairDetailsFragment {
            var bundle = Bundle()
            bundle.putString(KEY_CODE, code);
            var fragment = DeviceRepairDetailsFragment()
            fragment.arguments = bundle;
            return fragment
        }
    }

    lateinit var mView: DeviceRepairDetailsView

    private var mPresenter: DeviceRepairDetailsController.Presenter? = null;


    override fun createContentView(): DeviceRepairDetailsView {
        mView = DeviceRepairDetailsView(context!!)
        return mView
    }

    override fun initData() {
        super.initData()
    }

    override fun onDestroy() {
        mPresenter?.detach()
        super.onDestroy()
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        mTopBar?.setTitle("设备维修申请单")
        mTopBar?.addLeftBackImageButton()?.onClick { popBackStack() }

        var deviceCode = arguments?.getString(KEY_CODE)

        mPresenter = DeviceRepairDetailsPresenter();
        mPresenter?.attach(this)
        mPresenter?.attachAction(object : IController.IAction {
            override fun actionStartFragment(fragment: BaseFragment) {
                startFragment(fragment)
            }

            override fun actionStartFragmentForResult(fragment: BaseFragment, requestCode: Int) {
                startFragmentForResult(fragment, requestCode)
            }
        })


        mPresenter?.bindDevRepairCode(deviceCode);

        mView.setOnImgClick(object : ImgSelectView.OnImgSelectListener {
            override fun onClickAddImg() {
            }

            override fun onClickImg(position: Int) {
                mPresenter?.actionImgPage(position)
            }

            override fun onClickRemove(position: Int) {
            }
        })


    }

    override fun resetView() {
        mView.setImages(null)
        mView.devDetailsProgressBarVisible = false;
        mView.devDetailsProgressBarVisible = true;
        mView.repairRemarkVisible = false;
        mView.faultRemarkText = "";
        mView.faultRemarkVisible = false
        mView.verifyGroupVisible = false
        mView.repairMembers = mutableListOf()
        mView.applyMember = null
        mView.deviceModel = null
        mView.auditDetails = null
        mView.repairRemarkText = ""
        mView.stateText = ""
        mView.bottomBtnGroupVisible = false
        mView.removeBottomButtonAll()
        mView.setShowRepairRemarkInput(false)
        mView.setRepairRemarkInputClick(null)
        mView.setOnRepairMemberClick(null)
        mView.setRepairMemberNavClick(null)
        mView.setShowRepairMemberNavHint(false)

        mView.setInvList(null)
        mView.setShowInvListView(false)
        mView.setOnClickToInvSelect(null)

        mView.scrollMoveToTop()
        mView.setOnRepairTypeClick(null)
        mView.setShowRepairTypeSelectVisible(false)

        mView.setInvNavHint("")
        mView.setShowInvNavNavIcon(false)
    }


    private fun initDeviceDetails() {
        mView.devDetailsProgressBarVisible = true;
        mView.devDetailsFailedVisible = false
        mView.setClickToDevDetails {
            mPresenter?.actionLookDeviceDetails();
        }
    }


    override fun initUnVerify() {
        initDeviceDetails();

        mView.bottomBtnGroupVisible = true;
        mView.faultRemarkVisible = true;
        mView.stateText = "待审核"
        mView.setOnRepairMemberClick {
            mPresenter?.removeRepairMember(it);
        }
        mView.setShowRepairMemberNavHint(true, "选择维修人员")
        mView.setRepairMemberNavClick {
            mPresenter?.actionToSelectRepairMembers();
        }
        mView.setShowRepairTypeSelectVisible(true)

        mView.setOnRepairTypeClick {
            QMUIBottomSheet.BottomListSheetBuilder(context)
                .setTitle("设备维修类别选择")
                .addItem("本场维修", DevRepairModel.RepairType.Location.state)
                .addItem("委外维修", DevRepairModel.RepairType.Outsources.state)
                .setAddCancelBtn(true)
                .setGravityCenter(true)
                .setRadius(QMUIDisplayHelper.dp2px(context, 20))
                .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                    dialog.cancel()
                    mPresenter?.setSelectRepairType(tag);
                }
                .build()
                .show()
        }
        mView.addBottomButton("审核通过", R.id.btn_verify_ok)
            .onClick {
                mPresenter?.actionVerifyOk();
            }
        mView.setShowInvListView(false)
        mView.setInvRvTakeType(DeviceRepairDetailsView.InvTakeType.TakeInput)

    }

    override fun initDefaultView() {
        initDeviceDetails()
        mView.stateText = "待维修"
        mView.bottomBtnGroupVisible = true;
        mView.verifyGroupVisible = true;
        mView.faultRemarkVisible = true;
        mView.addBottomButton("开工", R.id.start)
            .onClick {
                mPresenter?.actionStart()
            }
        mView.setShowInvListView(false)
        mView.setShowRepairTypeSelectVisible(true)

    }

    override fun initStartedView() {
        initDeviceDetails()
        mView.stateText = "已开工"
        mView.setShowRepairTypeSelectVisible(true)

        mView.bottomBtnGroupVisible = true;
        mView.verifyGroupVisible = true;
        mView.faultRemarkVisible = true;
        mView.setShowRepairRemarkInput(true)
        mView.setRepairRemarkInputClick {
            mPresenter?.actionToInputRemark()
        }
        mView.setShowInvListView(true);
        mView.addBottomButton("暂停", R.id.start)
            .onClick {
                mPresenter?.actionPause()
            }
        mView.setShowInvListView(true)
        mView.setInvRvTakeType(DeviceRepairDetailsView.InvTakeType.TakeInput)
        mView.setInvNavHint("添加配件信息")
        mView.setShowInvNavNavIcon(true)

        //消耗品选择
        mView.setOnClickToInvSelect {
            mPresenter?.actionToInventorySelect();
        }

        mView.addBottomButton("完工", R.id.start)
            .onClick {
                QMUIBottomSheet.BottomListSheetBuilder(context)
                    .setTitle("完工操作")
                    .addItem("修理完成", TAG_END_OK)
                    .addItem("未修理完成", TAG_END_FAILED)
                    .setGravityCenter(true)
                    .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                        dialog.dismiss()
                        when (tag) {
                            TAG_END_OK -> {
                                mPresenter?.actionEndOk()
                            }
                            TAG_END_FAILED -> {
                                mPresenter?.actionEndFailed()
                            }
                            else -> {
                            }
                        }
                    }
                    .setRadius(QMUIDisplayHelper.dp2px(context, 20))
                    .build()
                    .show()
            }

    }

    override fun initPausedView() {
        initDeviceDetails()
        mView.stateText = "已暂停"
        mView.faultRemarkVisible = true;

        mView.bottomBtnGroupVisible = true;
        mView.verifyGroupVisible = true;
        mView.addBottomButton("开工", R.id.start)
            .onClick {
                mPresenter?.actionStart()
            }
        mView.setShowRepairTypeSelectVisible(true)

        mView.setShowInvListView(true)
        mView.setInvRvTakeType(DeviceRepairDetailsView.InvTakeType.TakeValue)

    }

    override fun initEndOkView() {
        initDeviceDetails()
        mView.stateText = "修理完成-已维修成功"
        mView.deviceRepairDetailsStateTv.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.green_0
            )
        )
        mView.verifyGroupVisible = true;
        mView.repairRemarkVisible = true;
        mView.faultRemarkVisible = true;
        mView.verifyGroupVisible = true;
        mView.setShowRepairTypeSelectVisible(true)


        mView.setShowInvListView(true)
        mView.setInvRvTakeType(DeviceRepairDetailsView.InvTakeType.TakeValue)

    }

    override fun initEndFailedView() {
        initDeviceDetails()
        mView.setShowRepairTypeSelectVisible(true)

        mView.stateText = "修理完成-未维修成功"
        mView.deviceRepairDetailsStateTv.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.app_color_red
            )
        )

        mView.verifyGroupVisible = true;
        mView.repairRemarkVisible = true;
        mView.faultRemarkVisible = true;
        mView.verifyGroupVisible = true;
    }

    override fun setStateText(txt: CharSequence?) {
        mView.stateText = txt;
    }

    override fun setDeviceDetails(model: MutableList<LabelTextModel>) {
        mView.devDetailsFailedVisible = false;
        mView.devDetailsProgressBarVisible = false;
        mView.deviceModel = model
    }

    override fun setFaultRemark(fRemark: CharSequence?) {
        mView.faultRemarkText = fRemark;
    }

    override fun setRepairMembers(members: MutableList<NavDetailsModel>) {
        mView.repairMembers = members
    }

    override fun setRemark(remark: CharSequence?) {
        mView.repairRemarkText = remark
    }

    override fun setApplyMember(member: MutableList<LabelTextModel>?) {
        mView.applyMember = member
    }

    override fun setAuditDetails(auditDetail: MutableList<LabelTextModel>?) {
        mView.auditDetails = auditDetail
    }


    override fun setShowDeviceProgress(isShow: Boolean) {
        mView.devDetailsProgressBarVisible = isShow
        if (isShow) {
            mView.devDetailsFailedVisible = false;
        }
    }

    override fun setLoadDeviceDetailsFailed(it: CharSequence?) {
        mView.devDetailsFailedVisible = true;
        mView.setDevDetailsFailed(it)
    }

    override fun setImages(imgs: List<String>) {
        mView.setImages(imgs)
    }

    override fun showInvList(boolean: Boolean) {
        mView.setShowInvListView(boolean)
    }

    override fun setInvList(list: MutableList<LabelTextModel>?) {
        mView.setInvList(list)
    }

    override fun setInvInputList(list: MutableList<LabelInputModel>?) {
        mView.setInvInputList(list)
    }

    override fun setRepairTypeText(type: String?) {
        mView.setRepairType(type)
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            else -> {
                if (mPresenter != null) {
                    if (mPresenter is IController.ICallBack) {
                        val iCallBack = mPresenter as IController.ICallBack
                        iCallBack.onFragmentForResult(requestCode, resultCode, data)
                    }
                }

            }
        }


    }


}