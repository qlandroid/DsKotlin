package com.feiling.dasong.ui.function.devrepair

/**
 * 描述：0:待维修,1:已开工,2:已暂停,3:维修完工
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/4
 * @author ql
 */
enum class DeviceRepairListContentPage(val state: String?) {
    ALL(null), DEFAULT("0"), STARTED("1"), PAUSED("2"), END("3"),UNAUDIT("4");


    companion object {
        open fun getPage(position: String?): DeviceRepairListContentPage {
            return when (position) {
                null -> ALL
                "0" -> DEFAULT
                "1" -> STARTED
                "2" -> PAUSED
                "3" -> END
                else -> ALL
            }
        }
    }
}