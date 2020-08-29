package com.feiling.dasong.ui.comm

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.feiling.dasong.R
import com.feiling.dasong.comm.BaseFragment
import com.feiling.dasong.comm.CommSelectFragment
import com.feiling.dasong.comm.getDsMsg
import com.feiling.dasong.comm.response
import com.feiling.dasong.http.ServiceBuild
import com.feiling.dasong.model.InventoryModel
import com.feiling.dasong.model.base.BasePage
import com.feiling.dasong.ui.adapter.CommInventorySelectAdapter
import com.feiling.dasong.ui.model.InventorySelectModel
import com.feiling.dasong.uitils.DialogUtils
import com.feiling.dasong.widget.CustomToast
import com.ql.comm.utils.JsonUtils
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_comm_select.*
import java.util.ArrayList

/**
 * 描述：存货基础档案 选择
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/28
 * @author ql
 */
class CommInventorySelectFragment : CommSelectFragment() {

    private val mAdapter = CommInventorySelectAdapter()
    private val mPage: BasePage = BasePage()
    private val mQueryParams: MutableMap<String, String> = mutableMapOf()

    private val mInvList: MutableList<InventoryModel> = mutableListOf()

    private val mSelectList: MutableList<InventoryModel> = mutableListOf()

    companion object {
        const val RESULT_KEY_LIST = "RESULT_KEY_LIST"
        fun getList(data: Intent?): List<String>? {
            return data?.getStringArrayListExtra(RESULT_KEY_LIST)?.toList()
        }
    }

    /**
     * nav OkBtn
     */
    private var mTopBarRightOkBtn: Button? = null;

    @Suppress("UNCHECKED_CAST")
    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        mTopBarRightOkBtn = topbar.addRightTextButton("确定(0)", R.id.topbar_right_yes_button)
        mTopBarRightOkBtn?.onClick {
            if (mSelectList.isEmpty()) {
                DialogUtils.displayAskMsgDialog(context, "当前没有选择配件,是否要退出当前页面?", okBlock = {
                    it.dismiss()
                    popBackStack()
                })
                return@onClick
            }
            val list = arrayListOf<String>()
            mSelectList.forEach { inventoryModel ->
                list.add(inventoryModel.invcode!!)
            }
            var data = Intent()
            data.putStringArrayListExtra(RESULT_KEY_LIST, list)
            setFragmentResult(QMUIFragment.RESULT_OK, data)
            popBackStack()
        }
        setTitle("存货选择")
            .setShowInputGroup(true, "请输入配件名称/编号/规格进行查询")
            .setOpenRefresh(false)
            .addLeftBack()
            .setSearchBtnVisible(true)
            .setOnSearchBtnClick {
                var queryStr = inputText.toString()
                mQueryParams["query"] = queryStr
                QMUIKeyboardHelper.hideKeyboard(comm_search_et)
                if (mSelectList.isNotEmpty()) {
                    DialogUtils.displayAskMsgDialog(
                        context,
                        "当前已经选择了配件,如果继续查找的话,将清除已选择的配件!确定要继续操作吗?",
                        okBlock = {
                            it.dismiss()
                            mSelectList.clear()
                            reloadData()
                            changeSelected()
                        })
                    return@setOnSearchBtnClick
                }
                reloadData()
            }
            .setAdapter(mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
        mPage.reset()


        mAdapter.setOnItemClickListener { adapter, view, position ->
            var model = adapter.data[position] as InventorySelectModel
            model.selected = !model.selected
            adapter.notifyItemChanged(position)
            if (model.selected) {
                mSelectList.add(mInvList[position])
            } else {
                mSelectList.remove(mInvList[position])
            }
            changeSelected()
        }
        mAdapter.loadMoreModule?.setOnLoadMoreListener {
            mPage.nextPage()
            loadData()
        }

        loadData()
    }

    private fun changeSelected() {
        mTopBarRightOkBtn?.text = "确定(${mSelectList.size})"
    }

    private fun reloadData() {
        showLoading()
        mPage.reset()
        loadData()
    }


    private fun loadData() {
        addDisposable(ServiceBuild.commService.getInventoryPage(
            mPage.pageNo,
            mPage.pageSize,
            mQueryParams
        )
            .response()
            .flatMap {
                setIsRefresh(false)
                hideEmpty()
                var pageModel = it.getPageModel()
                if (pageModel.isFirst) {
                    mAdapter.data.clear()
                    mInvList.clear()
                    mSelectList.clear()
                }
                if (pageModel.isNotLoadSize) {
                    mAdapter.loadMoreModule?.loadMoreEnd()
                }
                Observable.fromIterable(pageModel.list)

            }
            .map {
                JsonUtils.fromJson(it, InventoryModel::class.java)
            }
            .map {
                mInvList.add(it)
                //转成InventorySelectModel
                var inventorySelectModel =
                    InventorySelectModel(it.invcname, it.invcode, it.invstd, false)
                inventorySelectModel
            }
            .subscribe({
                mAdapter.addData(it)
            }, {
                it.printStackTrace()
                runUiThread {
                    if (mPage.isFirst()) {
                        setIsRefresh(false)
                        showFailed(it.getDsMsg()) { emptyView ->
                            emptyView.showEmptyView(true)
                        }
                        return@runUiThread
                    }
                    mAdapter.loadMoreModule?.isEnableLoadMore = true;
                    mAdapter.loadMoreModule?.loadMoreFail()
                    mAdapter.notifyDataSetChanged()
                    CustomToast.show(context!!, it.getDsMsg().toString())
                }
            }, {
                mAdapter.loadMoreModule?.loadMoreComplete()
                mAdapter.notifyDataSetChanged()
            })
        )
    }

    data class SelectModel(var name:String,var code:String):Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(code)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SelectModel> {
            override fun createFromParcel(parcel: Parcel): SelectModel {
                return SelectModel(parcel)
            }

            override fun newArray(size: Int): Array<SelectModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}