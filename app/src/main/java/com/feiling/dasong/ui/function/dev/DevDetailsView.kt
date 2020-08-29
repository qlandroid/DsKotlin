package com.feiling.dasong.ui.function.dev

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.feiling.dasong.R
import com.feiling.dasong.decorator.DivUtils
import com.feiling.dasong.decorator.LinearLayoutUnVerticallyManager
import com.feiling.dasong.ui.adapter.LabelTextAdapter
import com.feiling.dasong.ui.adapter.NavDetailsAdapter
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import kotlinx.android.synthetic.main.dev_details.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/21
 * @author ql
 */
class DevDetailsView : LinearLayout, IDevDetailsView {


    private val mGroupAdapter = NavDetailsAdapter()
    private val mPrincipalAdapter = NavDetailsAdapter()
    private val mAttrAdapter = LabelTextAdapter();

    var presenter: IDevDetailsPresenter? = null;

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

        val cview = LayoutInflater.from(context).inflate(R.layout.dev_details, this)
        ButterKnife.bind(this, cview)

        devDetailsAttrRv.layoutManager = object : GridLayoutManager(context, 4) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        mAttrAdapter.setGridSpanSizeLookup { gridLayoutManager, viewType, position ->
            mAttrAdapter.data[position].span
        }
        devDetailsAttrRv.adapter = mAttrAdapter

        devDetailsGroupRv.layoutManager = LinearLayoutUnVerticallyManager(context)
        devDetailsGroupRv.addItemDecoration(DivUtils.getDivDefault(context))
        devDetailsGroupRv.adapter = mGroupAdapter

        devDetailsPrincipalRv.layoutManager = LinearLayoutUnVerticallyManager(context)
        devDetailsPrincipalRv.addItemDecoration(DivUtils.getDivDefault(context))
        devDetailsPrincipalRv.adapter = mPrincipalAdapter


        mGroupAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, i: Int ->
            presenter?.actionLookGroupDetails(i);
        }
    }

    override fun setBaseDetails(models: MutableList<LabelTextModel>) {
        devDetailsBaseNdv.setDetailsData(models)
    }

    override fun setAttrs(attrs: MutableList<LabelTextModel>) {
        mAttrAdapter.setNewData(attrs)
        mAttrAdapter.notifyDataSetChanged()
    }

    override fun setGroups(groups: MutableList<NavDetailsModel>) {
        mGroupAdapter.setNewData(groups)
        mGroupAdapter.notifyDataSetChanged()
    }

    override fun setPrincipals(groups: MutableList<NavDetailsModel>) {
        mPrincipalAdapter.setNewData(groups)
        mPrincipalAdapter.notifyDataSetChanged()
    }

    override fun showGroup(isShow: Boolean) {
        devDetailsGroupNdv.visibility = if (isShow!!) View.VISIBLE else View.GONE


    }

    override fun showPrincipals(isShow: Boolean) {
        devDetailsEmplNdv.visibility = if (isShow!!) View.VISIBLE else View.GONE

    }
}