package com.feiling.dasong.ui.model

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
data class NavDetailsModel(
    var title: String?,
    var children: MutableList<LabelTextModel>?,
    var hint: String?,
    var showRight: Boolean = false,
    @DrawableRes var leftIcon: Int?,
    @DrawableRes var rightIcon: Int?,
    var subTitle: String? = null
) {
    @ColorInt
    var tagBackgroundColor: Int? = null
    @ColorInt
    var hintColor: Int? = null;
    var tagText: CharSequence? = null;
    var showTag: Boolean = false;

    constructor(title: String? = null, children: MutableList<LabelTextModel>? = null) : this(
        title,
        children,
        null,
        false,
        null,
        null
    )

    constructor(title: String?, children: MutableList<LabelTextModel>?, hint: String?) : this(
        title,
        children,
        hint,
        false,
        null,
        null
    )
}