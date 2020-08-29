package com.feiling.dasong.ui.function.arrange.model

import android.text.TextUtils
import android.view.TextureView

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/16
 * @author ql
 */
class ProcessParams {

    open class ProcessSendParams(var code: String?) {
        //        var code: String? = null;//工序编码
        /**
         * 计划使用设备
         */
        var planCode: String? = null;
        /**
         * 操作类型
         */
        var opType: String? = null;
        /**
         *  如果是员工，必填员工编组
         */
        var employeeCode: String? = null
            set(value) {
                opType = "0"
                field = value
            }
        /**
         *  如果是组，组编码必填
         */
        var groupCode: String? = null
            set(value) {
                opType = "1"
                field = value
            }

        fun isInputAll(): Boolean {
            return (!TextUtils.isEmpty(code))
                    && (!TextUtils.isEmpty(opType)) && (!TextUtils.isEmpty(planCode))
        }
    }

}