package com.feiling.dasong.ui.function.arrange.adapter

import com.chad.library.adapter.base.entity.node.BaseNode

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/22
 * @author ql
 */
class ChildrenNode : BaseNode {

    constructor(name: String?, code: String?) : super() {
        this.name = name;
        this.code = code;
    }

    var name: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    var code: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }

    override val childNode: MutableList<BaseNode>? = null
}