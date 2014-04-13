/**
 * @author zhaolie
 * @create-time 2010-11-24
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import com.qinyin.athene.container.AtheneBeanFactory;
import com.qinyin.athene.singleton.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

public class AtheneFactoryUtils {
    public static Logger log = LoggerFactory.getLogger(AtheneFactoryUtils.class);

    public static AtheneBeanFactory createContainerFactory() throws ServletException {
        try {
            String name = SystemConfiguration.getInstance().getProperty("container");
            Object obj = Class.forName(name).newInstance();
            if (obj instanceof AtheneBeanFactory)
                return (AtheneBeanFactory) obj;
        } catch (Exception e) {
            log.error("can't create container factory");
            throw new ServletException();
        }
        return null;
    }
}
