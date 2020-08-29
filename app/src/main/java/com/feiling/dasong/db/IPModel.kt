package com.feiling.dasong.db

import org.litepal.crud.LitePalSupport

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/23
 * @author ql
 */
class IPModel : LitePalSupport() {
    companion object {
        /**
         * 默认状态
         */
        const val STATE_DEFAULT = 0;
        /**
         * 使用状态
         */
        const val STATE_USE = 1
    }


    var id: Long? = null

    /**
     * 必须  http://  或 https://开头
     */
    var address: String? = null

    var date: Long? = null

    /**
     * 使用状态
     */
    var state: Int = STATE_DEFAULT


}