/**
 * @author zhaolie
 * @create-time 2010-11-27
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.singleton;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.util.*;

public class SystemConfiguration {
    public static Logger log = LoggerFactory.getLogger(SystemConfiguration.class);
    private final String configFileName = "glacier.properties";
    private Properties properties = new Properties();
    private List<String> beanFilterList = new ArrayList<String>();

    private static class ConfigurationHolder {
        private static SystemConfiguration instance = new SystemConfiguration();
    }

    public static SystemConfiguration getInstance() {
        return ConfigurationHolder.instance;
    }

    public void reloadConfig(ServletContext servletContext) throws ServletException {
        try {
            StringBuilder path = new StringBuilder();
            path.append("/").append("WEB-INF").append("/").append(configFileName);
            //path.append("/").append("WEB-INF").append("/").append("classes").append("/").append(configFileName);
            InputStream inStream = servletContext.getResourceAsStream(path.toString());
            properties.clear();
            properties.load(inStream);
            Iterator it = properties.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                log.info("load property " + entry.getKey() + "[" + entry.getValue() + "]");
                if (entry.getKey().toString().startsWith("bean.filter")) {
                    String[] beans = entry.getValue().toString().split(",");
                    for (String bean : beans) {
                        if (StringUtils.isNotBlank(bean)) {
                            beanFilterList.add(bean);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("can't load config , Exception is " + e);
            throw new ServletException();
        }

    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public boolean doFilter(String name) {
        for (String bean : beanFilterList) {
            if (name.endsWith(bean)) {
                return false;
            }
        }
        return true;
    }
}