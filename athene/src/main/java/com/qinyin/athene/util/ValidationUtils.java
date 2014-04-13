/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean isEmail(String email) {
        String regx = "^([a-z0-9A-Z]+[-\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(email).matches();
    }

    public static boolean isMobile(String mobile) {
        String regx = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(mobile).matches();
    }

    public static boolean isTelephone(String telephone) {
        String regx = "[0-9]{8}";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(telephone).matches();
    }

    public static boolean isChinese(String s) {
        String regx = "[\u4e00-\u9fa5]+";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(s).matches();
    }

    public static boolean isAllowedChinese(String s, int maxLength) {
        String regx = "[\u4e00-\u9fa5]{1," + maxLength + "}";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(s).matches();
    }

    public static boolean isAllowedLength(String s, int maxLength) {
        return s.length() <= maxLength ? true : false;
    }

    public static boolean isLoginId(String s) {
        String regx = "[a-z]+\\.[a-z]+\\d*";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(s).matches();
    }

    public static boolean isQQ(String s) {
        String regx = "[1-9][0-9]{4,9}";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(s).matches();
    }

    public static boolean isBigDecimal(String s) {
        boolean flag = false;
        try {
            BigDecimal b = new BigDecimal(s);
            flag = true;
        } catch (Exception e) {
        }
        return flag;
    }

    public static boolean isAllowBigDecimal(String s, int place) {
        String regx = "[0-9]+\\.[0-9]{1," + place + "}";
        Pattern pattern = Pattern.compile(regx);
        if (pattern.matcher(s).matches()) {
            return true;
        } else {
            String regx2 = "[0-9]+";
            Pattern pattern2 = Pattern.compile(regx2);
            return pattern2.matcher(s).matches();
        }
    }

    public static boolean isAllowPassword(String s) {
        String regx = "[a-z0-9]{8,}";
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(s).matches();
    }

    public static void main(String[] args) {
        boolean a = ValidationUtils.isAllowPassword("rrrrrrrr");
        System.out.println(a);
    }
}
