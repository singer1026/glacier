/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-24
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import com.qinyin.athene.exception.HackerException;
import com.qinyin.athene.exception.OptimisticLockException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

public class CommonUtils {
    public static Logger log = LoggerFactory.getLogger(CommonUtils.class);

    public static String getExceptionDetail(Exception e) {
        Throwable throwable = e;
        if (e instanceof InvocationTargetException) {
            throwable = ((InvocationTargetException) e).getTargetException();
        }
        if (throwable instanceof OptimisticLockException || throwable instanceof HackerException) {
            return throwable.getMessage();
        } else {
            StringWriter sw = null;
            PrintWriter pw = null;
            try {
                sw = new StringWriter();
                pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                pw.flush();
                sw.flush();
                return sw.toString();
            } catch (Exception exp) {
                log.error("catch exception", exp);
            } finally {
                try {
                    if (pw != null) pw.close();
                    if (sw != null) sw.close();
                } catch (IOException e1) {
                    log.error("catch exception", e1);
                }
            }
        }
        return StringUtils.EMPTY;
    }
}
