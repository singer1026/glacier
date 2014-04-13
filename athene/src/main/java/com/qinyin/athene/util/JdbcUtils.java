/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-2
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import org.apache.commons.lang.StringUtils;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtils {
    public static String convertColumnName(String columnName) throws SQLException {
        if (StringUtils.isBlank(columnName)) {
            return "unknownColumn";
        }
        if (!columnName.contains("_")) return columnName;
        StringBuilder sb = new StringBuilder();
        boolean previous = false;
        for (int i = 0; i < columnName.length(); i++) {
            char c = columnName.charAt(i);
            if (i == 0) {
                sb.append(c);
                continue;
            }
            if (c == 95) {
                previous = true;
                continue;
            }
            if (previous) {
                sb.append(Character.toUpperCase(c));
                previous = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static Object getResultSetValue(ResultSet rs, String key) throws SQLException {
        Object obj = rs.getObject(key);
        if (obj == null) return null;
        if (obj instanceof Blob) {
            obj = rs.getBytes(key);
        } else if (obj instanceof Clob) {
            obj = rs.getString(key);
        } else if (obj instanceof java.sql.Date) {
            obj = rs.getTimestamp(key);
        }
        return obj;
    }

}
