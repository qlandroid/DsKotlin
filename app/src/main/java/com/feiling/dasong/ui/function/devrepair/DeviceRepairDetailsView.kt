package com.feiling.dasong.ui.function.devrepair

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.IdRes
import butterknife.ButterKnife
import com.feiling.dasong.R
import com.feiling.dasong.decorator.DivUtils
import com.feiling.dasong.decorator.LinearLayoutUnVerticallyManager
import com.feiling.dasong.ui.adapter.LabelInputAdapter
import com.feiling.dasong.ui.adapter.LabelInputModel
import com.feiling.dasong.ui.adapter.LabelTextAdapter
import com.feiling.dasong.ui.adapter.NavDetailsAdapter
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel
import com.feiling.dasong.widget.EmptyView
import com.feiling.dasong.widget.ImgSelectView
import com.feiling.dasong.widget.ViewUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.layout.QMUIButton
import kotlinx.android.synthetic.main.device_repair_details.view.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/30
 * @author ql
 */
class DeviceRepairDetailsView : RelativeLayout {

    enum class InvTakeType {
        /**
         * takeValue - 装载 显示 adapter
         * takeInput - 状态 输入 adapter
         */
        TakeValue, TakeInput
    }

    /**
     * 设备加载失败
     */
    var devDetailsFailedVisible: Boolean
        set(value) {
            deviceRepairDetailsFailedGroup.visibility = if (value) View.VISIBLE else View.GONE
        }
        get() {
            return deviceRepairDetailsFailedGroup.visibility == View.VISIBLE
        }

    /**
     * 设备详情中的加载信息
     */
    var devDetailsProgressBarVisible: Boolean
        set(value) {
            deviceRepairDetailsProgressBar.visibility = if (value) View.VISIBLE else View.GONE
        }
        get() {
            return deviceRepairDetailsProgressBar.visibility == View.VISIBLE
        }

    /**
     * 故障描述
     */
    var faultRemarkText: CharSequence?
        set(value) {
            deviceRepairDetailsFaultRemarkTv.text = value
        }
        get() {
            return deviceRepairDetailsFaultRemarkTv.text;
        }

    /**
     * 故障描述 是否可见
     */
    var faultRemarkVisible: Boolean
        set(value) {
            deviceRepairDetailsFaultRemarkNdv.visibility = if (value) View.VISIBLE else View.GONE
        }
        get() {
            return deviceRepairDetailsFaultRemarkNdv.visibility == View.VISIBLE

        }

    /**
     * 设置设备维修人员列表
     */
    var repairMembers: MutableList<NavDetailsModel>
        set(value) {
            mRepairMemberAdapter.setNewData(value);
            mRepairMemberAdapter.notifyDataSetChanged()
        }
        get() = mRepairMemberAdapter.data

    /**
     * 设置设备维修描述 记录
     */
    var repairRemarkText: CharSequence? = null
        set(value) {
            deviceRepairDetailsRemarkTv.text = value;
            field = value
        }


    /**
     * 设置维修描述 是否可见
     */
    var repairRemarkVisible: Boolean
        set(value) {
            deviceRepairDetailsRemarkNdv.visibility = if (value) View.VISIBLE else View.GONE;
        }
        get() {
            return deviceRepairDetailsRemarkNdv.visibility == View.VISIBLE
        }

    /**
     * 申请人详情
     */
    var applyMember: MutableList<LabelTextModel>? = null
        set(value) {
            deviceRepairDetailsApplyMemberNdv.setDetailsData(value)
            field = value
        }

    /**
     * 审核信息
     */
    var auditDetails: MutableList<LabelTextModel>? = null
        set(value) {
            deviceRepairDetailsAuditNdv.setDetailsData(value)
            field = value
        }

    /**
     * 设置 审核信息 是否可见
     */
    var verifyGroupVisible: Boolean
        set(value) {
            deviceRepairDetailsAuditNdv.visibility = if (value) View.VISIBLE else View.GONE
        }
        get() {
            return deviceRepairDetailsAuditNdv.visibility == View.VISIBLE
        }

