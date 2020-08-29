package com.feiling.dasong.widget

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.feiling.dasong.R
import com.qmuiteam.qmui.layout.QMUIButton
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
object ViewUtils {
    /**
     * 高亮显示
     */
    const val BTN_TYPE_HIGHTRIGHT = 0;
    /**
     * 非重要按钮
     */
    const val BTN_TYPE_P = 1;

    fun createNormalBtn(
        context: Context?,
        name: String?,
        layoutParams: ViewGroup.LayoutParams,
        type:Int = BTN_TYPE_HIGHTRIGHT
    ): QMUIButton {

        if (type == BTN_TYPE_P ){
            val qmuiButton = QMUIButton(context)
            qmuiButton.text = name;
            qmuiButton.layoutParams = layoutParams;
            qmuiButton.setBackgroundResource(R.drawable.btn_p_radius)
            qmuiButton.setTextColor(ContextCompat.getColor(context!!, R.color.qmui_config_color_white))
            return qmuiButton;
        }

        val qmuiButton = QMUIButton(context)
        qmuiButton.text = name;
        qmuiButton.layoutParams = layoutParams;
        qmuiButton.setBackgroundResource(R.drawable.btn_radius)
        qmuiButton.setTextColor(ContextCompat.getColor(context!!, R.color.qmui_config_color_white))
        return qmuiButton;
    }

    fun createLinearLayoutBtn(context: Context?, name: String?, weight: Float = 1f,type:Int = BTN_TYPE_HIGHTRIGHT): QMUIButton {
        val layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams.weight = weight;
        layoutParams.leftMargin =
            context?.resources?.getDimensionPixelSize(R.dimen.activity_horizontal_margin)!!
        layoutParams.rightMargin =
            context.resources?.getDimensionPixelSize(R.dimen.activity_horizontal_margin)!!

        return createNormalBtn(context, name, layoutParams,type)
    }

    fun createRootView(context:Context?):ViewGroup{
        var rootGroup = QMUIWindowInsetLayout(context)

        rootGroup.fitsSystemWindows = true;
        rootGroup.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return rootGroup
    }
}