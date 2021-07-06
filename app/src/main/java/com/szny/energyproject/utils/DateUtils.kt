package com.szny.energyproject.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    /**
     * 字符串转时间戳
     * 开始时间
     * @param timeString 年月日的String
     * @return 转化年月日为时间戳String（适配PHP 除以1000）-后台是java的话则不用除以1000
     */
    fun getTimeStart(timeString: String): String? {
        var timeStamp: String? = null
        val sdf = SimpleDateFormat("yyyy年MM月dd日")
        val d: Date
        try {
            d = sdf.parse(timeString)
            var l = d.time
            if (l.toString().length > 10) {
                l = l / 1000
            }
            timeStamp = l.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeStamp
    }

    /**
     * 字符串转时间戳
     * 结束时间- 追加23小时59分钟
     * @param timeString 年月日的String
     * @return 转化年月日为时间戳String（适配PHP 除以1000）-后台是java的话则不用除以1000
     */
    fun getTimeEnd(timeString: String): String? {
        var timeStamp: String? = null
        val sdf = SimpleDateFormat("yyyy年MM月dd日HH:mm")
        val d: Date
        try {
            d = sdf.parse((timeString + "23:59"))
            var l = d . time
            if (l.toString().length > 10) {
                l = l / 1000
            }
            timeStamp = l.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return timeStamp
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    fun isEffectiveDate(nowTime: Date, startTime: Date, endTime: Date): Boolean {
        if (nowTime.time === startTime.time || nowTime.time === endTime.time) {
            return true
        }
        val date = Calendar.getInstance()
        date.time = nowTime
        val begin = Calendar.getInstance()
        begin.time = startTime
        val end = Calendar.getInstance()
        end.time = endTime
        return date.after(begin) && date.before(end)
    }

    @Throws(ParseException::class)
    fun isWeekend(bDate: String?): Boolean {
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val bdate: Date = format.parse(bDate)
        val cal = Calendar.getInstance()
        cal.time = bdate
        return cal[Calendar.DAY_OF_WEEK] === Calendar.SATURDAY || cal[Calendar.DAY_OF_WEEK] === Calendar.SUNDAY
    }
}