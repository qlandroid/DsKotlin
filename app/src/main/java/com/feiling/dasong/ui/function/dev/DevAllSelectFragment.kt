package com.feiling.dasong.ui.function.dev

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.C
import com.feiling.dasong.R
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.model.DevModel
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.uitils.ExceptionUtils
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.layout.QMUIFrameLayout
import com.qmuiteam.qmui.util.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/18
 * @author ql
 */
class DevAllSelectFragment : CommSelectFragment() {
    private val mAllList: MutableList<DevModel> = mutableListOf<DevModel>()
    private val mPage: BasePage = BasePage()
    private val mDevQueryParams: DevQueryParams = DevQueryParams()
    private var mDevQueryView: DevQueryView? = null

    companion object {
        private const val KEY_CODE = "data"
        fun getData(data: Intent?): DevModel? {
            val json = data?.getStringExtra(KEY_CODE)
            return JsonUtils.fromJson(json, DevModel::class.java)
        }
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("设备选择")

        mCommAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            val intent = Intent()
            intent.putExtra(KEY_CODE, C.sGson.toJson(mAllList[i]))
            setFragmentResult(RESULT_OK, intent)
            popBackStack()
        }
        comm_slt_pull_layout.setOnRefreshListener {
            mPage.reset()
            loadData()
        }

        mCommAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPage.nextPage()
            this.loadData()
        }
        topbar.addRightTextButton("查询", R.id.topbar_right_search_button)
            .onClick { showDevQueryView() }
        emptyView.showEmptyView()
        loadData()


    }

    private fun showDevQueryView() {
        val view = rootView.findViewById<View>(R.id.dev_query_view)
        if (view == null) {
            val qmuiFrameLayout = QMUIFrameLayout(context)
            qmuiFrameLayout.id = R.id.dev_query_view
            mDevQueryView = DevQueryView(context!!)
            mDevQueryView?.onDevQueryListener = object : DevQueryView.OnDevQueryListener {
                override fun onQuery(query: DevQueryView.DevQuery) {
                    val view = rootView.findViewById<View>(R.id.dev_query_view)
                    view.visibility = View.GONE

                    mDevQueryParams.name =
                        if (query.devName.orEmpty().isEmpty()) null else query.devName
                    mDevQueryParams.code =
                        if (query.devCode.orEmpty().isEmpty()) null else query.devCode
                    loadData()
                }
            }

            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.topMargin =
                context?.resources?.getDimensionPixelSize(R.dimen.qmui_topbar_height)!! + QMUIStatusBarHelper.getStatusbarHeight(
                    context
                )
            mDevQueryView?.background =
                QMUIResHelper.getAttrDrawable(context, R.attr.qmui_skin_support_popup_bg)

            mDevQueryView?.layoutParams = layoutParams;
            mDevQueryView?.setPadding(
                mDevQueryView!!.left,
                mDevQueryView!!.top,
                mDevQueryView!!.right,
                QMUIDisplayHelper.dp2px(context, 100)
            )

            qmuiFrameLayout.addView(mDevQueryView)
            qmuiFrameLayout.isClickable = true
            qmuiFrameLayout.onClick {
                it.visibility = View.GONE
            }
            qmuiFrameLayout.setBackgroundResource(R.color.pop_bg_color)
            rootView.addView(
                qmuiFrameLayout,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        } else {
            view.visibility = View.VISIBLE
        }

    }


    private fun loadData() {
        comm_slt_pull_layout.isEnabled = false
        mCommAdapter.loadMoreModule?.isEnableLoadMore = false
        val subscribe = ServiceBuild.deviceService.getPageList(
            mPage.pageNo,
            mPage.pageSize,
            mDevQueryParams.code,
            mDevQueryParams.name
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { t ->
                if (t.isFailed) {
                    throw DsException(t)
                }
                if (comm_slt_pull_layout.isRefreshing) {
                    mCommAdapter.data.clear()
                    mAllList.clear()
                }
                t.getPageModel()
            }
            .flatMap {
                if (it.isNotLoadSize) {
                    mCommAdapter.loadMoreModule?.loadMoreEnd()
                }
                if (it.isFirst) {
                    mCommAdapter.data.clear()
                    mAllList.clear()
                }
                Observable.fromIterable<Any?>(it.list)
            }
            .map { t ->
                C.sGson.fromJson(C.sGson.toJson(t), DevModel::class.java)
            }
            .subscribe({ devModel ->
                mAllList.add(devModel)
                mCommAdapter.addData(
                    NavDetailsModel(
                        devModel.ceqname,
                        mutableListOf(
                            LabelTextModel("设备编号", devModel.ceqcode!!)
                        )
                    )
                )
            }, {
                it.printStackTrace()
                topbar?.post {
                    if (comm_slt_pull_layout.isRefreshing) {
                        comm_slt_pull_layout.isRefreshing = false;
                    }
                    if (mPage.isFirst()) {
                        emptyView.showFailed(msg = ExceptionUtils.getExceptionMessage(it)) { empty ->
                            empty.showEmptyView()
                            loadData()
                        }
                    } else {
                        mDialogTipHelper.displayMsgDialog(
                            msg = ExceptionUtils.getExceptionMessage(
                                it
                            )
                        )
                        mCommAdapter.loadMoreModule?.loadMoreFail()
                        mCommAdapter.notifyDataSetChanged()
                    }

                }
            }, {
                if (emptyView.isShowing) {
                    emptyView.hideEmpty()
                }
                if (mAllList.size == 0) {
                    emptyView.showEmpty {
                        it.showEmptyView()
                        loadData()
                    }
                } else {
                    comm_slt_pull_layout.visibility = View.VISIBLE

                    if (comm_slt_pull_layout.isRefreshing) {
                        comm_slt_pull_layout.isRefreshing = false;
                    }

                    mCommAdapter.notifyDataSetChanged()
                }
                comm_slt_pull_layout.isEnabled = true
                mCommAdapter.loadMoreModule?.isEnableLoadMore = true

            })

        RxApiManager.instance.add(this, subscribe)
    }

}