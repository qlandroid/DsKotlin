package com.feiling.dasong.ui.function.arrange

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.acker.simplezxing.activity.CaptureActivity
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseTopBarFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.comm.toStringMap
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.ResultCode
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.DevModel
import com.feiling.dasong.model.EmployeeModel
import com.feiling.dasong.model.GroupModel
import com.feiling.dasong.model.process.ProcessModel
import com.feiling.dasong.ui.function.arrange.model.ProcessParams
import com.feiling.dasong.ui.function.dev.DevDetailsFragment
import com.feiling.dasong.ui.function.devcheck.DevCheckInputFragment
import com.feiling.dasong.ui.function.model.NavData
import com.feiling.dasong.uitils.ExceptionUtils
import com.google.gson.JsonElement
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_task_details.*

/**
 * 描述： 工序详情
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/4
 * @author ql
 */
class TaskDetailsFragment() : BaseTopBarFragment(),
    TaskDetailsView.TaskDetailsListener {

    enum class GroupType(val type: String) {
        /**
         * 0 是 人，1-组
         */
        MEMBER("0"), GROUP("1")
    }

    companion object {
        private val KEY_PROCESS_CODE: String by lazy { "KEY_PROCESS_CODE" }

        /**
         * 跳转设备点检REQUEST
         */
        const val REQUEST_DEV_CHECK = 0X3000

        /**
         * 跳转交接人员选择
         */
        const val REQUEST_PM_HANDOVER = 0X3001
        fun instance(pCode: String): TaskDetailsFragment {
            val args = Bundle();
            args.putString(KEY_PROCESS_CODE, pCode)
            val taskFragment = TaskDetailsFragment()
            taskFragment.arguments = args;
            return taskFragment;
        }

        const val DEV_SELECT_CODE: Int = 2//设备选择
        const val EMPL_SELECT_CODE: Int = 3//员工选择
        const val REJECT_CODE: Int = 4 // 驳回信息填写
        const val GROUP_SELECT_CODE = 5 //组选择

        //已开工状态下,检测设备编码
        const val LOAD_DEV_TYPE_GET_CODE = 1001;

        //待开工状态下，获得设备详情
        const val LOAD_DEV_TYPE_OPT_DEV = 1002;


        const val DEV_SELECT_SHEET_TYPE_SCANNING = "SCANNING";
        const val DEV_SELECT_SHEET_TYPE_INPUT = "INPUT";
    }

    private var loadDevType = LOAD_DEV_TYPE_OPT_DEV

    lateinit var mProcessCode: String

    lateinit var mOptProcess: ProcessModel
    private var mGetScanningCode: ((String?) -> Unit)? = null

    /**
     * 计划使用设备编码
     */
    private var mPlanDevCode: String? = null

    /**
     * 当前使用的设备编码
     */
    private var mOptDevCode: String? = null

    private var mPlanDevModel: DevModel? = null;

    private lateinit var mSendParams: ProcessParams.ProcessSendParams;

    /**
     * 检测设备未点检时，临时保存开工时需要用的设备编码
     */
    private var lastSubmitDevCode: String? = null;

    override fun createContentView(): View {
        return LayoutInflater.from(context).inflate(R.layout.fragment_task_details, null)
    }


    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        mTopBar?.addLeftBackImageButton()?.setOnClickListener { popBackStack() }
        taskDetailsView.mTaskDetailsListener = this;
        mProcessCode = arguments?.getString(KEY_PROCESS_CODE).orEmpty()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()

    }


    /**
     * 更多菜单显示
     */
    private fun displayMoreMenu() {
        //查看工时
        var menuBuilder = QMUIDialog.MenuDialogBuilder(context)
            .addItem(getString(R.string.worktimer_stat)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss();
                startFragment(ProcessTimerStatFragment.instance(mOptProcess.ccode))
            }

        //除分配状态下，都可以查看操作记录
        val status = ProcessModel.StatusType.getStatus(mOptProcess.opState)
        if (status != ProcessModel.StatusType.DEFAULT) {
            menuBuilder
                .addItem(getString(R.string.option_record)) { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss();
                    val instance = ProcessRecodeFragment.instance(mOptProcess.ccode)
                    startFragment(instance)
                }
        }
        //查看全部工序
        menuBuilder.addItem(getString(R.string.option_all_process)) { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
            var instance = ProcessAllFragment.instance(mOptProcess.ccode, mOptProcess.processSdid!!)
            startFragment(instance)
        }


        menuBuilder.show();

    }


    private fun loadData() {
        mEmptyView?.showEmptyView()
        val subscribe = ServiceBuild.processService.processDetailsById(mProcessCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isFailed) {
                    throw DsException(it.code.toString(), it.message!!)
                }
                it.getData(ProcessModel::class.java);
            }
            .doOnNext {
                mOptProcess = it
                initCommView()
                val planceqname = it.devicetestu8Plan?.ceqname
                val ceqname = it.devicetestu8Actual?.ceqname
                when (it.opState) {
                    ProcessModel.StatusType.DEFAULT.type -> {
                        initDefault()
                    }
                    ProcessModel.StatusType.PENDING.type -> {
                        initPending(planceqname)
                    }
                    ProcessModel.StatusType.STARTED.type -> {
                        initStarted(ceqname, planceqname)
                    }
                    ProcessModel.StatusType.PAUSED.type -> {
                        initPause(planceqname)
                    }
                    ProcessModel.StatusType.ENDED.type -> {
                        initEnd(planceqname)
                    }
                    else -> {
                    }
                }
                mOptDevCode = it.actualCode
                mPlanDevCode = it.planCode
                mTopBar?.setTitle(mOptProcess.pmname)
            }
            .doOnComplete {
                hideEmpty()
                mTopBar?.addRightImageButton(R.drawable.btn_nav_more, R.id.topbar_right_yes_button)
                    ?.onClick {
                        displayMoreMenu()
                    }
            }
            .doOnError {
                it.printStackTrace()
                runUiThread {
                    showFailed(msg = it.getDsMsg()) {
                        showEmptyView();
                        loadData();
                    }
                }
            }
            .filter {
                //只有在默认状态，待分配状态才加载的 默认使用设备
                ProcessModel.StatusType.getStatus(it.opState) == ProcessModel.StatusType.DEFAULT
            }
            .observeOn(Schedulers.io())
            .flatMap {
                //加载工序绑定的设备列表，默认使用第一条数据，设置为计划使用设备
                ServiceBuild.processService.getDevListByPCode(it.pmcode!!)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap() { t ->
                if (t.isFailed) {
                    throw DsException(t.message!!)
                }
                if (t.data == null) {
                    throw DsException("列表获得错误")
                }
                Observable.fromIterable<JsonElement?>(t.data!!.asJsonArray)
            }
            .firstElement()//只获得第一条数据，
            .map { t ->
                C.sGson.fromJson(C.sGson.toJson(t), DevModel::class.java)
            }
            .subscribe(
                {
                    setPlanDevDetails(it)
                    changeSelectDev(it)
                }, {
                    it.printStackTrace()
                }, {
                    hideEmpty()
                })
        addDisposable(subscribe)
    }


    private fun displaySelectEmployee(list: List<EmployeeModel>) {
        var build = QMUIBottomSheet.BottomListSheetBuilder(context)
            .setGravityCenter(true)
            .setTitle(getString(R.string.process_plan_option_employee_select))
            .setRadius(40)
            .setAddCancelBtn(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                dialog.dismiss()
                var filter = list.filter { it.code == tag }
                var employeeModel = filter[0]
                setSelectEmployee(employeeModel)
            }
        list.forEach {
            build.addItem(R.drawable.icon_group_member, "${it.firstname}(${it.code})", it.code)
        }
        var bottomSheet = build.build()
        bottomSheet.show();

    }

    private fun displaySelectGroup(list: List<GroupModel>) {
        var build = QMUIBottomSheet.BottomListSheetBuilder(context)
            .setGravityCenter(true)
            .setTitle(getString(R.string.process_plan_option_group_select))
            .setRadius(40)
            .setAddCancelBtn(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                dialog.dismiss()
                var filter = list.filter { it.groupCode == tag }
                var groupModel = filter[0]
                setSelectGroup(groupModel)
            }
        list.forEach {
            build.addItem(
                R.drawable.icon_group_member,
                "${it.gruopName}(${it.groupCode})",
                it.groupCode
            )
        }
        var bottomSheet = build.build()
        bottomSheet.show();
    }

    private fun initEnd(planceqname: String?) {
        taskDetailsView.initEnded()
        taskDetailsView.setPlanDevDetails(
            NavData.PlanDevViewModel(
                planceqname.orEmpty(),
                mOptProcess.planCode!!
            )
        )

    }

    private fun initPause(planceqname: String?) {
        taskDetailsView.initPaused()
        taskDetailsView.setPlanDevDetails(
            NavData.PlanDevViewModel(
                planceqname.orEmpty(),
                mOptProcess.planCode.orEmpty()
            )
        )
    }

    private fun initStarted(ceqname: String?, planceqname: String?) {
        taskDetailsView.initStarted()
        taskDetailsView.setOptDevDetails(
            NavData.DevViewModel(
                ceqname.orEmpty(),
                mOptProcess.actualCode!!
            )
        )
        taskDetailsView.setPlanDevDetails(
            NavData.PlanDevViewModel(
                planceqname.orEmpty(),
                mOptProcess.planCode.orEmpty()
            )
        )
    }

    private fun initPending(planceqname: String?) {
        taskDetailsView.initPended()
        taskDetailsView.setPlanDevDetails(
            NavData.PlanDevViewModel(
                planceqname.orEmpty(),
                mOptProcess.planCode!!
            )
        )

    }


    private fun initDefault() {
        mSendParams = ProcessParams.ProcessSendParams(mProcessCode)
        taskDetailsView.initDefault()
    }

    private fun initCommView() {

        val status = ProcessModel.StatusType.getStatus(mOptProcess.opState)

        val processViewModel = NavData.ProcessViewModel(
            mOptProcess.pmname!!,
            mOptProcess.pmcode!!,
            getString(status.title),
            mOptProcess.porder!!,
            mOptProcess.ddate!!
        )
        taskDetailsView.setProcessDetails(processViewModel)

        //设置产品信息
        taskDetailsView.setProductDetails(
            NavData.ProductViewModel(
                mOptProcess.custabbname!!,
                mOptProcess.contractnum!!,
                mOptProcess.pinvcode!!,
                mOptProcess.invname!!
            )
        )
    }


    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_PM_HANDOVER -> {
                var type = PMHandoverMemberSelectFragment.getType(data)
                var name = PMHandoverMemberSelectFragment.getName(data)
                var code = PMHandoverMemberSelectFragment.getCode(data)
                var hint = if (type == PMHandoverMemberSelectFragment.TYPE_GROUP) {
                    "确定交接组名: < $name > 吗？"
                } else {
                    "确定交接人姓名: < $name > 吗？"
                }
                displayAskMsgDialog(hint, okBlock = {
                    it.dismiss()
                    when (type) {
                        PMHandoverMemberSelectFragment.TYPE_GROUP -> {
                            actionSubmitHandoverBtn(GroupType.GROUP.type, null, code)
                        }
                        else -> {
                            actionSubmitHandoverBtn(GroupType.MEMBER.type, code, null)

                        }
                    }

                })
            }
            REQUEST_DEV_CHECK -> {
                //设备点检返回
                displayAskMsgDialog("是否继续开工操作?", okBlock = {
                    it.dismiss()
                    lastSubmitDevCode?.let {
                        actionToStart(it)
                    }
                })
            }
            REJECT_CODE -> {
                //驳回信息提交成功
                popBackStack()
            }
            CaptureActivity.REQ_CODE -> {
                //扫描二维码返回
                val code =
                    data!!.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT)
                when (loadDevType) {
                    LOAD_DEV_TYPE_GET_CODE -> {
                        mGetScanningCode?.let { it(code) };
                    }
                    else -> {

                    }
                }

            }
            DEV_SELECT_CODE -> {//设备选择后的回调
                if (resultCode == RESULT_OK) {
                    val devModel = ProcessDevSelectFragment.getDevModel(data!!)
                    loadDevDetailsByCode(devModel.ceqcode!!) {
                        setPlanDevDetails(it)
                        changeSelectDev(it)
                    }

                }
            }
            EMPL_SELECT_CODE -> {//员工选择的回调
                if (resultCode == RESULT_OK) {
                    val emplModel = EmployeeSelectFragment.getEmployeeModel(data!!)
                    setSelectEmployee(emplModel)
                }

            }
            GROUP_SELECT_CODE -> {//组选择回调
                if (resultCode == RESULT_OK) {
                    val groupModel = GroupSelectFragment.getData(data!!)
                    setSelectGroup(groupModel)
                }

            }
            else -> {
                super.onFragmentResult(requestCode, resultCode, data)
            }
        }
    }

    private fun setSelectGroup(groupModel: GroupModel) {
        mSendParams.groupCode = groupModel.groupCode;
        taskDetailsView.setPlanEmployeeDetails(
            NavData.EmployeeViewModel(
                groupModel.gruopName!!,
                groupModel.groupCode!!,
                NavData.EmployeeViewModel.TYPE_GROUP
            )
        )
        taskDetailsView.setSendBtnClickable(mSendParams.isInputAll())
    }

    private fun setSelectEmployee(emplModel: EmployeeModel) {
        mSendParams.employeeCode = emplModel.code;
        taskDetailsView.setPlanEmployeeDetails(
            NavData.EmployeeViewModel(emplModel.name, emplModel.code!!)
        )
        taskDetailsView.setSendBtnClickable(mSendParams.isInputAll())
    }

    /**
     * 设置默认使用设备信息
     */
    private fun setPlanDevDetails(devModel: DevModel) {
        mPlanDevModel = devModel;
        mPlanDevCode = devModel.ceqcode
        mSendParams.planCode = devModel.ceqcode;
        taskDetailsView.setPlanDevDetails(
            NavData.PlanDevViewModel(devModel.ceqname!!, devModel.ceqcode!!)
        )
        taskDetailsView.setSendBtnClickable(mSendParams.isInputAll())
    }

    override fun onProcessPlanDevSelect() {
        startFragmentForResult(
            ProcessDevSelectFragment.instance(mOptProcess.pmcode!!),
            DEV_SELECT_CODE
        )
    }

    override fun onProcessPlanEmployeeSelect() {
        if (mPlanDevModel?.groupsList.orEmpty().isNotEmpty()) {
            changeSelectDev(mPlanDevModel!!)
        } else if (mPlanDevModel?.personnelsList.orEmpty().isNotEmpty()) {
            changeSelectDev(mPlanDevModel!!)
        } else {
            mDialogTipHelper.displayMsgDialog(msg = getString(R.string.dev_no_employee_or_group))
        }

    }

    override fun onProcessReturn() {
        startFragmentForResult(RejectMsgFragment(), REJECT_CODE)
    }

    override fun onProcessSend() {
        mDialogTipHelper.showLoading(getString(R.string.submiting))
        val subscribe = ServiceBuild.processService.sendProcess(mSendParams.toStringMap())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isFailed) {
                        throw DsException(it)
                    }
                    mDialogTipHelper.showSuccessTip(getString(R.string.submit_success))
                    taskDetailsView.postDelayed({
                        mDialogTipHelper.dismissSuccess()
                        setFragmentResult(QMUIFragment.RESULT_OK, null)
                        popBackStack()
                    }, 1_000)
                },
                {
                    it.printStackTrace()
                    activity?.runOnUiThread {
                        mDialogTipHelper.dismissLoading()
                        mDialogTipHelper.showFailTip("网络异常")
                        taskDetailsView.postDelayed(
                            { mDialogTipHelper.dismissFail() }
                            , 1_000)
                    }

                }, {
                    mDialogTipHelper.dismissLoading()
                })
        addDisposable(subscribe)
    }

    /**
     * 点击开工操作
     */
    override fun onProcessStart() {
        displayDevInputSelectDialog { code ->
            loadDevDetailsByCode(code!!) {
                if (mOptProcess.planCode != it.ceqcode) {
                    QMUIDialog.MessageDialogBuilder(context)
                        .setTitle("提示")
                        .setMessage(
                            "当前设备:${it.ceqname}(${it.ceqcode})\t\n" +
                                    "计划设备:${mOptProcess.devicetestu8Plan!!.ceqname}(${mOptProcess.devicetestu8Plan!!.ceqcode})\t\n" +
                                    "请确定是否使用当前设备进行开工操作?"
                        )
                        .addAction("取消") { dialog, index -> dialog!!.dismiss() }
                        .addAction(
                            -1, "开工", QMUIDialogAction.ACTION_PROP_NEGATIVE
                        ) { dialog, index ->
                            dialog!!.dismiss()
                            actionToStart(code)

                        }
                        .show()


                } else {
                    actionToStart(it.ceqcode!!)
                }
            }
        }
    }

    /**
     * @param code 设备的编号
     */
    private fun actionToStart(code: String) {
        mDialogTipHelper.showLoading()
        val subscribe = ServiceBuild.processService
            .startProcess(mProcessCode, code)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { baseModel ->
                    mDialogTipHelper.dismissLoading()
                    if (baseModel.isFailed) {
                        throw DsException(baseModel)
                    }
                    mDialogTipHelper.showSuccessTip("提交成功")
                    taskDetailsView.postDelayed({
                        mDialogTipHelper.dismissSuccess()
                        setFragmentResult(RESULT_OK, null)
                        popBackStack()
                    }, 1_000)
                },
                { throwable ->
                    throwable.printStackTrace()
                    runUiThread {
                        if (throwable is DsException) {
                            if (throwable.code == ResultCode.CODE_DEV_WAIT_CHECK.toString()) {
                                lastSubmitDevCode = code;
                                //当前设备未点检
                                displayAskMsgDialog(
                                    "当前设备今日未进行点检操作!必须点检完成后才可以进行其他操作!确定创建设备的点检单据吗？",
                                    okBlock = {
                                        it.dismiss()
                                        //跳转设备点检 输入
                                        val devCheckFragment =
                                            DevCheckInputFragment.instanceCreateNewOrder(code)
                                        startFragmentForResult(devCheckFragment, REQUEST_DEV_CHECK);
                                    })
                                return@runUiThread
                            }
                        }

                        mDialogTipHelper.dismissLoading()
                        mDialogTipHelper.displayMsgDialog(
                            msg = ExceptionUtils.getExceptionMessage(
                                throwable
                            )
                        )
                    }

                })
        addDisposable(subscribe)
    }


    override fun onProcessPausing() {
        displayDevInputSelectDialog { code ->
            val subscribe = Observable.create<String> {
                if (code != mOptProcess.actualCode) {
                    //检测扫描设备的编码不正确
                    throw DsException("输入设备编号不匹配")
                }
                if (code != null) {
                    mDialogTipHelper.showLoading("提交中")
                    it.onNext(code)
                }
                it.onComplete()

            }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap {

                    ServiceBuild.processService.pauseProcess(mProcessCode)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        mDialogTipHelper.dismissLoading()
                        if (it.isFailed) {
                            throw DsException(it);
                        }
                        mDialogTipHelper.showSuccessTip("提交成功")
                        taskDetailsView.postDelayed({
                            mDialogTipHelper.dismissSuccess()
                            setFragmentResult(QMUIFragment.RESULT_OK, null)
                            popBackStack()
                        }, 1_000)
                    },
                    { throwable ->
                        throwable.printStackTrace()
                        runUiThread {
                            mDialogTipHelper.dismissLoading()
                            val exceptionMessage = ExceptionUtils.getExceptionMessage(throwable)
                            mDialogTipHelper.displayMsgDialog(msg = exceptionMessage)
                        }

                    })
            addDisposable(subscribe)

        }


    }


    private fun displayDevInputSelectDialog(codeBlock: (String?) -> Unit) {
        mGetScanningCode = codeBlock;
        QMUIBottomSheet.BottomListSheetBuilder(context)
            .addItem("输入设备编号", DEV_SELECT_SHEET_TYPE_INPUT)
            .addItem("扫描设备编号", DEV_SELECT_SHEET_TYPE_SCANNING)
            .setAddCancelBtn(true)
            .setGravityCenter(true)
            .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                when (tag) {
                    DEV_SELECT_SHEET_TYPE_INPUT -> {
                        displayInputDevCodeDialog(codeBlock)
                    }
                    DEV_SELECT_SHEET_TYPE_SCANNING -> {
                        loadDevType = LOAD_DEV_TYPE_GET_CODE
                        toScanning()
                    }
                    else -> {
                    }
                }
                dialog.dismiss()
            }.build().show()
    }

    private fun displayInputDevCodeDialog(inputCode: (String) -> Unit) {
        val builder = QMUIDialog.EditTextDialogBuilder(context)
            .setPlaceholder("请输入设备编号")
        builder
            .addAction("确定") { qmuiDialog: QMUIDialog, i: Int ->
                val text = builder.editText.text
                qmuiDialog.dismiss()
                inputCode(text.toString())
            }
            .addAction("取消") { qmuiDialog: QMUIDialog, i: Int ->
                qmuiDialog.dismiss()
            }
        builder
            .create()
            .show()
    }

    override fun onProcessEnding() {
        displayDevInputSelectDialog { code ->

            val subscribe = Observable.create<String> {
                if (code != mOptProcess.actualCode) {
                    throw DsException("输入设备编号不匹配")
                }
                mDialogTipHelper.showLoading()
                it.onNext(code!!)
                it.onComplete()
            }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap {
                    ServiceBuild.processService.endProcess(mProcessCode)
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        mDialogTipHelper.dismissLoading()
                        if (it.isFailed) {
                            throw DsException(it)
                        }
                        mDialogTipHelper.showSuccessTip("提交成功")
                        setFragmentResult(QMUIFragment.RESULT_OK, null)
                        taskDetailsView.postDelayed({
                            mDialogTipHelper.dismissSuccess()
                            popBackStack()
                            startFragment(ProcessNextFragment.instance(mProcessCode))
                        }, 1_000)
                    },
                    {
                        it.printStackTrace()
                        runUiThread {
                            mDialogTipHelper.dismissLoading()
                            mDialogTipHelper.displayMsgDialog(
                                msg = ExceptionUtils.getExceptionMessage(
                                    it
                                )
                            )
                        }

                    })
            addDisposable(subscribe)

        }
    }

    override fun onClickToPlanDevDetails() {
        startFragment(DevDetailsFragment.instance(mPlanDevCode))
    }

    override fun onClickToOptDevDetails() {
        startFragment(DevDetailsFragment.instance(mOptDevCode))

    }

    override fun onProcessCancel() {
        mDialogTipHelper.showLoading("提交中")
        var disposable = ServiceBuild.processService.actionProcessCancel(mProcessCode)
            .response()
            .subscribe({
                mDialogTipHelper.dismissLoading()
                if (it.isSuccess) {
                    mDialogTipHelper.showSuccessTip("操作成功") { dialog ->
                        dialog?.dismiss()
                        popBackStack()
                    }
                }
            }, {
                it.printStackTrace()
                runUiThread {
                    mDialogTipHelper.dismissLoading()
                    mDialogTipHelper.displayMsgDialog(msg = it.getDsMsg())
                }
            })
        addDisposable(disposable)
    }

    /**
     *  点击交接按钮
     */
    override fun onProcessHandoverBtn() {
        //1.选择交接人员
        startFragmentForResult(
            PMHandoverMemberSelectFragment.instance(mOptProcess.pmcode),
            REQUEST_PM_HANDOVER
        )
        //2.提交内容
//        ServiceBuild.processService.actionHandover(mProcessCode)

    }

    /**
     * 交接内容提交
     * @param type 交接 类型 人 或者 组 0-人员 employeeCode必填，1-组 groupCode 必填
     * @param code 需要提交的 编码
     */
    private fun actionSubmitHandoverBtn(type: String?, empCode: String?, groupCode: String?) {
        showTipLoading("提交中")
        addDisposable(
            ServiceBuild.processService.actionHandover(
                mOptProcess.ccode!!,
                type,
                empCode,
                groupCode
            )
                .response()
                .subscribe({
                    cancelTipLoading()
                    showTipSuccess("提交成功") {
                        it?.dismiss()
                        popBackStack()
                    }
                }, {
                    it.printStackTrace()
                    runUiThread {
                        cancelTipLoading()
                        displayMsgDialog(it.getDsMsg())
                    }
                })
        )
    }

    override fun onScanningCodeResult(code: String?, requestCode: Int?) {
        super.onScanningCodeResult(code, requestCode)
        mGetScanningCode?.let { it(code) };
    }


    /**
     * 用于查询设备详情
     */
    private fun loadDevDetailsByCode(code: String, block: (DevModel) -> Unit) {
        mDialogTipHelper.showLoading("查询中")
        val subscribe = ServiceBuild.deviceService.getById(code)
            .response()
            .flatMap { baseModel ->
                val data = baseModel.getData(DevModel::class.java)
                Observable.create<DevModel> {
                    it.onNext(data)
                    it.onComplete()
                }
            }
            .subscribe(
                {
                    block(it)
                }, {
                    activity?.runOnUiThread {
                        mDialogTipHelper.dismissLoading()
                        showTipFailed(it.getDsMsg())
                    }
                }, {
                    mDialogTipHelper.dismissLoading()
                }
            )
        addDisposable(subscribe)
    }

    private fun changeSelectDev(it: DevModel) {
        if (it.groupsList.orEmpty().isNotEmpty()) {
            displaySelectGroup(it.groupsList.orEmpty())
        } else if (it.personnelsList.orEmpty().isNotEmpty()) {
            displaySelectEmployee(it.personnelsList.orEmpty())
        }
    }


}