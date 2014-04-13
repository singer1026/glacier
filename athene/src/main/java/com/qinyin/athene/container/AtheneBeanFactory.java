/**
 * @author zhaolie
 * @create-time 2010-11-24
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.container;

import javax.servlet.ServletConfig;
import java.util.List;

/**
 * IOC容器接口
 */
public interface AtheneBeanFactory {
    /**
     * 初始化IOC容器
     *
     * @param config
     */
    public void init(ServletConfig config);

    /**
     * 获取所有容器注册的bean
     *
     * @return
     */

    public List<Object> getAllBeans();

    public Object getBean(String name);

    /**
     * 销毁IOC容器
     */
    public void destroy();
}
