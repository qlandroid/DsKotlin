package com.feiling.dasong.ui.function.statistics

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import com.feiling.dasong.R
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.ui.model.LabelTextModel
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.stat_process_working_details.view.*


/**
 * 描述：查询指定人的指定工序详细信息，工段长有权权限进行修改 审核工时
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/16
 * @author ql
 */
class StatProcessWorkingDetailsView : ScrollView, IStatProcessWorkingDetailsView {


    var presenter: IStatProcessWorkingDetailsPresenter? = null;

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        var inflate =
            LayoutInflater.from(context).inflate(R.layout.stat_process_working_details, this)
        ButterKnife.bind(inflate, this)


        statWorkingDetailsAuditBtn.onClick {
            var timer = statWorkingDetailsAuditTimerEt.text.toString()
            presenter?.actionAudit(timer)
        }
        statWorkingDetailsProcessNdv.setNavTitleClickListener {
            presenter?.actionToProcessDetails();
        }
        setShowAudit(false)
    }


    override fun onDetachedFromWindow() {
        RxApiManager.cancel(this)
        super.onDetachedFromWindow()
    }


    override fun setProcessDetails(details: MutableList<LabelTextModel>) {
        statWorkingDetailsProcessNdv.setDetailsData(details)
    }

    override fun setUserDetails(details: MutableList<LabelTextModel>) {
        statWorkingDetailsUserNdv.setDetailsData(details)
    }

    override fun setPlanWorkingTimer(timer: String) {
        statWorkingDetailsPlanTimerTv.text = timer;
    }

    override fun setShowAudit(isShow: Boolean) {
        var visibility = if (isShow) View.VISIBLE else View.GONE;
        statWorkingDetailsAuditBtn.visibility = visibility
        statWorkingDetailsAuditTimerGroup.visibility = visibility;
    }

    override fun setAuditStatus(auditStatue: String) {
        when (auditStatue) {
            "0" -> {
                var color = ContextCompat.getColor(context, R.color.app_color_red)
                statWorkingDetailsStatusTag.setBgColor(color)
                statWorkingDetailsStatusTag.text = "未审核"
            }
            "1" -> {
                var color = ContextCompat.getColor(context, R.color.green_0)
                statWorkingDetailsStatusTag.setBgColor(color)
                statWorkingDetailsStatusTag.text = "已审核"
            }
            else -> {
            }
        }

    }

    override fun setAuditWorkingTimer(auditTimer: String) {
        statWorkingDetailsAuditTimerEt.setText(auditTimer)
    }


}