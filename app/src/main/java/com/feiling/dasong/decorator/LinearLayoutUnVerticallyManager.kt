package com.feiling.dasong.decorator

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/8
 * @author ql
 */
class LinearLayoutUnVerticallyManager(context: Context?) : LinearLayoutManager(context) {

    override fun canScrollVertically(): Boolean {
        return false
    }
}