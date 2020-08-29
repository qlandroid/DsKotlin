package com.feiling.dasong.ui.function.devrepair

import android.content.Intent
import android.graphics.Color
import com.feiling.dasong.R
import com.feiling.dasong.comm.BasePresenter
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.http.HttpFileUtils
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.*
import com.feiling.dasong.model.base.ResponseModel
import com.feiling.dasong.ui.adapter.LabelInputModel
import com.feiling.dasong.ui.comm.CommInventorySelectFragment
import com.feiling.dasong.ui.comm.ImgPageFragment
import com.feiling.dasong.ui.comm.InputMsgFragment
import com.feiling.dasong.ui.function.arrange.EmployeeSelectFragment
import com.feiling.dasong.ui.function.dev.DevDetailsFragment
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.arch.QMUIFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.math.BigDecimal

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/1
 * @author ql
 */
class DeviceRepairDetailsPresenter : BasePresenter<DeviceRepairDetailsController.View>(),
    DeviceRepairDetailsController.Presenter {

    companion object {
        /**
         * 跳转输入备注信息
         */
        const val REQUEST_FRAG_INPUT_MSG = 0x300
        const val REQUEST_CODE_EMPLOYEE_SELECT = 0x301

        /**
         * 配件选择
         */
        const val REQUEST_INV_SELECT_CODE = 0X302
    }


    /**
     * 设备维修单编码
     */
    private var mBindDevRepairCode: String? = null
    private var mRepairModel: DevRepairModel? = null

    private var mInputRemark: String? = null;

    /**
     * 设备维修人员
     */
    private var mRepairMemberList: MutableList<EmployeeModel>? = null

    /**
     * 当前所使用配件列表
     */
    private var mPartsList: MutableList<PartsModel> = mutableListOf()

    private var mPartsValueList: MutableList<LabelTextModel>? = null

    private var mPartsInputList: MutableList<LabelInputModel>? = null;


    override fun bindDevRepairCode(deviceCode: String?) {
        mBindDevRepairCode = deviceCode!!;

        loadData(deviceCode)

    }

    override fun actionStart() {
        mView?.showTipLoading("提交中")
        var subscribe = ServiceBuild.deviceRepairService
            .actionStart(mBindDevRepairCode)
            .response()
            .subscribe(
                {
                    mView?.cancelTipLoading()
                    mView?.showTipSuccess("提交成功") {
                        loadData(mBindDevRepairCode)
                    }
                },
                {
                    mView?.cancelTipLoading()
                    it.printStackTrace()
                    mView?.displayMsgDialog(it.getDsMsg())
                })
        addDisposable(subscribe)
    }

    override fun actionEndOk() {
        mView?.showTipLoading("提交中")
        var subscribe = ServiceBuild.deviceRepairService
            .actionOk(mBindDevRepairCode, mInputRemark)
            .response()
            .subscribe(
                {
                    mView?.cancelTipLoading()
                    mView?.showTipSuccess("提交成功") {
                        loadData(mBindDevRepairCode)
                    }
                },
                {
                    mView?.cancelTipLoading()
                    it.printStackTrace()
                    mView?.displayMsgDialog(it.getDsMsg())
                })
        addDisposable(subscribe)
    }

    override fun actionPause() {
        mView?.showTipLoading("提交中")
        var subscribe = ServiceBuild.deviceRepairService
            .actionPause(mBindDevRepairCode)
            .response()
            .subscribe(
                {
                    mView?.cancelTipLoading()
                    mView?.showTipSuccess("提交成功") {
                        loadData(mBindDevRepairCode)
                    }
                },
                {
                    it.printStackTrace()
                    mView?.cancelTipLoading()
                    mView?.displayMsgDialog(it.getDsMsg())
                })
        addDisposable(subscribe)
    }

    override fun actionEndFailed() {
        mView?.showTipLoading("提交中")
        var subscribe = ServiceBuild.deviceRepairService
            .actionFailed(mBindDevRepairCode, mInputRemark)
            .response()
            .subscribe(
                {
                    mView?.cancelTipLoading()
                    mView?.showTipSuccess("提交成功") {
                        loadData(mBindDevRepairCode)
                    }
                },
                {
                    it.printStackTrace()
                    mView?.cancelTipLoading()
                    mView?.displayMsgDialog(it.getDsMsg())
                })
        addDisposable(subscribe)
    }

    override fun actionVerifyOk() {
        if (mRepairMemberList.orEmpty().isEmpty()) {
            mView?.showToast("请添加维修人员")
            return;
        }

        if (mRepairModel!!.stan4 == null) {
            mView?.showToast("请选择维修类型")
            return;
        }

        //将维修人员 编码 转成 1,2,3,4
        var stringBuffer = StringBuffer()
        mRepairMemberList?.forEach {
            stringBuffer.append(it.code)
                .append(",")
        }
        var repairMemberCode = stringBuffer.substring(0, stringBuffer.length - 1)
        mView?.showTipLoading("提交中")
        var subscribe = ServiceBuild.deviceRepairService
            .actionVerifyOk(mBindDevRepairCode!!, repairMemberCode, mRepairModel!!.stan4!!)
            .response()
            .subscribe(
                {
                    mView?.cancelTipLoading()
                    mView?.showTipSuccess("提交成功") {
                        loadData(mBindDevRepairCode)
                    }
                },
                {
                    it.printStackTrace()
                    mView?.cancelTipLoading()
                    mView?.displayMsgDialog(it.getDsMsg())
                })
        addDisposable(subscribe)
    }

    override fun reloadDeviceDetails() {
        loadDeviceDetailsByCode(mRepairModel?.ceqcode)
    }


    private fun addRepairMember(members: MutableList<EmployeeModel>) {
        if (mRepairMemberList == null) {
            mRepairMemberList = mutableListOf();
        }

        mRepairMemberList?.addAll(members)
        var replaceMemberListToNavModel = replaceMemberListToNavModel(mRepairMemberList!!)
        mView?.setRepairMembers(replaceMemberListToNavModel)
    }

    private fun addRepairMember(member: EmployeeModel) {
        if (mRepairMemberList == null) {
            mRepairMemberList = mutableListOf();
        }

        var filter = mRepairMemberList?.filter {
            it.code == member.code
        }
        if (filter.orEmpty().isNotEmpty()) {
            return;
        }
        mRepairMemberList?.add(member);
        var replaceMemberListToNavModel = replaceMemberListToNavModel(mRepairMemberList!!)
        mView?.setRepairMembers(replaceMemberListToNavModel)
    }

    /**
     * 用于设置维修人员列表 不可编辑
     */
    private fun setRepairMembersNoEdit(list: List<EmployeeModel>) {
        var navMembers = replaceMemberListToNavModel(list, false)
        mView?.setRepairMembers(navMembers)

    }

    private fun replaceMemberListToNavModel(
        list: List<EmployeeModel>,
        isDelete: Boolean = true
    ): MutableList<NavDetailsModel> {
        var mutableListOf = mutableListOf<NavDetailsModel>()
        list.forEach {
            mutableListOf.add(replaceMemberToNavModel(it, isDelete))
        }
        return mutableListOf;
    }

    private fun replaceMemberToNavModel(
        member: EmployeeModel,
        isDelete: Boolean = false
    ): NavDetailsModel {
        var navDetailsModel = NavDetailsModel()
        navDetailsModel.title = member.name
        navDetailsModel.subTitle = member.code;
        navDetailsModel.leftIcon = R.drawable.icon_group_member
        if (isDelete) {
            navDetailsModel.hint = "删除"
            navDetailsModel.hintColor = Color.RED
        }

        return navDetailsModel
    }


    override fun removeRepairMember(position: Int) {
        mRepairMemberList?.removeAt(position);
        var replaceMemberListToNavModel = replaceMemberListToNavModel(mRepairMemberList!!)
        mView?.setRepairMembers(replaceMemberListToNavModel)
    }

    override fun actionLookDeviceDetails() {
        if (!mBindDevRepairCode.isNullOrEmpty()) {
            iController?.actionStartFragment(DevDetailsFragment.instance(mBindDevRepairCode))
        }
    }

    override fun actionToInputRemark() {
        var inputRemark = "$mInputRemark\n";
        var instance = InputMsgFragment.instance(normalMsg = inputRemark)
        iController?.actionStartFragmentForResult(instance, REQUEST_FRAG_INPUT_MSG)
    }

    override fun actionToSelectRepairMembers() {
        iController?.actionStartFragmentForResult(
            EmployeeSelectFragment(),
            REQUEST_CODE_EMPLOYEE_SELECT
        )
    }

    override fun actionImgPage(position: Int) {

        iController?.actionStartFragment(
            ImgPageFragment.instance(
                getImageUrls(mRepairModel!!),
                position
            )
        )
    }

    override fun setSelectRepairType(tag: String?) {
        mRepairModel?.stan4 = tag;
        setViewRepairTypeName(tag)
    }

    override fun actionToInventorySelect() {
        iController?.actionStartFragmentForResult(CommInventorySelectFragment(),
            REQUEST_INV_SELECT_CODE)
    }

    /**
     * 获得维修单详情
     */
    private fun loadData(deviceCode: String?) {
        mView?.resetView()
        mView?.showEmptyView(true)

        var subscribe = ServiceBuild.deviceRepairService
            .getById(deviceCode)
            .response()
            .map {
                JsonUtils.fromJson(it.data, DevRepairModel::class.java)
            }
            .subscribe(
                {
                    mRepairModel = it;
                    mInputRemark = it.postscript
                    mView?.hideEmpty()

                    it.partsList?.let { partList ->
                        mPartsList = partList;
                    }

                    setDefaultViewData(it)
                    it.stan4?.let { type ->
                        setViewRepairTypeName(type)
                    }
                    var page = DeviceRepairListContentPage.getPage(it.devState)
                    var verifyState = DevRepairModel.RepairVerifyState.getState(it.vstatus)
                    if (it.devFileList != null) {
                        var images = getImageUrls(it)
                        mView?.setImages(images)
                    }
                    if (verifyState == DevRepairModel.RepairVerifyState.UNVERIFY) {
                        //未审核状态
                        mView?.initUnVerify()
                        setDefaultViewData(it)
                        return@subscribe
                    }
                    changeParts(mPartsList)
                    when (page) {
                        DeviceRepairListContentPage.DEFAULT -> {
                            mView?.initDefaultView()
                        }
                        DeviceRepairListContentPage.STARTED -> {
                            mView?.initStartedView()
                        }
                        DeviceRepairListContentPage.PAUSED -> {
                            mView?.initPausedView()
                        }
                        DeviceRepairListContentPage.END -> {
                            var state = DevRepairModel.RepairState.getState(it.repairState)
                            when (state) {
                                DevRepairModel.RepairState.OK -> {
                                    mView?.initEndOkView()
                                }
                                else -> {
                                    mView?.initEndFailedView()
                                }
                            }
                        }
                        else -> {
                            mView?.showFailed(msg = "未知当前单据状态")
                        }
                    }
                },
                { throwable ->
                    throwable.printStackTrace()
                    Observable.just(throwable)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            mView?.showFailed(msg = it.getDsMsg()) { empty ->
                                empty.showEmptyView(true)
                                loadData(mBindDevRepairCode)
                            }
                        }

                }, {
                    loadDeviceDetailsByCode(mRepairModel?.ceqcode)
                })
        addDisposable(subscribe)
    }

    private fun getImageUrls(it: DevRepairModel): MutableList<String> {
        var images = mutableListOf<String>();
        it.devFileList!!.forEach { fileModel ->
            images.add(HttpFileUtils.replaceImgUrl(fileModel.filePath!!))
        }
        return images
    }

    private fun setDefaultViewData(it: DevRepairModel) {

        //申请人信息
        mView?.setApplyMember(
            mutableListOf<LabelTextModel>(
                LabelTextModel("申请人工号", it.createdby),
                LabelTextModel("申请人姓名", it.createdName),
                LabelTextModel(
                    "申请时间",
                    com.feiling.dasong.uitils.DateUtils.replaceYYYY_MM_dd_HHmmss(it.createddate)
                )
            )
        )
        //描述信息
        mView?.setFaultRemark(it.problemDesc)

        //维修人员
        setRepairMembersNoEdit(it.dmPersonnelList.orEmpty())
        //设置维修描述信息
        mInputRemark = it.postscript;
        mView?.setRemark(mInputRemark)
        //审核人信息
        mView?.setAuditDetails(
            mutableListOf<LabelTextModel>(
                LabelTextModel("审核人工号", it.verifyby),
                LabelTextModel("审核人姓名", it.verifybyName),
                LabelTextModel("审核时间", it.verifydate)
            )
        )
    }


    private fun loadDeviceDetailsByCode(devCode: String?) {
        mView?.setShowDeviceProgress(true)
        var subscribe = ServiceBuild.deviceService.getById(devCode)
            .response()
            .map {
                JsonUtils.fromJson(it.data, DevModel::class.java)
            }
            .subscribe(
                {
                    mView?.setShowDeviceProgress(false)
                    var model = mutableListOf(
                        LabelTextModel("设备名称", it.ceqname),
                        LabelTextModel("设备编码", it.ceqcode)
                    )
                    mView?.setDeviceDetails(
                        model
                    )
                },
                {
                    it.printStackTrace()
                    mView?.setShowDeviceProgress(false)
                    mView?.setLoadDeviceDetailsFailed(it.getDsMsg());
                }
            )
        addDisposable(subscribe)
    }


    /**
     * 批量加载 配件信息
     */
    private fun loadInventoryList(list: List<String>?) {
        mView?.showTipLoading("加载数据中")
        val partList = mutableListOf<PartsModel>()
        addDisposable(Observable.fromIterable(list)
            .flatMap {
                loadInventory(it)
            }
            .response()
            .map {
                it.getData(InventoryModel::class.java)
            }
            .map {
                var partsModel = PartsModel()
                partsModel.partsName = it.invname
                partsModel.partsCode = it.invcode
                partsModel.quantity = BigDecimal(1)
                partsModel
            }
            .subscribe({
                partList.add(it)
            }, {
                it.printStackTrace()
                mView?.displayMsgDialog(it.getDsMsg())
                mView?.cancelTipLoading()
            }, {
                mView?.cancelTipLoading()
                mPartsList.addAll(partList)
                changeParts(mPartsList)
            })
        )

    }

    /**
     * 配件数据变更
     */
    private fun changeParts(list: MutableList<PartsModel>) {
        val it = mRepairModel!!
        var page = DeviceRepairListContentPage.getPage(it.devState)
        var verifyState = DevRepairModel.RepairVerifyState.getState(it.vstatus)
        if (verifyState == DevRepairModel.RepairVerifyState.UNVERIFY) {
            //未审核状态
            mView?.setInvList(replaceLabelValueModel(list))
            return;
        }
        when (page) {
            DeviceRepairListContentPage.STARTED -> {
                //已开始状态
                mView?.setInvInputList(replaceLabelInputModel(list))
            }
            DeviceRepairListContentPage.DEFAULT, DeviceRepairListContentPage.PAUSED, DeviceRepairListContentPage.END -> {
                //结束状态
                mView?.setInvList(replaceLabelValueModel(list))
            }
            else -> {
                mView?.showFailed(msg = "未知当前单据状态")
            }
        }
    }

    private fun replaceLabelValueModel(list: MutableList<PartsModel>): MutableList<LabelTextModel> {
        val valList = mutableListOf<LabelTextModel>()
        list.forEach {
            valList.add(LabelTextModel(it.partsName, it.quantity.toString()))
        }
        return valList
    }

    private fun replaceLabelInputModel(list: MutableList<PartsModel>): MutableList<LabelInputModel> {
        val valList = mutableListOf<LabelInputModel>()
        list.forEach {
            valList.add(LabelInputModel(it, it.partsName, it.quantity.toString()))
        }
        return valList
    }

    private fun loadInventory(invcode: String?): Observable<ResponseModel> {
        return ServiceBuild.commService.getInventoryGetById(invcode!!)
    }

    override fun onFragmentForResult(requestCode: Int?, resultCode: Int?, data: Intent?) {
        when (requestCode) {
            REQUEST_INV_SELECT_CODE -> {
                //配件选择
                if (resultCode == QMUIFragment.RESULT_OK) {
                    var list = CommInventorySelectFragment.getList(data)
                    loadInventoryList(list)
                }

            }
            REQUEST_FRAG_INPUT_MSG -> {
                if (resultCode == QMUIFragment.RESULT_OK) {
                    mInputRemark = InputMsgFragment.getInputMsg(data);
                    mView?.setRemark(mInputRemark)
                }
            }
            REQUEST_CODE_EMPLOYEE_SELECT -> {
                if (resultCode == QMUIFragment.RESULT_OK) {
                    var employeeModel = EmployeeSelectFragment.getEmployeeModel(data!!)
                    addRepairMember(employeeModel)
                }
            }
            else -> {
                super.onFragmentForResult(requestCode, resultCode, data)
            }
        }
    }

    /**
     * 设置view中显示 维修类型
     */
    private fun setViewRepairTypeName(type: String?) {
        mView?.setRepairTypeText(
            when (DevRepairModel.RepairType.getState(type)) {
                DevRepairModel.RepairType.Outsources -> {
                    "委外维修"
                }
                DevRepairModel.RepairType.Location -> {
                    "本厂维修"
                }
                else -> {
                    ""
                }
            }
        )
    }

}