package com.feiling.dasong.ui.function.quality

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import butterknife.ButterKnife
import com.feiling.dasong.R
import com.feiling.dasong.ui.function.model.NavData
import com.qmuiteam.qmui.kotlin.onClick
import com.qmuiteam.qmui.layout.QMUIButton
import kotlinx.android.synthetic.main.proce_check_view.view.*

/**
 * 描述：工序质检详情
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/27
 * @author ql
 */
class ProcessQualityDetailsView : RelativeLayout, IProcessQualityDetailsView {

    lateinit var presenter: IProcessQualityPresenter


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
        val view = LayoutInflater.from(context).inflate(R.layout.proce_check_view, this)
        ButterKnife.bind(this, view)
    }

    private fun createBtn(@StringRes name: Int): QMUIButton {
        return createBtn(context.getString(name))
    }

    private fun createBtn(name: String): QMUIButton {
        val qmuiButton = QMUIButton(context)
        qmuiButton.text = name;
        val layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams.weight = 1f;
        layoutParams.leftMargin =
            context?.resources?.getDimensionPixelSize(R.dimen.activity_horizontal_margin)!!
        layoutParams.rightMargin =
            context?.resources?.getDimensionPixelSize(R.dimen.activity_horizontal_margin)!!
        qmuiButton.layoutParams = layoutParams;
        qmuiButton.setBackgroundResource(R.drawable.btn_radius)
        qmuiButton.setTextColor(ContextCompat.getColor(context!!, R.color.qmui_config_color_white))
        return qmuiButton;
    }

    override fun initDefault() {
        processCheckStatusIv.visibility = View.GONE
        val createBtn = createBtn(context.getString(R.string.btn_start_process))
        createBtn.onClick {
            presenter.onProcessCheckStart()
        }
        processCheckBtnGroup.addView(createBtn)
        processCheckBtnGroup.visibility = View.VISIBLE
    }

    override fun initStarted() {
        val pauseBtn = createBtn(R.string.btn_pause_process)
        pauseBtn.onClick {
            presenter.onProcessCheckPause()
        }
        processCheckBtnGroup.addView(pauseBtn)

        val endBtn = createBtn(R.string.btn_end_process)
        endBtn.onClick { presenter.onProcessCheckEnd() }
        processCheckBtnGroup.addView(endBtn)
        processCheckStatusIv.visibility = View.GONE

        processCheckBtnGroup.visibility = View.VISIBLE
    }

    override fun initPause() {
        processCheckStatusIv.visibility = View.GONE
        val createBtn = createBtn(R.string.btn_end_process)
        createBtn.onClick {
            presenter.onProcessCheckStart()
        }
        processCheckBtnGroup.addView(createBtn)
        processCheckBtnGroup.visibility = View.VISIBLE
    }

    override fun initStop() {
        processCheckStatusIv.visibility = View.GONE
        processCheckBtnGroup.visibility = View.GONE
    }

    override fun initOk() {
        processCheckStatusIv.visibility = View.VISIBLE
        processCheckStatusIv.setImageResource(R.drawable.icon_audit_success)
        processCheckBtnGroup.visibility = View.GONE
    }

    override fun initFailed() {
        processCheckBtnGroup.visibility = View.GONE
        processCheckStatusIv.visibility = View.VISIBLE
        processCheckStatusIv.setImageResource(R.drawable.icon_audit_failed)
    }
    override fun initConcession() {
        processCheckBtnGroup.visibility = View.GONE
        processCheckStatusIv.visibility = View.GONE
    }

    override fun setProcessDetails(model: NavData.ProcessViewModel) {
        processCheckProcessView.setDetailsData(model.view)

    }

    override fun setProductDetails(model: NavData.ProductViewModel) {
        processCheckProductView.setDetailsData(model.view)
    }

    override fun setShowFailedMsg(failedMsg: String?) {
        processCheckFailedMsgTv.text = failedMsg
    }

    override fun setShowFailedView(visible: Boolean) {
        processCheckFailedNdv.visibility = if (visible) View.VISIBLE else View.GONE
    }


}