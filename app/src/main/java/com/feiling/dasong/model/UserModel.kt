package com.feiling.dasong.model

/**
 * 描述： {"code":0,"message":"成功返回信息","data":{"code":"0001","name":"吴小峰","password":"e10adc3949ba59abbe56e057f20f883e","password2":"A685C29B84DC76","enabled":1,"allowscaner":0,"empcode":"0001","expirationdate":null,"createdby":"Admin","createddate":1355322378000,"changedby":"Admin","changeddate":1589268966170,"token":1592642378962,"gdztag":null}}
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/15
 * @author ql
 */
class UserModel {
    var code: String? = null;
    var empcode: String? = null;
    var expirationdate: String? = null;
    var name: String? = null;
    /**
     * 工段长标识
     */
    var gdztag: String? = null

    /**
     * 是否是工段长
     */
    var isSectionChief: Boolean = false
        get() {
            return gdztag == "1"
        }
}