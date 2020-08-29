package com.feiling.dasong.ui.function.group

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.decorator.DividerItemDecoration2
import com.feiling.dasong.http.RxApiManager
import com.feiling.dasong.model.GroupMemberModel
import com.feiling.dasong.ui.adapter.NavDetailsAdapter
import com.feiling.dasong.ui.model.NavDetailsModel
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import io.reactivex.Observable
import kotlinx.android.synthetic.main.group_details_edit.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/28
 * @author ql
 */
class GroupDetailsView : RelativeLayout, IGroupDetailsView {

    private lateinit var unbinder: Unbinder

    lateinit var presenter: IGroupDetailsPresenter

    private var mAdapter: NavDetailsAdapter = NavDetailsAdapter()

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

        val cView = LayoutInflater.from(context).inflate(R.layout.group_details_edit, this)
        unbinder = ButterKnife.bind(cView, this)


        groupDetailsRv.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val dp2px = QMUIDisplayHelper.dp2px(context, 1)
        groupDetailsRv.addItemDecoration(
            DividerItemDecoration2(
                context,
                LinearLayoutManager.VERTICAL,
                dp2px,
                ContextCompat.getColor(context, R.color.div_6_color)
            )
        )
        groupDetailsRv.adapter = mAdapter

//        mAdapter.setOnItemClickListener { baseQuickAdapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
//            presenter.onClickRemoveMember(i)
//        }

//        groupDetailsNameLtv.onClick {
//            presenter.onClickEditGroupName()
//        }

        groupDetailsAddMemberBtn.onClick {
            presenter.onClickAddMember()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unbinder.unbind()
        RxApiManager.instance.cancel(this)
    }

    override fun setGroupName(name: String?) {
        groupDetailsNameLtv.text = name
    }

    override fun setGroupCode(code: String?) {
        groupDetailsCodeLtv.text = code
    }

    override fun setGroupMember(memberList: MutableList<GroupMemberModel>) {
        mAdapter.data.clear()
        groupDetailsMemberCountTv.text = "成员数量:${memberList.size}"
        val subscribe = Observable.fromIterable(memberList)
            .map {
                val navDetailsModel = NavDetailsModel(it.firstname, mutableListOf())
                navDetailsModel.subTitle = it.employeeCode
                navDetailsModel.leftIcon = R.drawable.icon_group_member
                navDetailsModel
            }.subscribe({
                mAdapter.addData(it)
            }, {}, {
                mAdapter.notifyDataSetChanged()
            })
        RxApiManager.instance.add(this, subscribe)
    }

    override fun getGroupName(): String? {
        return groupDetailsNameLtv.text.toString()
    }

    override fun getGroupCode(): String? {
        return groupDetailsCodeLtv.text.toString()
    }
}