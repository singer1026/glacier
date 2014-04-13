/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-6-11
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.comparator;

import com.qinyin.athene.util.DateUtils;

import java.util.Comparator;
import java.util.Date;

public class DateMonthComparator implements Comparator {
    public static final String DATE_FORMAT = "yyyy年MM月";

    @Override
    public int compare(Object o1, Object o2) {
        String s1 = (String) o1;
        String s2 = (String) o2;
        Date d1 = DateUtils.parseDate(s1, DateMonthComparator.DATE_FORMAT);
        Date d2 = DateUtils.parseDate(s2, DateMonthComparator.DATE_FORMAT);
        return DateUtils.compare(d1, d2);
    }
}
