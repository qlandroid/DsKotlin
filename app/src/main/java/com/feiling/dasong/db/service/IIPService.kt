package com.feiling.dasong.db.service

import com.feiling.dasong.db.IPModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/23
 * @author ql
 */
interface IIPService {

    fun insert(model: IPModel)

    fun setDefaultModel(id:Long)

    fun getDefaultModel(): IPModel?

    fun getIpModelAll(): MutableList<IPModel>

    fun updateModel(id: Long, model: IPModel)

    fun setDefaultAddress(address: String?)
}