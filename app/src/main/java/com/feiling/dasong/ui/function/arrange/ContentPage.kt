package com.feiling.dasong.ui.function.arrange

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/4
 * @author ql
 */
enum class ContentPage(val position:Int) {
    ALL(0),DEFAULT(1), PENDING(2), STARTED(3), PAUSED(4), END(5);


    companion object {
       open fun getPage(position: Int): ContentPage {
            return when (position) {
                0 -> ALL
                1 -> DEFAULT
                2 -> PENDING
                3 -> STARTED
                4 -> PAUSED
                5 -> END
                else -> ALL
            }
        }
    }
}