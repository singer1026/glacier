/**
 * @author zhaolie
 * @version 1.0
 * @create-time 2010-5-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

public class SystemUtils {
    public static String getOsInfo() {
        return System.getProperty("os.name") + " " + System.getProperty("os.version");
    }

    public static String getOsSprit() {
        if (System.getProperty("os.name").contains("Windows")) {
            return "/";
        } else {
            return "\\";
        }
    }

}
