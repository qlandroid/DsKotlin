package com.feiling.dasong.model.base

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/15
 * @author ql
 */
open class BasePage(open var pageNo: Int = PAGE_NO_1, open var pageSize: Int = SIZE) {
    companion object {
        const val SIZE = 20;
        const val PAGE_NO_1 = 1;
    }

    open fun reset() {
        pageNo = 1;
    }

    fun nextPage() {
        pageNo++;
    }

    fun isFirst(): Boolean {
        return pageNo == PAGE_NO_1
    }
}