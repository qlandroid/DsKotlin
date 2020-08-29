package com.feiling.dasong.ui.function.arrange.adapter.process

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/26
 * @author ql
 */
class ProcessNode : BaseExpandNode {

    constructor(name: String?, code: String?) : super() {
        this.name = name
        this.code = code
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
    override var childNode: MutableList<BaseNode>? = null

}