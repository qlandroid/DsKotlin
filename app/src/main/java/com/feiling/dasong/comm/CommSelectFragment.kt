package com.feiling.dasong.comm

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.feiling.dasong.R
import com.feiling.dasong.decorator.DivUtils
import com.feiling.dasong.ui.adapter.NavDetailsAdapter
import com.feiling.dasong.widget.EmptyView
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import kotlinx.android.synthetic.main.fragment_comm_select.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
open class CommSelectFragment : BaseFragment() {

    open val mCommAdapter: NavDetailsAdapter = NavDetailsAdapter()

    var inputText: Editable? = null
        get() {
            return comm_search_et.text
        }

    private var inputListenerBlock: ((String) -> Unit)? = null;
    private var refreshBlock: ((SwipeRefreshLayout) -> Unit)? = null;


    override fun createView(): View {
        return layoutInflater.inflate(R.layout.fragment_comm_select, null)
    }

    fun getTopBar(): QMUITopBarLayout {
        return topbar
    }

    override fun initWidget(rootView: View) {
        super.initWidget(rootView)
        topbar.addLeftBackImageButton().setOnClickListener {
            popBackStack()
        }
        comm_select_rv.layoutManager = LinearLayoutManager(context);
        comm_select_rv.addItemDecoration(DivUtils.getDivDefault(context, 2))
        comm_slt_pull_layout.setColorSchemeColors(
            ContextCompat.getColor(
                context!!,
                R.color.app_color_red
            )
        )
        comm_select_rv.adapter = mCommAdapter

        comm_input_group.visibility = View.GONE

        comm_slt_pull_layout.setOnRefreshListener {
            refreshBlock?.let { it(comm_slt_pull_layout) };
        }

        comm_search_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputListenerBlock?.let { it(s.toString()) }
            }
        })
        comm_search_tv.visibility = View.GONE
    }

    /**
     * 是否显示 查询按钮
     */
    fun setSearchBtnVisible(visible: Boolean): CommSelectFragment {
        comm_search_tv.visibility = if (visible) View.VISIBLE else View.GONE
        return this;
    }

    /**
     * 设置查询点击事件
     */
    fun setOnSearchBtnClick(block: ((View) -> Unit)?): CommSelectFragment {
        comm_search_tv.setOnClickListener(block)
        return this;
    }


    fun setRefresh(refresh: ((SwipeRefreshLayout) -> Unit)? = null) {
        this.refreshBlock = refresh
    }

    fun setShowInputGroup(isShow: Boolean, hint: String = ""): CommSelectFragment {
        comm_input_group.visibility = if (isShow) View.VISIBLE else View.GONE
        comm_search_et.hint = hint;
        return this;
    }

    fun setInputListener(block: (String) -> Unit): CommSelectFragment {
        inputListenerBlock = block;
        return this;
    }

    fun setLoadMoreListener(block: (SwipeRefreshLayout) -> Unit): CommSelectFragment {
        refreshBlock = block
        return this;
    }

    fun setOpenRefresh(isOpen: Boolean): CommSelectFragment {
        comm_slt_pull_layout.isEnabled = isOpen;
        return this;
    }

    fun setIsRefresh(isRefresh: Boolean): CommSelectFragment {
        comm_slt_pull_layout.isRefreshing = isRefresh;
        return this;
    }

    fun setTitle(title: String): CommSelectFragment {
        topbar.setTitle(title)
        return this;
    }

    fun addLeftBack(): CommSelectFragment {
        topbar.addLeftBackImageButton().onClick { popBackStack() }
        return this;
    }

    fun addRightText(label: String, @IdRes id: Int, block: () -> Unit): CommSelectFragment {
       topbar.addRightTextButton(label, id)
            .onClick { block() }
        return this;
    }

    fun setAdapter(baseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        comm_select_rv.adapter = baseAdapter;
    }

    fun showEmpty(msg: CharSequence? = "没有发现数据,点击重新获得数据", block: ((IEmptyView) -> Unit)? = null) {
        emptyView.showEmpty(msg = msg, block = block)
    }

    fun showFailed(msg: CharSequence?, block: (EmptyView) -> Unit) {
        emptyView.showFailed(msg, block = block)
    }

    fun hideEmpty() {
        emptyView.hideEmpty();
    }

    fun showLoading() {
        emptyView.showEmptyView();
    }


}