package com.feiling.dasong.ui.function.arrange.adapter

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.module.LoadMoreModule

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/22
 * @author ql
 */
class GroupSelectAdapter : BaseNodeAdapter,LoadMoreModule {
    companion object {
        const val ITEM_GROUP = 1
        const val ITEM_CHILDREN = 2
        const val EXPAND_COLLAPSE_PAYLOAD = 110
    }

    var groupNodeProvider = GroupNodeProvider();
    var childNodeProvider = ChildrenNodeProvider();

    constructor() : super() {
        addNodeProvider(groupNodeProvider)
        addNodeProvider(childNodeProvider)
    }

    fun setOnGroupSelectListener(l: GroupNodeProvider.OnSelectGroupListener) {
        groupNodeProvider.selectGroupListener = l;
    }


    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when (data[position]) {
            is GroupNode -> {
                ITEM_GROUP
            }
            is ChildrenNode -> {
                ITEM_CHILDREN
            }
            else -> {
                -1
            }
        }

    }
}