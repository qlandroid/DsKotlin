package com.feiling.dasong.ui.function.statistics

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.EmployeeModel
import com.feiling.dasong.model.base.ResponseModel
import com.feiling.dasong.model.process.ProcessTimeStatisticsModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.DateUtils
import com.feiling.dasong.uitils.ExceptionUtils
import com.feiling.dasong.uitils.LoginUtils
import com.feiling.dasong.uitils.StringUtils
import com.google.gson.reflect.TypeToken
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_employee_working_stat.*
import java.math.BigDecimal
import java.util.*

/**
 * 描述： 员工工时统计详情
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/16
 * @author ql
 */
class EmployeeWorkingStatisticsFragment : BaseFragment() {

    private lateinit var mView: IEmployeeWorkingStatView

    private data class Params(
        var userCode: String?,
        var startDate: String? = null,
        var endDate: String? = null
    )

    private lateinit var mParams: Params;
    private var mProcessTimerList: MutableList<ProcessTimeStatisticsModel>? = null;

    companion object {
        public const val KEY_CODE = "KEY-CODE"
        /**
         * @param code  员工的编码
         */
        fun instance(code: String): EmployeeWorkingStatisticsFragment {
            var bundle = Bundle()
            bundle.putString(KEY_CODE, code)

            var employeeWorkingStatisticsFragment = EmployeeWorkingStatisticsFragment()
            employeeWorkingStatisticsFragment.arguments = bundle;

            return employeeWorkingStatisticsFragment;
        }
    }


    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_employee_working_stat, null)
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)

        topbar.addLeftBackImageButton().onClick { popBackStack() }
        topbar.setTitle("工时统计")

        var employeeCode = arguments?.getString(KEY_CODE)
        mParams = Params(employeeCode!!);

        mView = employeeWorkingStatView
        employeeWorkingStatView.presenter = Presenter();

        var dayEnd = DateUtils.getDayEndByDate(Date()).time
        var endDate =
            DateUtils.replaceYYYY_MM_dd_HHmmss(dayEnd);
        var dayStart = DateUtils.getFirstDayStartFormMonthByDate(Date()).time
        var startDate =
            DateUtils.replaceYYYY_MM_dd_HHmmss(dayStart)
        mParams.startDate = "$startDate "
        mView.setStartDate(DateUtils.replaceYYYYMMdd(dayStart)!!)
        mParams.endDate = "$endDate"
        mView.setEndDate(DateUtils.replaceYYYYMMdd(dayEnd)!!)


        LoginUtils.getUserModel(context)?.let {
            mView.setShowAudit(it.isSectionChief)
        }


        loadEmployeeDetailsByCode(employeeCode.orEmpty());


    }

    private fun loadEmployeeDetailsByCode(code: String) {
        emptyView.showEmptyView();
        var subscribe = ServiceBuild.employeeService.loadEmployeeDetailsByCode(code)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (it.isFailed) {
                    throw DsException(it)
                }
                it.getData(EmployeeModel::class.java)
            }
            .map {
                //用户信息
                mutableListOf<LabelTextModel>(
                    LabelTextModel("姓名", it.firstname.orEmpty()),
                    LabelTextModel("编号", it.code.orEmpty())
                )
            }
            .subscribe({
                mView.setUserDetails(it);
                loadProcessWorkingRecode(mParams.userCode!!, mParams.startDate, mParams.endDate)
            }, {
                it.printStackTrace()
                runUiThread {
                    mDialogTipHelper.displayMsgDialog(msg = ExceptionUtils.getExceptionMessage(it))
                }
            })
        addDisposable(subscribe)
    }

    /**
     * 查询用户所有工时
     */
    private fun loadProcessWorkingRecode(code: String, startDate: String?, endDate: String?) {
        mDialogTipHelper.showLoading("加载中")
        var list = mutableListOf<NavDetailsModel>()
        var subscribe =
            ServiceBuild.processService.getEmployeeWorkingRecode(code, startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (it.isFailed) {
                        throw DsException(it)
                    }
                    it.getData(MutableMap::class.java)
                }
                .map {
                    val any = it["whsList"]
                    val json = JsonUtils.toJson(any);
                    val type =
                        object : TypeToken<MutableList<ProcessTimeStatisticsModel>>() {}.type;
                    val totalWorkingTimer = it["zgs"] as Double
                    mView.setTotalWorkingHour(totalWorkingTimer);
                    C.sGson.fromJson<MutableList<ProcessTimeStatisticsModel>>(json, type)
                }
                .flatMap {
                    mProcessTimerList = it;
                    Observable.fromIterable(it)
                }
                .map {
                    var nvdm = NavDetailsModel()

//                    nvdm.leftIcon = R.drawable.icon_audit_success
                    //工序名称
                    nvdm.title = it.opProcess?.pmname
                    //工序编号
                    nvdm.subTitle = it.opProcess?.pmcode
                    var audit = "未审核"
                    nvdm.showTag = true;

                    var workingTimer = it.srcTimer.orEmpty()
                    if (it.vstatus == "1") {
                        audit = "已审核"
                        workingTimer = it.repairTimer.orEmpty()
                        nvdm.tagBackgroundColor =
                            ContextCompat.getColor(context!!, R.color.audit_ok)

                    } else {
                        nvdm.tagBackgroundColor =
                            ContextCompat.getColor(context!!, R.color.audit_un)
                    }
                    nvdm.tagText = audit;

                    var toDoubleOrNull = workingTimer.toDouble()
                    var sec: BigDecimal
                    sec = BigDecimal(toDoubleOrNull)
                    //审核状态
                    nvdm.hint = "${sec}\t分钟"
                    nvdm.hintColor = ContextCompat.getColor(context!!, R.color.app_color_red)
                    nvdm.children = getProcessChildren(it)


                    //需要显示的内容
                    nvdm
                }
                .subscribe({
                    list.add(it)
                }, {
                    it.printStackTrace()
                    runUiThread {
                        mDialogTipHelper.dismissLoading()
                        emptyView.showFailed(
                            msg = ExceptionUtils.getExceptionMessage(
                                it
                            )
                        ) {
                            loadEmployeeDetailsByCode(mParams.userCode!!)
                        }
                    }
                }, {
                    mView.setProcessList(list)
                    emptyView.hideEmpty()
                    mDialogTipHelper.dismissLoading()
                })
        addDisposable(subscribe)
    }

    private fun getProcessChildren(it: ProcessTimeStatisticsModel): MutableList<LabelTextModel>? {
        return mutableListOf(
            LabelTextModel("合同号", it.opProcess?.contractnum),
            LabelTextModel("产品编码", it.opProcess?.pinvcode),
            LabelTextModel("订单号", it.opProcess?.porder)
        )
    }

    fun displayDateSelect(title: String?, block: (Date) -> Unit) {
        val selectedDate: Calendar = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        //startDate.set(2013,1,1);
        //startDate.set(2013,1,1);
        val endDate: Calendar = Calendar.getInstance()
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        //endDate.set(2020,1,1);
//正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1)
        endDate.set(2020, 11, 31)

        var pvTime = TimePickerBuilder(context,
            OnTimeSelectListener { date, v ->
                //选中事件回调
                block(date)
            })
            .setType(booleanArrayOf(true, true, true, false, false, false)) // 默认全部显示
            .setCancelText("取消") //取消按钮文字
            .setSubmitText("确定") //确认按钮文字
            .setTitleSize(20) //标题文字大小
            .setTitleText(title) //标题文字
            .isCyclic(true) //是否循环滚动
            .setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
            .setRangDate(startDate, endDate) //起始终止年月日设定
            .setLabel("年", "月", "日", "时", "分", "秒") //默认设置为年月日时分秒
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .build()
        pvTime.show();
    }

    inner class Presenter : IEmployeeWorkingStatPresenter {
        override fun actionSelectStartDate() {
            displayDateSelect("开始时间") {
                var dayEndByDate = DateUtils.getDayStartByDate(it)
                var replaceyyyyMmDdHhmmss = DateUtils.replaceYYYY_MM_dd_HHmmss(dayEndByDate.time)
                mParams.startDate = replaceyyyyMmDdHhmmss
                mView.setStartDate(DateUtils.replaceYYYYMMdd(it.time).orEmpty())



                loadProcessWorkingRecode(
                    mParams.userCode!!,
                    mParams.startDate,
                    mParams.endDate
                )
            }
        }

        override fun actionSelectEndDate() {
            displayDateSelect("结束时间") {

                var dayEndByDate = DateUtils.getDayEndByDate(it)
                var replaceyyyyMmDdHhmmss = DateUtils.replaceYYYY_MM_dd_HHmmss(dayEndByDate.time)
                mView.setEndDate(DateUtils.replaceYYYYMMdd(it.time).orEmpty())
                mParams.endDate = replaceyyyyMmDdHhmmss;

                loadProcessWorkingRecode(
                    mParams.userCode!!,
                    mParams.startDate,
                    mParams.endDate
                )
            }
        }

        override fun actionAuditAll() {
            QMUIDialog.MessageDialogBuilder(context)
                .setTitle("提示")
                .setMessage("确定要批量审核吗？")
                .addAction("确定") { qmuiDialog: QMUIDialog, i: Int ->
                    qmuiDialog.dismiss()
                    actionSubmitAll()
                }
                .addAction("取消") { qmuiDialog: QMUIDialog, i: Int ->
                    qmuiDialog.dismiss()
                }
                .show();
        }

        private fun actionSubmitAll() {
            mDialogTipHelper.showLoading("提交中")
            var subscribe = Observable.fromIterable(mProcessTimerList)
                .filter {
                    //没有审核
                    !it.isAuditWorkingTimer
                }
                .flatMap {
                    submitAuditWorkingTimer(
                        it.ccode!!,
                        StringUtils.replaceNumber(it.srcTimer.orEmpty()).toInt()
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
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
                    }, {
                        mDialogTipHelper.dismissLoading()
                        mDialogTipHelper.showSuccessTip("提交成功", topbar) {
                            it?.dismiss()
                        }
                        loadProcessWorkingRecode(
                            mParams.userCode!!,
                            mParams.startDate,
                            mParams.endDate
                        )
                    }
                )
            addDisposable(subscribe)
        }

        private fun submitAuditWorkingTimer(
            code: String,
            workingTimer: Int
        ): Observable<ResponseModel> {
            return ServiceBuild.processService.actionWorkingHourFormProcess(
                code,
                workingTimer
            );
        }


        override fun actionAuditToPosition(position: Int) {
            var processTimeStatisticsModel = mProcessTimerList?.get(position)
            var instance =
                processTimeStatisticsModel?.ccode?.let {
                    StatProcessWorkingDetailsFragment.instance(
                        it
                    )
                }
            startFragment(instance)

        }
    }


}