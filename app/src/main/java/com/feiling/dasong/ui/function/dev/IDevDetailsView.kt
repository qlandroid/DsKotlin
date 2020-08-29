package com.feiling.dasong.ui.function.dev

import com.feiling.dasong.ui.model.LabelTextModel
import com.feiling.dasong.ui.model.NavDetailsModel

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/21
 * @author ql
 */
interface IDevDetailsView {

    fun setBaseDetails(models: MutableList<LabelTextModel>)

    fun setAttrs(attrs: MutableList<LabelTextModel>)

    fun setGroups(groups: MutableList<NavDetailsModel>)

    fun setPrincipals(groups: MutableList<NavDetailsModel>)

    fun showGroup(isShow: Boolean)
    fun showPrincipals(isShow: Boolean)
}