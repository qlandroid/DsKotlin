package com.feiling.dasong.uitils

import android.graphics.drawable.InsetDrawable
import java.text.SimpleDateFormat
import java.util.*

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2020/5/18
 * @author ql
 */
object DateUtils {

    fun replaceYYYYMMdd(date: Long?): String? {
        if (date == null) {
            return ""
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(Date(date!!))
    }


    fun replaceYYYY_MM_dd_HHmmss(date: Long?): String? {
        if (date == null) {
            return ""
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return simpleDateFormat.format(Date(date!!))
    }


    fun getDayEndByDate(date: Date): Date {
        var instance = Calendar.getInstance()
        instance.time = date
        var year = instance.get(Calendar.YEAR)
        var month = instance.get(Calendar.MONTH)
        var day = instance.get(Calendar.DAY_OF_MONTH)
        return getDayEnd(year, month, day)
    }

    fun getDayStartByDate(date: Date): Date {
        var instance = Calendar.getInstance()
        instance.time = date
        var year = instance.get(Calendar.YEAR)
        var month = instance.get(Calendar.MONTH)
        var day = instance.get(Calendar.DAY_OF_MONTH)
        return getDayStart(year, month, day)
    }

    fun getDayEnd(year: Int, month: Int, day: Int): Date {
        var instance = Calendar.getInstance()
        instance.set(year, month, day, 23, 59, 59)
        return instance.time;
    }

    fun getDayStart(year: Int, month: Int, day: Int): Date {
        var instance = Calendar.getInstance()
        instance.set(year, month, day, 0, 0, 0)
        return instance.time;
    }

    fun getFirstDayStartFormMonthByDate(date: Date): Date {
        var instance = Calendar.getInstance()
        instance.time = date;
        instance.set(Calendar.DAY_OF_MONTH, 1);

        return getDayStartByDate(instance.time)
    }
}