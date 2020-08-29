package com.feiling.dasong.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.DsAPI
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.decorator.GridDividerItemDecoration
import com.feiling.dasong.model.QDItemDescription
import com.feiling.dasong.ui.HomeActivity
import com.feiling.dasong.ui.function.arrange.ArrangeWorkFragment
import com.feiling.dasong.ui.function.dev.DeviceListFragment
import com.feiling.dasong.ui.function.devcheck.DevCheckListFragment
import com.feiling.dasong.ui.function.devmaintenance.DeviceMaintenanceListFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairApplyFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairListFragment
import com.feiling.dasong.ui.function.devrepair.DeviceRepairVerifyListFragment
import com.feiling.dasong.ui.function.quality.ProcessQualityFragment
import com.feiling.dasong.ui.function.outsource.OutsourceInFragment
import com.feiling.dasong.ui.function.outsource.OutsourceOutFragment
import com.feiling.dasong.ui.function.statistics.EmplWorkingStatListFragment
import com.feiling.dasong.ui.function.statistics.EmployeeWorkingStatisticsFragment
import com.feiling.dasong.ui.function.statistics.WorkHourPageFragment
import com.feiling.dasong.uitils.LoginUtils
import com.qmuiteam.qmui.widget.QMUITopBarLayout

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/2
 * @author ql
 */
class HomeFunctionController(context: Context) : HomeCommController(context) {

    @BindView(R.id.topbar)
    lateinit var mTopBar: QMUITopBarLayout
    @BindView(R.id.recyclerView)
    lateinit var mRecyclerView: RecyclerView

    override fun bindView(context: Context, layoutInflater: LayoutInflater): View? {
        return LayoutInflater.from(this.context).inflate(R.layout.home_all_function, this);
    }

    override fun initWeight() {
        super.initWeight()
        initTopBar();
        initRecyclerView();
    }

    private fun initTopBar() {
        mTopBar.setTitle("全部功能")
    }

    private fun initRecyclerView() {
        val spanCount = 3
        mRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
        mRecyclerView.addItemDecoration(GridDividerItemDecoration(context, spanCount))

        var itemAdapter: ItemAdapter = ItemAdapter();

        mRecyclerView.adapter = itemAdapter;
        var functionAll = getFunctionAll();
        itemAdapter.setNewData(functionAll);
        itemAdapter.setOnItemClickListener { adapter, view, position ->
            val qdItemDescription = adapter.data[position] as QDItemDescription
            if (qdItemDescription.demoClass == ArrangeWorkFragment::class.java) {

            }
            val fragment = qdItemDescription.demoClass.newInstance();

            if (fragment is EmployeeWorkingStatisticsFragment) {
                var bundle = Bundle()
                var userModel = LoginUtils.getUserModel(context)
                bundle.putString(EmployeeWorkingStatisticsFragment.KEY_CODE, userModel?.empcode)
                var of =
                    HomeActivity.of(context, EmployeeWorkingStatisticsFragment::class.java, bundle)
                mHomeControllerListener.startActivity(of!!)
            } else if (fragment is BaseFragment) {
                val intent = HomeActivity.of(context, qdItemDescription.demoClass)
                mHomeControllerListener.startActivity(intent!!);
            }
        }

    }

    private fun getFunctionAll(): MutableList<QDItemDescription>? {
        return DsAPI.role?.getMenus();
    }

    internal class ItemAdapter() :
        BaseQuickAdapter<QDItemDescription, BaseViewHolder>(R.layout.home_item_layout) {

        override fun convert(helper: BaseViewHolder, item: QDItemDescription) {
            helper.setText(R.id.item_name, item.name)
            if (0 != item.iconRes) {
                helper.setImageResource(R.id.item_icon, item.iconRes)
            }

        }
    }


}