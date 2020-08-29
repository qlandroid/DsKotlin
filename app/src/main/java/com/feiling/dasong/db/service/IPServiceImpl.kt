package com.feiling.dasong.db.service

import com.feiling.dasong.db.IPModel
import org.litepal.LitePal
import java.text.DecimalFormat

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/23
 * @author ql
 */
class IPServiceImpl : IIPService {

    override fun insert(model: IPModel) {

        model.date = System.currentTimeMillis()
        model.save()
    }

    override fun setDefaultModel(id: Long) {
        val useList =
            LitePal.where("state = ? ", DecimalFormat("0").format(IPModel.STATE_USE)).find(IPModel::class.java)
        useList.forEach {
            it.state = IPModel.STATE_DEFAULT
            it.save()
        }
        val find = LitePal.find(IPModel::class.java, id)
        find.state = IPModel.STATE_USE
        find.date = System.currentTimeMillis()
        find.update(id)
    }

    override fun getDefaultModel(): IPModel? {
        return LitePal.where("state = ? ", IPModel.STATE_USE.toString())
            .findFirst(IPModel::class.java)
    }

    override fun getIpModelAll(): MutableList<IPModel> {
        return LitePal.order("date desc").find(IPModel::class.java)
    }

    override fun updateModel(id: Long, model: IPModel) {
        model.id = id
        model.update(id)
    }

    override fun setDefaultAddress(address: String?) {
        val find = LitePal.where("address = ?", address).find(IPModel::class.java)

        if (find.isEmpty()) {
            val ipModel = IPModel()
            ipModel.address = address
            insert(ipModel)
            setDefaultModel(ipModel.id!!)
        } else {
            setDefaultModel(find.first().id!!)
        }

    }


}