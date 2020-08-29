package com.feiling.dasong.ui.function.devrepair

/**
 * 描述：0:未审核 1:已审核
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/4
 * @author ql
 */
enum class DeviceRepairVerifyListContentPage(val state:String) {
    UNVERIFY("0"), VERIFY("1");


    companion object {
        open fun getPage(state: String?): DeviceRepairVerifyListContentPage {
            return when (state) {
                "1" -> VERIFY
                else -> UNVERIFY
            }
        }
    }
}