package com.feiling.dasong.ui.model

import android.view.Gravity
import androidx.annotation.StringRes

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class LabelTextModel {
    var maxLine: Int? = 1;
    var label: String = "";
    var txt: String? = "";
    @StringRes
    var txtRes: Int? = null;
    var span: Int = 4;//一行最大显示数量
    var contentPosition: Int = Gravity.RIGHT

    var labelColor: Int? = null;
    var textColor: Int? = null;

    constructor(
        label: String?,
        txt: String?,
        span: Int = 4,//一行最大显示数量
        contentPosition: Int = Gravity.RIGHT,
        labelColor: Int? = null,
        textColor: Int? = null,
        maxLine:Int? = null
    ) {
        this.label = label.orEmpty();
        this.txt = txt.orEmpty();
        this.span = span;
        this.contentPosition = contentPosition;
        this.textColor = textColor
        this.labelColor = labelColor
        this.maxLine = maxLine
    }

    constructor(
        label: String,
        @StringRes txt: Int,
        span: Int = 4,//一行最大显示数量
        contentPosition: Int = Gravity.RIGHT
    ) {
        this.label = label;
        this.txtRes = txt;
        this.span = span;
        this.contentPosition = contentPosition;
    }
}