package com.feiling.dasong.ui.function.group

import android.view.View
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.exception.DsException
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.GroupModel
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.uitils.ExceptionUtils
import com.ql.comm.utils.JsonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/28
 * @author ql
 */
class GroupManagerFragment : CommSelectFragment() {

    private val mGroupAdapter: GroupAdapter = GroupAdapter()
    private val mPageParams: BasePage = BasePage()

    private val mAllList: MutableList<GroupModel> by lazy { mutableListOf<GroupModel>() }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.setTitle("操作组选择")
        topbar.addLeftBackImageButton().setOnClickListener {
            popBackStack()
        }
//        topbar.addRightTextButton("添加", R.id.topbar_right_yes_button)
//            .onClick {
//                startFragmentForResult(GroupDetailsInsertFragment(), 100)
//            }
        comm_select_rv.adapter = mGroupAdapter
        comm_slt_pull_layout.setOnRefreshListener {
            mPageParams.reset()
            loadData()
        }
        comm_slt_pull_layout.visibility = View.GONE

        comm_input_group.visibility = View.GONE
        mGroupAdapter.loadMoreModule?.isAutoLoadMore = true;
        mGroupAdapter.animationEnable = true
        mGroupAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPageParams.nextPage()
            loadData()
        }


        mGroupAdapter.setOnItemClickListener { adapter, view, position ->
            val groupModel = mAllList[position]
            startFragment(GroupDetailsFragment.instance(groupModel.groupCode!!))
        }



        emptyView.showEmptyView(true)
        loadData()
    }


    private fun loadData() {
        mGroupAdapter.loadMoreModule?.isEnableLoadMore = false;
        val subscribe = ServiceBuild.groupService.loadPage(mPageParams.pageNo, mPageParams.pageSize)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (comm_slt_pull_layout.isRefreshing) {
                    comm_slt_pull_layout.isRefreshing = false;
                }
                mGroupAdapter.loadMoreModule?.isAutoLoadMore = true;
                mGroupAdapter.loadMoreModule?.isEnableLoadMore = true;

                if (it.isFailed) {
                    throw DsException(it)
                }
                emptyView.hideEmpty();
                it.getPageModel()
            }
            .flatMap {
                if (it.isFirst) {
                    mAllList.clear()
                }
                if (it.isFirst && it.isEmpty) {
                    emptyView.showEmpty("没有找到数据", "点击页面重新加载") { emptyView ->
                        emptyView.showEmptyView(true)
                        loadData()
                    }
                } else if (it.isNotLoadSize) {
                    //如果不够一页,显示没有更多数据布局
                    mGroupAdapter.loadMoreModule?.loadMoreEnd()
                } else {
                    mGroupAdapter.loadMoreModule?.loadMoreComplete()
                }
                comm_slt_pull_layout.visibility = View.VISIBLE
                Observable.fromIterable(it.list)
            }
            .map { it ->
                JsonUtils.fromJson(it, GroupModel::class.java)
            }
            .subscribe({ it ->
                mAllList.add(it);
            }, {
                it.printStackTrace()
                activity?.runOnUiThread {
                    if (comm_slt_pull_layout.isRefreshing) {
                        comm_slt_pull_layout.isRefreshing = false;
                    }
                    mGroupAdapter.loadMoreModule?.isAutoLoadMore = true;
                    mGroupAdapter.loadMoreModule?.isEnableLoadMore = true;


                    val msg: CharSequence? = ExceptionUtils.getExceptionMessage(it)
                    if (mPageParams.isFirst()) {
                        emptyView.showFailed(msg = msg) { emView ->
                            emView.showEmptyView(true)
                            loadData()
                        }
                    } else {
                        mDialogTipHelper.showFailTip(msg)
                        emptyView.postDelayed({ mDialogTipHelper.dismissFail() }, 2_000)
                    }
                    mGroupAdapter.loadMoreModule?.loadMoreFail();
                }
            }, {
                mGroupAdapter.setNewData(mAllList)
                mGroupAdapter.notifyDataSetChanged();
            })

        RxApiManager.instance.add(this, subscribe)


    }
}