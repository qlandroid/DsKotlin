package com.feiling.dasong.ui.function.arrange.adapter.process

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/8/26
 * @author ql
 */
class ProcessNodeAdapter : BaseNodeAdapter {

    companion object {
        const val NODE_PROCESS_PARENT = 1
        const val NODE_PROCESS_CHILDREN = 2;
        const val EXPAND_COLLAPSE_PAYLOAD = 110

    }

    val childrenProvider = ProcessChildrenNodeProvider()
    val firstProvider = ProcessFirstNodeProvider()

    constructor() : super() {
        addNodeProvider(firstProvider)

        addNodeProvider(childrenProvider)
    }

    fun setChildrenClick(block: ((BaseNode) -> Unit)?) {
        childrenProvider.childrenClickBlock = block;
    }


    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        var i = when (data[position]) {
            is ProcessNode -> {
                NODE_PROCESS_PARENT
            }
            is ProcessChildrenNode -> {
                NODE_PROCESS_CHILDREN
            }
            else -> {
                NODE_PROCESS_CHILDREN
            }
        }
        return i
    }
}