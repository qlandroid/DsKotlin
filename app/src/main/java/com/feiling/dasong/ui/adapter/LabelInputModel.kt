package com.feiling.dasong.ui.adapter

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/11
 * @author ql
 */
class LabelInputModel {
    constructor()

    constructor(key: Any?, label: CharSequence?, value: CharSequence?) {
        this.key = key
        this.label = label
        this.value = value
    }


    var key: Any? = null;

    var label: CharSequence? = null;
    var value: CharSequence? = null;

    /**
     * 是否显示尾部text
     */
    var showEndText: Boolean = false

    /**
     * 尾部显示的text
     */
    var endText: CharSequence? = null;
}