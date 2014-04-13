/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-6-11
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.comparator;

import com.qinyin.athene.util.ParamUtils;

import java.util.Comparator;
import java.util.Map;

public class StudentExcelComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Map<String, Object> studentMap1 = (Map<String, Object>) o1;
        Map<String, Object> studentMap2 = (Map<String, Object>) o2;
        String subjectName1 = ParamUtils.getString(studentMap1.get("subjectName"));
        String subjectName2 = ParamUtils.getString(studentMap2.get("subjectName"));
        if (subjectName1 == null) return 1;
        if (subjectName2 == null) return -1;
        return subjectName2.compareTo(subjectName1);
    }
}
