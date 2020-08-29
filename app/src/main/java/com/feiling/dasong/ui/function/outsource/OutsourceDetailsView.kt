package com.feiling.dasong.ui.function.outsource

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.feiling.dasong.R
import com.feiling.dasong.decorator.DivUtils
import com.feiling.dasong.decorator.LinearLayoutUnVerticallyManager
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.widget.EmptyView
import com.feiling.dasong.widget.NavDetailsView
import com.feiling.dasong.widget.ViewUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.layout.QMUIFrameLayout
import com.qmuiteam.qmui.skin.QMUISkinHelper
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups
import kotlinx.android.synthetic.main.outsource_details.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
class OutsourceDetailsView : RelativeLayout, IOSDetailsProcessView {

    private lateinit var unbinder: Unbinder

    lateinit var presenter: IOSDetailsProcessPresenter

    private var mAdapter: NavDetailsOsAdapter = NavDetailsOsAdapter()

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

        val cView = LayoutInflater.from(context).inflate(R.layout.outsource_details, this)
        unbinder = ButterKnife.bind(cView, this)

        mAdapter.setOnItemClickListener { adapter, view, position ->
            presenter.onClickToProcessDetails(position)
        }
        val emptyView = EmptyView(context)
        emptyView.showEmpty(msg = "当前单据没有数据");
        mAdapter.setEmptyView(emptyView)
    }


    override fun initUnout() {

        osDetailsProcessRv.visibility = View.VISIBLE
        osDetailsProcessRv.layoutManager = LinearLayoutUnVerticallyManager(context)
        osDetailsProcessRv.addItemDecoration(DivUtils.getDivDefault(context))
        osDetailsProcessRv.adapter = mAdapter
        osDetailsProcessSuppNdv.visibility = View.VISIBLE

        val sendBtn = ViewUtils.createLinearLayoutBtn(context, "发货")
        sendBtn.onClick {
            presenter.onClickOutSend()
        }
        osDetailsProcessBtnGroup.addView(sendBtn)
        osDetailsProcessBtnGroup.visibility = View.VISIBLE
    }

    override fun initOutEnd() {
        osDetailsProcessRv.visibility = View.VISIBLE
        osDetailsProcessRv.layoutManager = LinearLayoutUnVerticallyManager(context)
        osDetailsProcessRv.addItemDecoration(DivUtils.getDivDefault(context))
        osDetailsProcessRv.adapter = mAdapter
        osDetailsProcessSuppNdv.visibility = View.VISIBLE

        osDetailsProcessBtnGroup.visibility = View.GONE
    }

    override fun initIning() {
        osDetailsProcessRv.layoutManager = LinearLayoutUnVerticallyManager(context)
        osDetailsProcessRv.addItemDecoration(DivUtils.getDivDefault(context))
        osDetailsProcessRv.adapter = mAdapter
        osDetailsProcessSuppNdv.visibility = View.VISIBLE

        osDetailsProcessBtnGroup.visibility = View.GONE
    }


    override fun setSupplierDetails(supp: MutableList<LabelTextModel>?) {
        osDetailsProcessSuppNdv.setDetailsData(supp!!)

    }

    override fun setProcessList(list: MutableList<NavDetailsModel>?) {
        mAdapter.setNewData(list)
        mAdapter.notifyDataSetChanged()
    }

    override fun displayProductInOptionsPopupWindow(
        details: NavDetailsModel?,
        actionIn: () -> Unit
    ) {
        var popupWindow: QMUIFullScreenPopup? = null

        val builder = QMUISkinValueBuilder.acquire()

        val frameLayout: QMUIFrameLayout =
            LayoutInflater.from(context).inflate(
                R.layout.pop_os_process_in,
                null
            ) as QMUIFrameLayout
        frameLayout.background =
            QMUIResHelper.getAttrDrawable(context, R.attr.qmui_skin_support_popup_bg)
        builder.background(R.attr.qmui_skin_support_popup_bg)
        QMUISkinHelper.setSkinValue(frameLayout, builder)
        frameLayout.radius = QMUIDisplayHelper.dp2px(context, 12)
        builder.clear()
        builder.textColor(R.attr.app_skin_common_title_text_color)
        builder.release()

        val ndv = frameLayout.findViewById<NavDetailsView>(R.id.popOsProcessInNdv)
        val cancelBtn = frameLayout.findViewById<TextView>(R.id.popOsProcessCancelBtn)
        val inBtn = frameLayout.findViewById<View>(R.id.popOsProcessInBtn)
        val popOsProcessTitleTv = frameLayout.findViewById<TextView>(R.id.popOsProcessTitleTv)
        cancelBtn.text = "关闭"
        cancelBtn.onClick {
            popupWindow!!.dismiss()

        }
        inBtn.onClick {
            popupWindow!!.dismiss()
            actionIn()
        }

        ndv.title = details?.title
        ndv.subTitle = details?.subTitle
        ndv.setDetailsData(details?.children!!)
        popupWindow = QMUIPopups.fullScreenPopup(context)
            .addView(frameLayout)
            .closeBtn(false)
        popupWindow
            .skinManager(QMUISkinManager.defaultInstance(context))
            .onBlankClick { }
            .show(this)


    }

    override fun displayProductDetails(details: NavDetailsModel?) {
        var popupWindow: QMUIFullScreenPopup? = null

        val builder = QMUISkinValueBuilder.acquire()

        val frameLayout: QMUIFrameLayout =
            LayoutInflater.from(context).inflate(
                R.layout.pop_os_process_in,
                null
            ) as QMUIFrameLayout
        frameLayout.background =
            QMUIResHelper.getAttrDrawable(context, R.attr.qmui_skin_support_popup_bg)
        builder.background(R.attr.qmui_skin_support_popup_bg)
        QMUISkinHelper.setSkinValue(frameLayout, builder)
        frameLayout.radius = QMUIDisplayHelper.dp2px(context, 12)
        builder.clear()
        builder.textColor(R.attr.app_skin_common_title_text_color)
        builder.release()

        val ndv = frameLayout.findViewById<NavDetailsView>(R.id.popOsProcessInNdv)
        val cancelBtn = frameLayout.findViewById<View>(R.id.popOsProcessCancelBtn)
        val inBtn = frameLayout.findViewById<View>(R.id.popOsProcessInBtn)
        val popOsProcessTitleTv = frameLayout.findViewById<TextView>(R.id.popOsProcessTitleTv)

        popOsProcessTitleTv.text = "委外工序详情"
        cancelBtn.onClick {
            popupWindow!!.dismiss()
        }
        inBtn.visibility = View.GONE
        inBtn.onClick {
            popupWindow!!.dismiss()
        }

        ndv.title = details?.title
        ndv.subTitle = details?.subTitle
        ndv.setDetailsData(details?.children!!)
        popupWindow = QMUIPopups.fullScreenPopup(context)
            .addView(frameLayout)
            .closeBtn(false)
        popupWindow
            .skinManager(QMUISkinManager.defaultInstance(context))
            .onBlankClick { }
            .show(this)
    }
}