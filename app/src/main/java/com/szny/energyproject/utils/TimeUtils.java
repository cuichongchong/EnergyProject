package com.szny.energyproject.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_DATE_NOYEAR = new SimpleDateFormat("MM/dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE_NOYEAR_CHINA = new SimpleDateFormat("MM月dd日 HH:mm");
    public static final SimpleDateFormat DATE_FORMAT_DATE_CHINA = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat DATE_MONTH = new SimpleDateFormat("MM.dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    public static String getTime(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }


    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /*
    * 将时间转换为时间戳
    */
    public static long dateToStamp(String s, String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String time = s;
        Date date = format.parse(time);
        return date.getTime();
    }

    /*
   * 将时间戳转换为时间
   */
    public static String stampToDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(time);
//        Date date = format.parse(d);
        return d;
    }

    /*
   * 将时间戳转换为时间
   */
    public static String stampToDateYMDHMS(long time) {
        SimpleDateFormat format = DEFAULT_DATE_FORMAT;
        String d = format.format(time);
//        Date date = format.parse(d);
        return d;
    }

    /*
   * 将时间戳转换为时间
   */
    public static String stampToDateMDHS(long time) {
        SimpleDateFormat format = DATE_FORMAT_DATE_NOYEAR;
        String d = format.format(time);
//        Date date = format.parse(d);
        return d;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDateCommon(long time, String smpleDateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(smpleDateFormat);
        String d = format.format(time);
//        Date date = format.parse(d);
        return d;
    }

    public static String getMonth(long timeInMillis) {
        return getTime(timeInMillis, DATE_MONTH);
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis * 1000L));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE_NOYEAR);
    }

    /**
     * @param timeInMillis
     * @return
     */
    public static String getTime_china(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE_NOYEAR_CHINA);
    }

    /**
     * @param timeInMillis
     * @return
     */
    public static String getTime_year_month_data(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE_NOYEAR_CHINA);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }


    //获得当天的日期
    public static String getTodayDate(String pattern) {
        String dateString = new SimpleDateFormat(pattern).format(new Date());
        return dateString;
    }

    public static long getDay(String endTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = df.parse(endTime);
            Date d2 = df.parse(getTodayDate("yyyy-MM-dd"));
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            return days;
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getDay2(String startTime, String endTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(endTime);
            Date d2 = df.parse(startTime);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            return days + "天" + hours + "小时";
        } catch (Exception e) {
            return null;
        }
    }

    //把时间戳转换为毫秒
    public static String dateTimeMs(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        long msTime = -1;
        try {
            msTime = simpleDateFormat.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf(msTime);

    }
}
