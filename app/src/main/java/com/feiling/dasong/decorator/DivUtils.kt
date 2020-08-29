package com.feiling.dasong.decorator

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feiling.dasong.R
import com.qmuiteam.qmui.util.QMUIDisplayHelper

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
object DivUtils {

    fun getDivDefault(context: Context?, height: Int = 2): RecyclerView.ItemDecoration {
        return DividerItemDecoration2(
            context,
            LinearLayoutManager.VERTICAL,
            QMUIDisplayHelper.dp2px(context, height),
            ContextCompat.getColor(context!!,R.color.div_color)
        )
    }
    fun getDivGridDefault(context: Context?, height: Int = 2): RecyclerView.ItemDecoration {
        return GridDividerItemDecoration(
            context,
            QMUIDisplayHelper.dp2px(context, height)
        )
    }
}