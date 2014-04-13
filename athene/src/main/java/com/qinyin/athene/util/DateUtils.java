/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-3
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date parseDate(String s) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
        }
        return date;
    }

    public static Date parseDate(String s, String format) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String formatDateTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static int compare(Date date1, Date date2) {
        if (date1 == null) return -1;
        if (date2 == null) return 1;
        long l1 = date1.getTime();
        long l2 = date2.getTime();
        if (l1 > l2) {
            return 1;
        } else if (l1 == l2) {
            return 0;
        } else {
            return -1;
        }
    }

    public static Date getNow() {
        return new Date();
    }

    public static Date getNowByMonth(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, count);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        Date a = DateUtils.getToday();
        String b = DateUtils.formatDate(a, "yyyy年MM月");
        System.out.println(b);
    }

    public static Date nextDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Date previousWeek(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        return calendar.getTime();
    }

    public static Date previousDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date cloneDate(Date date) {
        long m = date.getTime();
        return new Date(m);
    }

    public static Timestamp cloneTimestamp(Date date) {
        long m = date.getTime();
        return new Timestamp(m);
    }

    public static boolean isToday(Date date) {
        Date today = DateUtils.getToday();
        return DateUtils.compare(date, today) >= 0;
    }
}
