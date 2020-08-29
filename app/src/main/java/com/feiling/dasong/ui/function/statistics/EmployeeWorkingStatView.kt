package com.feiling.dasong.ui.function.statistics

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.decorator.DivUtils
import com.feiling.dasong.decorator.LinearLayoutUnVerticallyManager
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.ui.adapter.NavDetailsAdapter
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.widget.EmptyView
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.statistics_employee_working.view.*


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/16
 * @author ql
 */
class EmployeeWorkingStatView : RelativeLayout, IEmployeeWorkingStatView {

    private var mProcessAdapter: NavDetailsAdapter = NavDetailsAdapter();

    var presenter: IEmployeeWorkingStatPresenter? = null;

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
            LayoutInflater.from(context).inflate(R.layout.statistics_employee_working, this)
        ButterKnife.bind(inflate, this)

        statEmployeeWorkingProcessRv.layoutManager = LinearLayoutUnVerticallyManager(context);
        statEmployeeWorkingProcessRv.addItemDecoration(DivUtils.getDivDefault(context,3))
        statEmployeeWorkingProcessRv.adapter = mProcessAdapter
        mProcessAdapter.loadMoreModule?.setOnLoadMoreListener {
        }

        statEmployeeWorkingStartBtn.onClick {
            presenter?.actionSelectStartDate()
        }
        statEmployeeWorkingEndBtn.onClick {
            presenter?.actionSelectEndDate()
        }

        statEmployeeWorkingAuditBtn.onClick {
            presenter?.actionAuditAll();
        }
        var emptyView = EmptyView(context)
        emptyView.showEmpty();
        mProcessAdapter.setEmptyView(emptyView)

        mProcessAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            presenter?.actionAuditToPosition(i)
        }


    }

    override fun setUserDetails(list: MutableList<LabelTextModel>) {
        statEmployeeWorkingUserNdv.setDetailsData(list)
    }

    override fun setProcessList(list: MutableList<NavDetailsModel>) {
        mProcessAdapter.setNewData(list)
        mProcessAdapter.notifyDataSetChanged()

        mProcessAdapter.loadMoreModule?.loadMoreEnd()

    }


    override fun setTotalWorkingHour(totalWorkingTimer: Double) {
        statEmployeeWorkingTotalNdv.text = String.format("%.0f", totalWorkingTimer)
    }

    override fun setStartDate(startDate: String) {
        statEmployeeWorkingStartDateTv.text = startDate;
    }

    override fun setEndDate(endDate: String) {
        statEmployeeWorkingEndDateTv.text = endDate;
    }

    override fun setShowAudit(isShow: Boolean) {
        statEmployeeWorkingBottomGroup.visibility = if (isShow) View.VISIBLE else View.GONE
    }


    override fun onDetachedFromWindow() {
        RxApiManager.cancel(this)
        super.onDetachedFromWindow()
    }


}