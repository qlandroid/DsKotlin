package com.feiling.dasong.ui.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/28
 * @author ql
 */
class InventorySelectModel {


    constructor(name: String?, code: String?, std: String?, selected: Boolean = false) {
        this.name = name
        this.code = code
        this.std = std
        this.selected = selected
    }

    /**
     * 存货名称
     */
    var name: String? = null

    /**
     * 存货编码
     */
    var code: String? = null

    /**
     * 选中状态
     */
    var selected: Boolean = false

    /**
     * 规格
     */
    var std: String? = null
}