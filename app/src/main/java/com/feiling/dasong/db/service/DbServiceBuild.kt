package com.feiling.dasong.db.service

import com.feiling.dasong.db.IPModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/23
 * @author ql
 */
object DbServiceBuild {

    val ipService: IIPService by lazy {
        IPServiceImpl()
    }

    fun initIp() {
        var defaultModel = ipService.getDefaultModel()
        if (defaultModel == null) {
            defaultModel = IPModel()
            defaultModel.address = "http://192.168.1.21:8096"
            DbServiceBuild.ipService.insert(defaultModel);
            DbServiceBuild.ipService.setDefaultModel(defaultModel.id!!);
        }
    }
}