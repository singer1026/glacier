/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-21
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

public class StringEscapeUtils {
    public static String htmlEncode(String source) {
        if (source == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case 10:
                    sb.append("<br/>");
                    break;
                case 32:
                    sb.append("&nbsp;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