    /**
     * 设置设备信息
     */
    var deviceModel: MutableList<LabelTextModel>? = null
        set(value) {
            value?.let {
                deviceRepairDetailsNdv.setDetailsData(value)
            }

            field = value;
        }

    /**
     * 状态
     */
    var stateText: CharSequence?
        get() = deviceRepairDetailsStateTv.text
        set(value) {
            deviceRepairDetailsStateTv.text = value
        }

    var bottomBtnGroupVisible: Boolean
        get() {
            return bottomTabGroup.visibility == View.VISIBLE
        }
        set(value) {
            bottomTabGroup.visibility = if (value) View.VISIBLE else View.GONE
        }

    /**
     *  使用消耗品的 适配器 显示
     */
    private var mRepairInvAdapter: LabelTextAdapter = LabelTextAdapter()

    /**
     * 使用输入框的 适配器
     */
    private var mRepairInvInputAdapter: LabelInputAdapter = LabelInputAdapter()

    /**
     * 指定维修人员的适配器
     */
    private var mRepairMemberAdapter: NavDetailsAdapter = NavDetailsAdapter();

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
        val view = LayoutInflater.from(context).inflate(R.layout.device_repair_details, this)
        ButterKnife.bind(view)

        deviceRepairDetailsRepairMemberRv.layoutManager = LinearLayoutUnVerticallyManager(context);
        deviceRepairDetailsRepairMemberRv.adapter = mRepairMemberAdapter;
        var emptyView = EmptyView(context)
        emptyView.showEmpty(msg = "请添加维修人员")
        mRepairMemberAdapter.setEmptyView(emptyView)

        deviceRepairDetailsImgSelectView.visibility = View.GONE
        deviceRepairDetailsImgSelectView.showDel = false;
        deviceRepairDetailsImgSelectView.showAdd = false;

