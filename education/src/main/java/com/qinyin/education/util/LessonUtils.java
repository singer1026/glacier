/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-3-27
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.util;

public class LessonUtils {
    public static final int MINUTE_OF_HOUR = 60;
    public static final int MINUTE_OF_DAY = MINUTE_OF_HOUR * 24;
    public static final int MINUTE_OF_WEEK = MINUTE_OF_DAY * 7;
    public static final int MINUTE_OF_HALF_DAY = MINUTE_OF_HOUR * 12;

    public static int generateTime(int dayOfWeek, int hour, int minute) {
        int time = dayOfWeek * MINUTE_OF_DAY;
        time += (hour * MINUTE_OF_HOUR);
        return time += minute;
    }

    public static int generateEndTime(int startTime, int timeLength) {
        return startTime + timeLength;
    }

    public static boolean isSameDay(int startTime, int endTime) {
        int d1 = startTime / MINUTE_OF_DAY;
        int d2 = endTime / MINUTE_OF_DAY;
        return d1 == d2 ? true : false;
    }

    public static int computeHour(int time) {
        int rm = time % MINUTE_OF_DAY;
        return rm / MINUTE_OF_HOUR;
    }

    public static int computeMinute(int time) {
        int rm = time % MINUTE_OF_DAY;
        int hour = rm / MINUTE_OF_HOUR;
        return rm % MINUTE_OF_HOUR;
    }

    public static String formatTime(int time) {
        int rm = time % MINUTE_OF_DAY;
        int hour = rm / MINUTE_OF_HOUR;
        int minute = rm % MINUTE_OF_HOUR;
        StringBuilder sb = new StringBuilder();
        if (hour < 10) sb.append("0");
        sb.append(hour).append(":");
        if (minute < 10) sb.append("0");
        sb.append(minute);
        return sb.toString();
    }

    public static boolean isAMLesson(Integer startTime) {
        int rm1 = startTime % MINUTE_OF_DAY;
        if (rm1 >= MINUTE_OF_HALF_DAY) {
            return false;
        } else {
            return true;
        }
    }
}
