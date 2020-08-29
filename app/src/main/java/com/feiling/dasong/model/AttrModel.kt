package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/21
 * @author ql
 */
class AttrModel {

    /**
     * 设备id
     **/
    var deviceid: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     *主键id
     **/
    var id: Int? = null
    /**
     * 名称
     **/
    var name: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * value
     **/
    var value: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
}