        deviceRepairDetailsInvRv.layoutManager = LinearLayoutUnVerticallyManager(context)
        deviceRepairDetailsInvRv.addItemDecoration(DivUtils.getDivDefault(context))
    }


    fun setOnImgClick(l: ImgSelectView.OnImgSelectListener) {
        deviceRepairDetailsImgSelectView.onImgSelectListener = l;
    }

    /**
     * 查看详情
     */
    fun setClickToDevDetails(block: () -> Unit) {
        deviceRepairDetailsNdv.setNavTitleClickListener {
            block()
        }
    }

    /**
     * 监听 消耗品选择 点击事件
     */
    fun setOnClickToInvSelect(block: ((View) -> Unit)?) {
        deviceRepairDetailsInvNdv.setNavTitleClickListener(block)
    }

    /**
     * 维修类别选择
     */
    fun setOnRepairTypeClick(block: ((View) -> Unit)?) {
        deviceRepairDetailsTypeNdv.setNavTitleClickListener(block)
    }

    /**
     * 维修类别 名称
     */
    fun setRepairType(type: CharSequence?) {
        deviceRepairDetailsTypeNdv.hint = type;
    }

    /**
     * 显示申请时上传的图片
     */
    fun setImages(imgs: List<String>?) {
        if (imgs != null && imgs.isNotEmpty()) {
            if (deviceRepairDetailsImgSelectView.visibility != View.VISIBLE) {
                deviceRepairDetailsImgSelectView.visibility = View.VISIBLE;
            }
        }

        deviceRepairDetailsImgSelectView.notifyDataSetChanged(imgs);
    }

    /**
     * 添加按钮
     */
    fun addBottomButton(name: String, @IdRes id: Int): QMUIButton {
        var button = ViewUtils.createLinearLayoutBtn(context, name)
        button.id = id;
        bottomTabBtnGroup.addView(button)
        return button
    }

    /**
     * 移出全部按钮
     */
    fun removeBottomButtonAll() {
        bottomTabBtnGroup.removeAllViews();
    }

    /**
     * 移出底部按钮
     */
    fun removeBottomButtonById(view: View?) {
        bottomTabBtnGroup.removeView(view)
    }

    /**
     * 设置是否可以编写设备 维修描述
     */
    fun setShowRepairRemarkInput(isShow: Boolean) {
        deviceRepairDetailsRemarkNdv.hideRightIcon = isShow;
        if (isShow) {
            deviceRepairDetailsRemarkNdv.hint = "请输入内容"
            deviceRepairDetailsRemarkNdv.hideRightIcon = false
        } else {
            deviceRepairDetailsRemarkNdv.hint = ""
            deviceRepairDetailsRemarkNdv.hideRightIcon = true
        }

    }

    /**
     * 设置设备维修描述 点击事件
     */
    fun setRepairRemarkInputClick(block: (() -> Unit)?) {
        deviceRepairDetailsRemarkNdv.setNavTitleClickListener {
            block?.let {
                it()
            }
        }
    }


    /**
     * 设置加载设备 失败的原因
     */
    fun setDevDetailsFailed(msg: CharSequence?) {
        deviceRepairDetailsFailedTextTv.text = msg;
    }

    /**
     * 设置点击事件
     */
    fun setOnRepairMemberClick(block: ((Int) -> Unit)?) {
        if (block == null) {
            mRepairMemberAdapter.setOnItemClickListener(null)
        } else {
            mRepairMemberAdapter.setOnItemClickListener { adapter, view, position ->
                block(position);
            }
        }

    }

    fun setRepairMemberNavClick(function: (() -> Unit)?) {
        if (function == null) {
            deviceRepairDetailsRepairMember.setNavTitleClickListener(null)
            return;
        }

        deviceRepairDetailsRepairMember.setNavTitleClickListener {
            function()
        }
    }

    fun setShowRepairMemberNavHint(isShow: Boolean, hint: String? = null) {
        if (isShow) {
            deviceRepairDetailsRepairMember.hideRightIcon = false
            deviceRepairDetailsRepairMember.hint = hint;
            return
        }

        deviceRepairDetailsRepairMember.hideRightIcon = true;
        deviceRepairDetailsRepairMember.hint = ""
    }

    fun scrollMoveToTop() {
        deviceRepairDetailsSl.smoothScrollTo(0, 0)
    }

    /**
     * 设置是否显示 消耗配件内容
     */
    fun setShowInvListView(show: Boolean) {
        deviceRepairDetailsInvNdv.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * 设置显示消耗品列表
     */
    fun setInvList(list: MutableList<LabelTextModel>?) {
        mRepairInvAdapter.setNewData(list)
        mRepairInvAdapter.notifyDataSetChanged()
    }

    /**
     * 设置显示 可输入数值的列表
     */
    fun setInvInputList(list: MutableList<LabelInputModel>?) {
        mRepairInvInputAdapter.setNewData(list)
        mRepairInvInputAdapter.notifyDataSetChanged()
    }

    fun setInvRvTakeType(type: InvTakeType) {
        when (type) {
            InvTakeType.TakeInput -> {
                deviceRepairDetailsInvRv.adapter = mRepairInvInputAdapter
                var emptyView = EmptyView(context)
                emptyView.showEmpty(msg = "没有使用配件")
                mRepairInvInputAdapter.setEmptyView(emptyView)
            }
            InvTakeType.TakeValue -> {
                var emptyView = EmptyView(context)
                emptyView.showEmpty(msg = "没有使用配件")
                mRepairInvAdapter.setEmptyView(emptyView)
                deviceRepairDetailsInvRv.adapter = mRepairInvAdapter
            }
        }
    }

    fun setShowRepairTypeSelectVisible(isShow: Boolean) {
        deviceRepairDetailsTypeNdv.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun setInvNavHint(hint: String) {
        deviceRepairDetailsInvNdv.hint = hint;
    }

    fun setShowInvNavNavIcon(isShow:Boolean){
        deviceRepairDetailsInvNdv.hideRightIcon = !isShow;

    }


}