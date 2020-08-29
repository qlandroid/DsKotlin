package com.feiling.dasong.comm

import android.view.View
import com.feiling.dasong.widget.EmptyView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/6/29
 * @author ql
 */
interface IEmptyView {

    fun showEmptyView(
        loading: Boolean = true,
        title: CharSequence? = null,
        msg: CharSequence? = null,
        buttonText: CharSequence? = null,
        block: ((View) -> Unit)? = null
    )


    fun showEmpty(
        title: CharSequence? = null,
        msg: CharSequence? = "没有找到数据",
        block: ((IEmptyView) -> Unit)? = null
    );

    fun showFailed(
        title: CharSequence? = "",
        msg: CharSequence? = "加载失败",
        block: ((EmptyView) -> Unit)? = null
    )

    fun hideEmpty()
}