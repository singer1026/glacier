/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-4-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import freemarker.template.SimpleDate;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParamUtils {
    public static Byte getByteValue(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Byte) return (Byte) obj;
        if (obj instanceof String) return Byte.valueOf((String) obj);
        if (obj instanceof Integer) return Byte.valueOf(((Integer) obj).byteValue());
        if (obj instanceof Long) return Byte.valueOf(((Long) obj).byteValue());
        if (obj instanceof Short) return Byte.valueOf(((Short) obj).byteValue());
        return null;
    }

    public static Boolean getBooleanValue(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Boolean) return (Boolean) obj;
        if (obj instanceof String) {
            try {
                return Boolean.valueOf((String) obj);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String getString(Object obj) {
        if (obj == null) return null;
        if (obj instanceof String) {
            String s = (String) obj;
            if (StringUtils.isBlank(s)) return null;
            return s.trim();
        }
        return obj.toString();
    }

    public static Integer getInteger(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Integer) return (Integer) obj;
        if (obj instanceof String) {
            String s = (String) obj;
            if (StringUtils.isBlank(s)) return null;
            return Integer.valueOf(s.trim());
        }
        if (obj instanceof Long) {
            Long l = (Long) obj;
            return l.intValue();
        }
        return null;
    }

    public static Integer getIdInteger(Object obj) {
        Integer id = getInteger(obj);
        if (id == null) return null;
        return id > 0 ? id : null;
    }

    public static List<Object> getList(Object obj) {
        if (obj == null) return new ArrayList<Object>(0);
        if (!(obj instanceof List)) return new ArrayList<Object>(0);
        return (List) obj;
    }

    public static Date getDate(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof SimpleDate) {
            return ((SimpleDate) obj).getAsDate();
        }
        return null;
    }

    public static Timestamp getTimestamp(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Timestamp) {
            return (Timestamp) obj;
        }
        if (obj instanceof Date) {
            Date temp = (Date) obj;
            return new Timestamp(temp.getTime());
        }
        if (obj instanceof SimpleDate) {
            Date temp = ((SimpleDate) obj).getAsDate();
            return new Timestamp(temp.getTime());
        }
        return null;
    }

    public static BigDecimal getBigDecimal(Object obj) {
        if (obj == null) return null;
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        return null;
    }
}
