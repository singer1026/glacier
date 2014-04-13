/**
 * @author zhaolie
 * @create-time 2010-11-24
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.container;

import com.qinyin.athene.singleton.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import java.util.ArrayList;
import java.util.List;

public class SpringBeanFactory implements AtheneBeanFactory {
    public static Logger log = LoggerFactory.getLogger(SpringBeanFactory.class);
    private ApplicationContext ac;

    @Override
    public void init(ServletConfig config) {
        ac = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
    }

    @Override
    public List<Object> getAllBeans() {
        List<Object> list = new ArrayList<Object>();
        String[] beanNames = ac.getBeanDefinitionNames();
        if (beanNames != null) {
            for (String beanName : beanNames) {
                if (SystemConfiguration.getInstance().doFilter(beanName)) {
                    Object bean = ac.getBean(beanName);
                    list.add(bean);
                }
            }
        }
        return list;
    }

    public Object getBean(String name) {
        return ac.getBean(name);
    }

    @Override
    public void destroy() {
    }
}
