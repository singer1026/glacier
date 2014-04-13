/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-6-11
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.comparator;

import com.qinyin.athene.util.DateUtils;
import com.qinyin.finance.wrap.AccountStatisticsWrap;

import java.util.Comparator;

public class AccountStatisticsComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        AccountStatisticsWrap as1 = (AccountStatisticsWrap) o1;
        AccountStatisticsWrap as2 = (AccountStatisticsWrap) o2;
        return DateUtils.compare(as1.getStatisticsDate(), as2.getStatisticsDate());
    }
}
