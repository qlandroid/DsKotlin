package com.feiling.dasong.ui.function.arrange.model

import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.annotation.ColorRes
import com.feiling.dasong.R
import com.feiling.dasong.uitils.DateUtils

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/20
 * @author ql
 */
class ProcessRecodeModel {
    //0-派工,1-开工,2-暂停,3-加人,4-完工,5-质检合格,6-质检不合格,7-委外 ,
    enum class Status(var value: String?, @ColorRes var color: Int, var tagName: String) {
        PENDING("0", R.color.task_status_pending, "派工"),
        START("1", R.color.task_status_start, "开工"),
        PAUSE("2", R.color.task_status_stop, "暂停"),
        ADD("3", R.color.task_status_start, "添加人员"),
        END("4", R.color.task_status_end, "完工"),
        CheckOk("5", R.color.process_check_status_ok, "质检合格"),
        CheckFailed("6", R.color.process_check_status_failed, "质检不合格"),
        Outsource("7", R.color.process_check_status_default, "委外"),
        Cancel("8", R.color.process_check_status_default, "取消报工"),
        Handover("9", R.color.process_check_status_default, "交接"),
        Concession("10", R.color.process_check_status_failed, "让步交接");


        companion object {

            fun getStatus(status: String?): Status {
                return when (status) {
                    PENDING.value -> {
                        PENDING
                    }
                    START.value -> {
                        START
                    }
                    PAUSE.value -> {
                        PAUSE
                    }
                    END.value -> {
                        END
                    }
                    ADD.value -> {
                        ADD
                    }
                    CheckOk.value -> {
                        CheckOk
                    }
                    CheckFailed.value -> {
                        CheckOk
                    }
                    Outsource.value -> {
                        Outsource
                    }
                    Cancel.value -> {
                        Cancel
                    }
                    Handover.value -> {
                        Handover
                    }
                    Concession.value -> {
                        Concession
                    }
                    else -> {
                        PENDING
                    }
                }
            }
        }
    }

    var status: Status? = null
    var content: CharSequence? = null
    var date: String? = null

    /**
     * 质检生产操作记录
     */
    class BuilderCheck {
        //操作人员
        private var userCode: String? = null
        private var userName: String? = null
        private var date: String? = null
        private var status: Status? = null

        fun optUser(name: String?, code: String?): BuilderCheck {
            this.userCode = code
            this.userName = name;
            return this
        }

        fun date(date: String?): BuilderCheck {
            this.date = date
            return this;
        }

        fun date(date: Long?): BuilderCheck {
            this.date = DateUtils.replaceYYYY_MM_dd_HHmmss(date)
            return this;
        }

        fun status(status: Status?): BuilderCheck {
            this.status = status
            return this
        }

        fun build(): ProcessRecodeModel {
            val recodeModel = ProcessRecodeModel()
            recodeModel.date = this.date
            recodeModel.status = status
            val user = getRedSpanString("${userName}(${userCode})")
            val append = SpannableStringBuilder().append("操作人员")
                .append(user)
            recodeModel.content = append
            return recodeModel
        }

        private fun getRedSpanString(text: String?): SpannableString {
            val spannableString = SpannableString(text)
            val foregroundColorSpan = ForegroundColorSpan(Color.RED)
            spannableString.setSpan(
                foregroundColorSpan,
                0,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                UnderlineSpan(),
                0,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannableString
        }
    }


    class BuilderEmployee {
        private var userCode: String? = null
        private var userName: String? = null
        private var date: String? = null
        private var status: Status? = null
        private var devCode: String? = null
        private var devName: String? = null
        private var employeeCode: String? = null
        private var employeeName: String? = null

        fun optUser(name: String?, code: String?): BuilderEmployee {
            this.userCode = code
            this.userName = name;
            return this
        }

        fun employee(name: String?, code: String?): BuilderEmployee {
            this.employeeCode = code
            this.employeeName = name;
            return this;
        }

        fun date(date: String?): BuilderEmployee {
            this.date = date
            return this;
        }

        fun date(date: Long?): BuilderEmployee {
            this.date = DateUtils.replaceYYYY_MM_dd_HHmmss(date)
            return this;
        }

        fun optDev(name: String?, code: String?): BuilderEmployee {
            this.devCode = code
            this.devName = name;
            return this
        }


        fun status(status: Status?): BuilderEmployee {
            this.status = status
            return this
        }

