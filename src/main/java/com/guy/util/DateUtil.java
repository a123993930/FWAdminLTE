package com.guy.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 功能描述
 *
 * @author Administrator
 * @version 1.0
 * @Date Jul 19, 2008
 * @Time 9:47:53 AM
 */
public class DateUtil {

//	 private static List<Calendar> holidayList=null;


    public static Date date = null;

    public static DateFormat dateFormat = null;

    public static Calendar calendar = null;

    /**
     * 英文简写（默认）如：09/22/2008 16:33:00
     */
    public static String FORMAT_UNIX = "yyyy/MM/dd HH:mm:ss";
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";
    /**
     * 英文简写（默认）如：2010-12
     */
    public static String FORMAT_SHORT_MONTH = "yyyy-MM";

    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

    /**
     * 英文全称 如：2010-12-01 23:15
     */
    public static String FORMAT_LONG_S = "yyyy-MM-dd HH:mm";
    /**
     * 英文全称 如：20101201231506
     */
    public static String FORMAT_LONG_F = "yyyyMMddHHmmss";
    public static String FORMAT_LONG_SH = "yyyyMMdd";

    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 中文简写 如：2010年12月01日
     */
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd日";
    /**
     * 年份 如：2010
     */
    public static String FORMAT_YEAR = "yyyy";

    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";

    /**
     * 精确到毫秒的完整中文时间 yyyy年MM月dd日  HH时mm分ss秒SSS毫秒
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
    /**
     * 精确到毫秒的完整中文时间 yyyy年MM月
     */
    public static String FORMAT_S_CN = "yyyy年MM月";
    /**
     * 精确到毫秒的完整中文时间 yyyy年MM月
     */
    public static String FORMAT_YEAR_CN = "yyyy年";

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return FORMAT_LONG_S;
    }

    /**
     * 根据预设格式返回当前日期
     *
     * @return
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     *
     * @param format
     * @return
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }

    /**
     * 使用预设格式格式化日期
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, getDatePattern());
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 获取距现在某一小时的时刻
     *
     * @param format 格式化时间的格式
     * @param h      距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * @return
     */
    public static String getpreHour(String format, int h) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(date.getTime() + h * 60 * 60 * 1000);
        return sdf.format(date);
    }

    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取日期年份
     *
     * @param date 日期
     * @return
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 功能描述：返回小
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 功能描述：返回毫
     *
     * @param date 日期
     * @return 返回毫
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 获取时间戳 微信
     *
     * @param date
     * @return
     */
    public static long UNIXtimestamp(String date) {
        return parse(date, "yyyy/MM/dd HH:mm:ss").getTime() / 1000;
    }

    public static Calendar paseCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 校验指定的日期是否在节日列表中 具体节日包含哪些,可以在HolidayMap中修改
     *
     * @param src 要校验的日期(源)
     * @version [s001, 2010-11-19]
     * @author shenjunjie
     */
    public static boolean checkHoliday(Calendar src) {
        boolean result = false;
        // if (holidayList == null)
        // {
        // initHolidayList();
        // }
        // 先检查是否是周六周日(有些国家是周五周六)
        if (src.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || src.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        // for (Calendar c : holidayList)
        // {
        // if (src.get(Calendar.MONTH) == c.get(Calendar.MONTH)
        // && src.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH))
        // {
        // result = true;
        // }
        // }
        return result;
    }

    /**
     * 把long 转换成 日期 再转换成String类型
     */
    public static String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     * string转换为timestamp
     * @param dateFormat
     * @param str
     * @return
     */
    public static Timestamp transferStringToTimestamp(String dateFormat, String str) {
        SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        Timestamp t1 = null;
        try {
            Date date = sf.parse(str);
            System.out.println(date);
            t1 = new Timestamp(date.getTime());
            System.out.println(t1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t1;
    }
//	/**
//     * 初始化节日List,如果需要加入新的节日,请在这里添加
//     * 加的时候请尽量使用Calendar自带的常量而不是魔鬼数字
//     * 注:年份可以随便写,因为比的时候只比月份和天
//     * @version  [s001, 2010-11-19]
//     * @author  shenjunjie
//     */
//    private static void initHolidayList()
//    {
//        holidayList = new ArrayList<Calendar>();
// 
//        //五一劳动节
//        Calendar may1 = Calendar.getInstance();
//        may1.set(Calendar.MONTH,Calendar.MAY);
//        may1.set(Calendar.DAY_OF_MONTH,1);
//        holidayList.add(may1);
// 
//        Calendar may2 = Calendar.getInstance();
//        may2.set(Calendar.MONTH,Calendar.MAY);
//        may2.set(Calendar.DAY_OF_MONTH,2);
//        holidayList.add(may2);
// 
//        Calendar may3 = Calendar.getInstance();
//        may3.set(Calendar.MONTH,Calendar.MAY);
//        may3.set(Calendar.DAY_OF_MONTH,3);
//        holidayList.add(may3);
// 
//        Calendar h3 = Calendar.getInstance();
//        h3.set(2000, 1, 1);
//        holidayList.add(h3);
// 
//        Calendar h4 = Calendar.getInstance();
//        h4.set(2000, 12, 25);
//        holidayList.add(h4);
// 
//        //中国母亲节：五月的第二个星期日
//        Calendar may5 = Calendar.getInstance();
//        //设置月份为5月
//        may5.set(Calendar.MONTH,Calendar.MAY);
//        //设置星期:第2个星期
//        may5.set(Calendar.DAY_OF_WEEK_IN_MONTH,2);
//        //星期日
//        may5.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
////        System.out.println(may5.getTime());
// 
//        holidayList.add(may5);
//    }
}