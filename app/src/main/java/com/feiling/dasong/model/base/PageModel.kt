package com.feiling.dasong.model.base

import com.feiling.dasong.C
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/15
 * @author ql
 */
open class PageModel {

    companion object {
        private var FIRST_PAGE_NO = 1
        private var PAGE_SIZE = 20;

        fun initPage(pageNo: Int, pageSize: Int) {
            setFirstPageNo(pageNo)
            setPageSize(pageSize)
        }

        fun setPageSize(pageSize: Int) {
            PAGE_SIZE = pageSize
        }

        fun setFirstPageNo(first: Int) {
            FIRST_PAGE_NO = first;
        }
    }

    var sFirstPageNo = FIRST_PAGE_NO;
    var sPageSize = PAGE_SIZE

    open var list: JsonArray? = null;
    var pages: Int? = null;
    var size: Int? = null;
    var total: Int? = null;
    var current: Int? = null;

    val isNotLoadSize: Boolean
        get() {
            if (list != null) {
                return list!!.size() < sPageSize
            };
            return false
        }
    val isEmpty: Boolean
        get() {
            if (list == null) {
                return true;
            }
            return list!!.size() == 0;
        }
    val isFirst: Boolean
        get() {
            return current == sFirstPageNo
        }

//    public fun <T> formList(clazz: Class<*>): MutableList<T> {
//
//
//        var sGson = C.sGson
//        var type = object : TypeToken<MutableList<T>>() {}.type
//        val pageModel = sGson.fromJson<MutableList<T>>(list, type)
//        return pageModel
//    }
}