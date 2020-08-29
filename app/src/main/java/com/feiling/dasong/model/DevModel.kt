package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/5
 * @author ql
 */
class DevModel {
    /**
     * 设备编码
     **/
    var ceqcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 设备名称
     **/
    var ceqname: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 类别编码
     **/
    var ceqtypecode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 设备型号
     **/
    var clbcode: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 多个组
     **/
    var groups: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 组列表
     **/
    var groupsList: MutableList<GroupModel>? = null
    /**
     * 设备属性集合
     **/
    var list: MutableList<AttrModel>? = null
    /**
     * 多个人
     **/
    var personnels: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 人列表
     **/
    var personnelsList: MutableList<EmployeeModel>? = null
    /**
     * 备用字段1
     **/
    var stan1: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段2
     **/
    var stan2: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段3
     **/
    var stan3: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段4
     **/
    var stan4: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }
    /**
     * 备用字段5
     **/
    var stan5: String? = null
        get() {
            if (field == null) {
                return ""
            }
            return field
        }


}