package com.feiling.dasong.ui.function.arrange

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import com.feiling.dasong.DsAPI
import com.feiling.dasong.R
import com.feiling.dasong.role.Action
import com.feiling.dasong.ui.function.model.NavData
import com.feiling.dasong.uitils.LogUtils
import com.feiling.dasong.widget.ViewUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.layout.QMUIButton
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import kotlinx.android.synthetic.main.task_details.view.*
import kotlinx.android.synthetic.main.view_nav_details.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/14
 * @author ql
 */
class TaskDetailsView : RelativeLayout,
    ITaskDetailsView {

    var mTaskDetailsListener: TaskDetailsListener? = null;

    private var mSendBtn: QMUIButton? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    @SuppressLint("WrongConstant")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.task_details, this)
        ButterKnife.bind(this, view)

        task_details_process.nav_details_title_group.isClickable = false
        task_details_process.nav_details_hint.text = "";
        task_details_process.nav_details_title_arrow_iv.visibility = View.GONE


        task_details_produce.nav_details_title_group.isClickable = false
        task_details_produce.nav_details_hint.text = "";
        task_details_produce.nav_details_title_arrow_iv.visibility = View.GONE

        task_details_opt_dev_toDetails.onClick {
            mTaskDetailsListener?.onClickToOptDevDetails()
        }
        task_details_dev_toDetails.onClick {
            mTaskDetailsListener?.onClickToPlanDevDetails()
        }


    }

    override fun initDefault() {


        task_details_dev.nav_details_title.isClickable = true
        task_details_employee.nav_details_title.isClickable = true
        task_details_opt_dev.visibility = View.GONE

        task_details_dev.nav_details_title_group.setOnClickListener {
            mTaskDetailsListener?.onProcessPlanDevSelect()
        }
        task_details_dev.nav_details_hint.text = "请选择设备"
        task_details_dev.nav_details_title_arrow_iv.visibility = View.VISIBLE
        task_details_dev_empty.visibility = View.VISIBLE
        task_details_dev_toDetails.visibility = View.GONE

        task_details_employee.nav_details_hint.text = "请选择员工/组"
        task_details_employee.nav_details_title_arrow_iv.visibility = View.VISIBLE

        task_details_employee.nav_details_title_group.setOnClickListener {
            mTaskDetailsListener?.onProcessPlanEmployeeSelect();
        }
        task_details_employee_empty.visibility = View.VISIBLE

        mSendBtn = createBtn(R.string.btn_send_process)
        mSendBtn!!.setOnClickListener {
            mTaskDetailsListener?.onProcessSend();
        }
        mSendBtn!!.isEnabled = false;
        task_btn_group.addView(mSendBtn)

    }

    override fun initPended() {
        task_details_dev.nav_details_title_group.isClickable = false
        task_details_dev.nav_details_title_arrow_iv.visibility = View.GONE
        task_details_dev.nav_details_hint.text = "";
        task_details_dev_empty.visibility = View.VISIBLE
        task_details_dev_toDetails.visibility = View.GONE



        task_details_employee.nav_details_hint.text = "";
        task_details_employee.nav_details_title_arrow_iv.visibility = View.GONE
        task_details_employee.visibility = View.GONE

        task_details_opt_dev.visibility = View.GONE

        addCancelBtn()
        val startBtn = createBtn(R.string.btn_start_process)
        startBtn.setOnClickListener {
            mTaskDetailsListener?.onProcessStart();
        }
        task_btn_group.addView(startBtn)
    }

    override fun initStarted() {
        task_details_opt_dev.visibility = View.VISIBLE
        task_details_opt_dev.nav_details_title_group.isClickable = false
        task_details_opt_dev_empty.visibility = View.VISIBLE
        task_details_opt_dev_toDetails.visibility = View.GONE


        task_details_dev.nav_details_hint.text = ""
        task_details_dev.nav_details_title_arrow_iv.visibility = View.GONE
        task_details_dev.nav_details_title_group.isClickable = false
        task_details_dev_toDetails.visibility = View.GONE
        task_details_dev_empty.visibility = View.VISIBLE


        task_details_employee.nav_details_hint.text = ""
        task_details_employee.nav_details_title_arrow_iv.visibility = View.GONE
        task_details_employee.nav_details_title_group.isClickable = false
        task_details_employee.visibility = View.GONE

        addCancelBtn()
        val pauseBtn = createBtn(R.string.btn_pause_process)
        pauseBtn.setOnClickListener {
            mTaskDetailsListener?.onProcessPausing()
        }
        task_btn_group.addView(pauseBtn)

        val endBtn = createBtn(R.string.btn_end_process)
        endBtn.setOnClickListener {
            mTaskDetailsListener?.onProcessEnding()
        }
        task_btn_group.addView(endBtn)

        //交接按钮
        val handoverBtn = createBtn(R.string.btn_handover_process)
        handoverBtn.setOnClickListener {
            mTaskDetailsListener?.onProcessHandoverBtn()
        }
        task_btn_group.addView(handoverBtn)
    }

    /**
     * 询问取消加工
     */
    private fun displayAskCancelProcess() {
        QMUIDialog.MessageDialogBuilder(context)
            .setTitle(R.string.process_cancel)
            .setMessage(R.string.process_cancel_ask)
            .addAction(
                0,
                R.string.action_cancel,
                QMUIDialogAction.ACTION_PROP_POSITIVE
            ) { qmuiDialog: QMUIDialog, i: Int ->
                qmuiDialog.dismiss()
            }
            .addAction(R.string.action_ok) { qmuiDialog: QMUIDialog, i: Int ->
                qmuiDialog.dismiss()
                mTaskDetailsListener?.onProcessCancel();
            }
            .setCancelable(false)
            .show();
    }

    override fun initPaused() {
        task_details_opt_dev.visibility = View.VISIBLE
        task_details_opt_dev.nav_details_title_group.isClickable = false
        task_details_opt_dev.visibility = View.GONE

        task_details_dev.nav_details_hint.text = ""
        task_details_dev.nav_details_title_arrow_iv.visibility = View.GONE
        task_details_dev.nav_details_title_group.isClickable = false
        task_details_dev_toDetails.visibility = View.GONE
        task_details_dev_empty.visibility = View.VISIBLE


        task_details_employee.visibility = View.GONE
        task_details_employee.nav_details_hint.text = ""
        task_details_employee.nav_details_title_arrow_iv.visibility = View.GONE
        task_details_employee.nav_details_title_group.isClickable = false

        addCancelBtn()
        val pauseBtn = createBtn(R.string.btn_start_process)
        pauseBtn.setOnClickListener {
            mTaskDetailsListener?.onProcessStart();
        }
        task_btn_group.addView(pauseBtn)
    }

    private fun addCancelBtn() {
        LogUtils.d("role == ${DsAPI.role}")
        DsAPI.role?.hasPermissions(Action.PROCESS_BTN_CANCEL)?.takeIf {
            it
        }?.apply {
            if (!this) {
                return@apply
            }

            val cancelProcessBtn = ViewUtils.createLinearLayoutBtn(
                context,
                context.getString(R.string.btn_cancel_process),
                type = ViewUtils.BTN_TYPE_P
            )
            cancelProcessBtn.onClick {
                displayAskCancelProcess()
            }
            task_btn_group?.addView(cancelProcessBtn)
        }
    }

    override fun initEnded() {

        task_details_dev.nav_details_hint.text = "";
        task_details_dev.nav_details_title_arrow_iv.visibility = View.GONE
        task_details_dev.nav_details_title_group.isClickable = false;
        task_details_dev_toDetails.visibility = View.GONE
        task_details_dev_empty.visibility = View.VISIBLE



        task_btn_group.visibility = View.GONE
        task_details_opt_dev.visibility = View.GONE

        task_details_employee.visibility = View.GONE
    }


    override fun setProcessDetails(model: NavData.ProcessViewModel) {
        task_details_process.setDetailsData(model.view)
    }

    override fun setProductDetails(model: NavData.ProductViewModel) {
        task_details_produce.setDetailsData(model.view)
    }

    override fun setPlanDevDetails(model: NavData.PlanDevViewModel) {
        task_details_dev.setDetailsData(model.view)
        if (task_details_dev_empty.visibility != View.GONE) {
            task_details_dev_empty.visibility = View.GONE
        }
        if (task_details_dev_toDetails.visibility != View.VISIBLE) {
            task_details_dev_toDetails.visibility = View.VISIBLE
        }
    }

    override fun setPlanEmployeeDetails(model: NavData.EmployeeViewModel) {
        task_details_employee.setDetailsData(model.view)
        task_details_employee_empty.visibility = View.GONE

    }

    override fun setOptDevDetails(model: NavData.DevViewModel) {
        task_details_opt_dev.setDetailsData(model.view)
        if (task_details_opt_dev_empty.visibility != View.GONE) {
            task_details_opt_dev_empty.visibility = View.GONE
        }
        if (task_details_opt_dev_toDetails.visibility != View.VISIBLE) {
            task_details_opt_dev_toDetails.visibility = View.VISIBLE
        }
    }

    override fun setOptEmployeeDetails(model: NavData.EmployeeViewModel) {
    }

    override fun setSendBtnClickable(isClickable: Boolean) {
        mSendBtn?.isEnabled = isClickable;
    }

    private fun createBtn(@StringRes name: Int): QMUIButton {
        return createBtn(context.getString(name))
    }

    private fun createBtn(name: String): QMUIButton {
        val qmuiButton = QMUIButton(context)
        qmuiButton.text = name;
        val layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams.weight = 1f;
        layoutParams.leftMargin =
            context?.resources?.getDimensionPixelSize(R.dimen.activity_horizontal_margin)!!
        layoutParams.rightMargin =
            context?.resources?.getDimensionPixelSize(R.dimen.activity_horizontal_margin)!!
        qmuiButton.layoutParams = layoutParams;
        qmuiButton.setBackgroundResource(R.drawable.btn_radius)
        qmuiButton.setTextColor(ContextCompat.getColor(context!!, R.color.qmui_config_color_white))
        return qmuiButton;
    }


    open interface TaskDetailsListener {
        fun onProcessPlanDevSelect()
        fun onProcessPlanEmployeeSelect()
        fun onProcessReturn()
        fun onProcessSend()
        fun onProcessStart()
        fun onProcessPausing()
        fun onProcessEnding()

        /**
         *  查看计划设备详情
         */
        fun onClickToPlanDevDetails()

        /**
         * 查看使用设备详情
         */
        fun onClickToOptDevDetails()

        /**
         * 取消工序
         */
        fun onProcessCancel()

        /**
         * 点击交接
         */
        fun onProcessHandoverBtn()
    }


}