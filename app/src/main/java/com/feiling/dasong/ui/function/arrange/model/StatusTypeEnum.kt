package com.feiling.dasong.ui.function.arrange.model

import com.feiling.dasong.R

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/4/3
 * @author ql
 */
enum class StatusTypeEnum(val label:Int,val color: Int) {
    DEFAULT(R.string.task_status_default,R.color.task_status_default),
    PENDING(R.string.task_status_pending,R.color.task_status_pending),
    START(R.string.task_status_start,R.color.task_status_start),
    END(R.string.task_status_end,R.color.task_status_end),
    PAUSE(R.string.task_status_pause,R.color.task_status_stop),
    /**
     * 未审核状态
     */
    UNVERIFY(R.string.un_verify,R.color.unverify_color),
    /**
     * 已审核
     */
    VERIFY(R.string.verify,R.color.verify_color),
    FAILED(R.string.tag_state_failed, R.color.unverify_color)
}