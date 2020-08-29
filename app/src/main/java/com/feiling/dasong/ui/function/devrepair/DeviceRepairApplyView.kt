package com.feiling.dasong.ui.function.devrepair

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import butterknife.ButterKnife
import com.feiling.dasong.R
import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.widget.ImgSelectView
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.device_apply_repair.view.*


/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
class DeviceRepairApplyView : LinearLayout {


    var inputRemark: CharSequence
        set(value) {
            deviceApplyRepairRemarkEt.setText(value)
        }
        get() {
            return deviceApplyRepairRemarkEt.text
        }

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


        var cView = LayoutInflater.from(context).inflate(R.layout.device_apply_repair, this)
        ButterKnife.bind(cView, this);

        deviceApplyRepairSeeDetailsBtn.visibility = View.GONE
        deviceApplyRepairDevEmptyView.visibility = View.VISIBLE
        deviceApplyRepairImgSelectView.notifyDataSetChanged(null)
    }

    fun setOnSelectImageClick(l:ImgSelectView.OnImgSelectListener){
        deviceApplyRepairImgSelectView.onImgSelectListener = l;
    }

    fun setSelectImgList(list:List<String>){
        deviceApplyRepairImgSelectView.notifyDataSetChanged(list);
    }


    fun setDeviceView(list: MutableList<LabelTextModel>) {
        deviceApplyRepairDevDetailsNdv.setDetailsData(list)
        deviceApplyRepairDevEmptyView.visibility = View.GONE
        deviceApplyRepairSeeDetailsBtn.visibility = View.VISIBLE
    }

    fun setLookDeviceDetailsClick(block: () -> Unit) {
        deviceApplyRepairSeeDetailsBtn.onClick { block() }
    }

    fun setDeviceNavClick(block: () -> Unit) {
        deviceApplyRepairDevDetailsNdv.setNavTitleClickListener {
            block()
        }
    }

    fun setSubmitClick(block: () -> Unit) {
        deviceApplyRepairSubmitTv.onClick {
            block();
        }
    }

}