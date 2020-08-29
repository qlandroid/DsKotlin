package com.feiling.dasong.ui.function.outsource

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.comm.toStringMap
import com.feiling.dasong.decorator.DividerItemDecoration2
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.model.os.OutsourceModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：委外管理，入库
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/27
 * @author ql
 */
class OutsourceInFragment : CommSelectFragment() {


    private var mPage: BasePage = BasePage()

    private var mDataList: MutableList<OutsourceModel> = mutableListOf()
    private var mParams: OutsourceModel = OutsourceModel()
    private var mClearBtn: TextView? = null

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("委外待入库")
        topbar.addLeftBackImageButton().onClick { popBackStack() }
        topbar.addRightImageButton(R.drawable.icon_scanning, R.id.topbar_right_scanning_button)
            .onClick {
                toScanning()
            }
        comm_select_rv.layoutManager = LinearLayoutManager(context)
        comm_select_rv.addItemDecoration(
            DividerItemDecoration2(
                context,
                LinearLayoutManager.VERTICAL,
                QMUIDisplayHelper.dp2px(context, 10),
                R.color.div_color
            )
        )
        comm_select_rv.adapter = mCommAdapter


        comm_slt_pull_layout.isEnabled = false

        comm_slt_pull_layout.setOnRefreshListener {
            mPage.reset()
            loadData()
        }

        mCommAdapter.setOnItemClickListener { adapter, view, position ->
            val outsourceModel = mDataList[position]
            val instance = OutsourceProcessOutDetailsFragment.instance(
                outsourceModel.ccode,
                outsourceModel.invcode,
                OutsourceProcessOutDetailsFragment.OPT_TYPE_IN
            )
            startFragment(instance)
        }

        emptyView.showEmptyView()
        loadData()
    }

    override fun onScanningCodeResult(code: String?, requestCode: Int?) {
        super.onScanningCodeResult(code, requestCode)
        mParams.invcode = code;
        mPage.reset()
        showClearBtn();
        loadData()
    }

    private fun loadData() {
        val subscribe =
            ServiceBuild.outsourceService.outsourceUninPageList(
                mPage.pageNo,
                mPage.pageSize,
                mParams.toStringMap()
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (it.isFailed) {
                        throw DsException(it)
                    }
                    it.getPageModel()
                }
                .flatMap {
                    if (it.isFirst) {
                        mDataList.clear()
                        mCommAdapter.data.clear()
                    }
                    if (it.isFirst && !it.isEmpty) {
                        if (!comm_slt_pull_layout.isEnabled) {
                            comm_slt_pull_layout.isEnabled = true
                        }

                    }

                    if (it.isFirst && it.isEmpty) {

                    } else if (it.isFirst && it.isNotLoadSize) {
                        mCommAdapter.loadMoreModule?.loadMoreEnd()
                    } else if (!it.isFirst && it.isNotLoadSize) {
                        mCommAdapter.loadMoreModule?.loadMoreEnd()
                    }
                    Observable.fromIterable(it.list)
                }
                .map {
                    JsonUtils.toJson(it)
                }
                .map {
                    JsonUtils.fromJson(it, OutsourceModel::class.java)
                }.map {
                    mDataList.add(it)
                    val navDetailsModel = NavDetailsModel()
                    navDetailsModel.title = it.suppname
                    navDetailsModel.subTitle = it.suppcode
                    navDetailsModel.children = mutableListOf(
                        LabelTextModel("委外单号", it.ccode.orEmpty()),
                        LabelTextModel("存货名称", it.invname.orEmpty()),
                        LabelTextModel("存货编号", it.invcode.orEmpty()),
                        LabelTextModel("合同号", it.conno.orEmpty())
                    )

                    navDetailsModel
                }
                .subscribe(
                    {
                        mCommAdapter.addData(it)
                    },
                    {
                        it.printStackTrace()
                        topbar?.post {
                            val msg = ExceptionUtils.getExceptionMessage(it)
                            if (mDataList.isEmpty()) {
                                emptyView.showFailed(msg = msg) { emptyView ->
                                    emptyView.showEmptyView(true)
                                    loadData()
                                }
                            } else {
                                mCommAdapter.loadMoreModule?.loadMoreFail()
                            }
                        }
                    },
                    {
                        if (comm_slt_pull_layout.isRefreshing) {
                            comm_slt_pull_layout.isRefreshing = false
                        }

                        if (mDataList.isEmpty()) {
                            emptyView.showEmpty (){
                                it.showEmptyView()
                                loadData()
                            }
                        } else {
                            if (emptyView.isShowing) {
                                emptyView.hideEmpty()
                            }
                        }
                        mCommAdapter.notifyDataSetChanged()

                    }
                )
        addDisposable( subscribe)
    }

    private fun showClearBtn() {

        if (mClearBtn == null) {
            var tv: TextView = TextView(context)
            tv.text = "显示全部"
            tv.id = R.id.btn_clear
            tv.gravity = Gravity.CENTER
            val top = QMUIDisplayHelper.dp2px(context, 10)
            tv.setPadding(top, top, top, top)
            tv.setBackgroundResource(R.drawable.btn_radius)
            tv.isClickable = true
            tv.isFocusable = true
            tv.setTextColor(Color.WHITE)
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            layoutParams.gravity = Gravity.BOTTOM
            layoutParams.bottomMargin = QMUIDisplayHelper.dp2px(context, 16)
            layoutParams.rightMargin = QMUIDisplayHelper.dp2px(context, 16)
            layoutParams.leftMargin = QMUIDisplayHelper.dp2px(context, 16)
            tv.layoutParams = layoutParams
            mClearBtn = tv
            rootView.addView(tv)
        }

        if (mClearBtn?.visibility != View.VISIBLE) {
            mClearBtn?.visibility = View.VISIBLE
        }

        mClearBtn?.onClick {
            it.visibility = View.GONE
            mParams = OutsourceModel()
            this.loadData()
        }

    }
}