        fun build(): ProcessRecodeModel {
            val recodeModel = ProcessRecodeModel()
            recodeModel.date = this.date
            recodeModel.status = status


            recodeModel.content = when (status) {
                Status.ADD -> {
                    val user = getRedSpanString("${userName}(${userCode})")

                    val append = SpannableStringBuilder().append("操作人员")
                        .append(user)
                        .append(",添加人员")
                    append
                }
                Status.PENDING -> {
                    val user = getRedSpanString("${userName}(${userCode})")

                    val append = SpannableStringBuilder().append("操作人员")
                        .append(user)
                        .append(",进行了派工操作")
                    append
                }
                Status.Handover -> {
                    val user = getRedSpanString("${userName}(${userCode})")

                    val append = SpannableStringBuilder().append("操作人员")
                        .append(user)
                        .append(",进行交接操作.")
                    val employee = getRedSpanString("${employeeName}(${employeeCode})")
                    append.append("将任务交接给:")
                        .append(employee)
                    append
                }
                Status.START, Status.PAUSE, Status.END -> {
                    val dev = getRedSpanString("${devName}(${devCode})")
                    val user = getRedSpanString("${userName}(${userCode})")

                    val append = SpannableStringBuilder().append("操作人员")
                        .append(user)
                        .append(",操作设备:")
                        .append(dev)
                    val employee = getRedSpanString("${employeeName}(${employeeCode})")
                    append.append("负责人:")
                        .append(employee)
                    append
                }


                else -> null
            }
            return recodeModel
        }

        private fun getRedSpanString(text: String?): SpannableString {
            val spannableString = SpannableString(text)
            val foregroundColorSpan = ForegroundColorSpan(Color.RED)
            spannableString.setSpan(
                foregroundColorSpan,
                0,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                UnderlineSpan(),
                0,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannableString
        }

    }

    class BuilderGroup {
        private var userCode: String? = null
        private var userName: String? = null
        private var date: String? = null
        private var status: Status? = null
        private var devCode: String? = null
        private var devName: String? = null
        private var groupCode: String? = null
        private var groupName: String? = null

        fun optUser(name: String?, code: String?): BuilderGroup {
            this.userCode = code
            this.userName = name;
            return this
        }

        fun date(date: String?): BuilderGroup {
            this.date = date
            return this;
        }

        fun date(date: Long?): BuilderGroup {
            this.date = DateUtils.replaceYYYY_MM_dd_HHmmss(date)
            return this;
        }

        fun optDev(name: String?, code: String?): BuilderGroup {
            this.devCode = code
            this.devName = name;
            return this
        }

        fun optGroup(name: String?, code: String?): BuilderGroup {
            this.groupCode = code
            this.groupName = name;
            return this
        }

        fun status(status: Status?): BuilderGroup {
            this.status = status
            return this
        }

        fun build(): ProcessRecodeModel {
            val recodeModel = ProcessRecodeModel()
            recodeModel.date = this.date
            recodeModel.status = status


            recodeModel.content = when (status) {
                Status.ADD -> {
                    val user = getRedSpanString("${userName}(${userCode})")

                    val append = SpannableStringBuilder().append("操作人员")
                        .append(user)
                        .append(",添加人员")
                    append
                }
                Status.PENDING -> {
                    val user = getRedSpanString("${userName}(${userCode})")

                    val append = SpannableStringBuilder().append("操作人员")
                        .append(user)
                        .append(",进行了派工操作")
                    append
                }
                Status.START, Status.PAUSE, Status.END -> {
                    val dev = getRedSpanString("${devName}(${devCode})")
                    val user = getRedSpanString("${userName}(${userCode})")

                    val append = SpannableStringBuilder().append("操作人员")
                        .append(user)
                        .append(",操作设备:")
                        .append(dev)

                    val group = getRedSpanString("${groupName}(${groupCode})")
                    append.append("当前组名称:")
                        .append(group)

                    append
                }
                Status.Handover -> {
                    val user = getRedSpanString("${userName}(${userCode})")

                    val append = SpannableStringBuilder().append("操作人员")
                        .append(user)
                        .append(",进行交接操作.")
                    val group = getRedSpanString("${groupName}(${groupCode})")
                    append.append("交接给组:")
                        .append(group)
                    append
                }


                else -> null
            }
            return recodeModel
        }

        private fun getRedSpanString(text: String?): SpannableString {
            val spannableString = SpannableString(text)
            val foregroundColorSpan = ForegroundColorSpan(Color.RED)
            spannableString.setSpan(
                foregroundColorSpan,
                0,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                UnderlineSpan(),
                0,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannableString
        }

    }
}