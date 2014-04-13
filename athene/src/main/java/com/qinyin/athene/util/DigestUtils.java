/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-16
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import org.apache.commons.codec.binary.Base64;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DigestUtils {
    public static String getInitPassword() {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex("123456");
    }

    public static String encodeBase64(String s) {
        byte[] base64 = Base64.encodeBase64(s.getBytes(), true);
        return new String(base64);
    }

    public static String decodeBase64(String base64) {
        byte[] bytes = Base64.decodeBase64(base64);
        return new String(bytes);
    }

    public static String md5Hex(String s) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(s);
    }

    public static String formatDecimal(BigDecimal bigDecimal) {
        DecimalFormat decimalFormat = new DecimalFormat("###,##0.0");
        return decimalFormat.format(bigDecimal);
    }

    public static void main(String[] args) {
        String p = org.apache.commons.codec.digest.DigestUtils.md5Hex("83112643");
        System.out.println(p);
    }
}
