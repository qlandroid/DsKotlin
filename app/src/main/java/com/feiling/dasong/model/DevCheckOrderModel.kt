package com.feiling.dasong.model

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/7/10
 * @author ql
 */
class DevCheckOrderModel {

    /**
     * 设备编码
     **/
    var ceqCode : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 设备名称
     **/
    var ceqName : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 修改人
     **/
    var changeBy : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 修改日期
     **/
    var changeDate : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 点检日期
     **/
    var checkDate : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 点检项名称
     **/
    var checkName : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 点检人员(编码)
     **/
    var checkPersonnel : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 点检人员(姓名)
     **/
    var checkPersonnelName : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * (单间操作单号)单据编码
     **/
    var checkSheetCode : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 点检项目列表
     **/
    var checkSheetProjectList : MutableList<DevCheckItemModel>?=null
    /**
     * 点检状态
     **/
    var checkState : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 创建人
     **/
    var createBy : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 创建日期
     **/
    var createDate : Long?=null
    /**
     *ID
     **/
    var id : Int?=null
    /**
     * 审核人(编码)
     **/
    var verifyby : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 审核日期
     **/
    var verifydate : String?=null
        get(){if(field == null){return ""}
            return field}
    /**
     * 审核状态
     **/
    var vstatus : String?=null
        get(){if(field == null){return ""}
            return field}
}