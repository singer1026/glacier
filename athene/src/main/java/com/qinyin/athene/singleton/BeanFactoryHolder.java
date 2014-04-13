/**
 * @author zhaolie
 * @create-time 2010-7-3
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.singleton;

import com.qinyin.athene.container.AtheneBeanFactory;

public class BeanFactoryHolder {
    private static AtheneBeanFactory atheneBeanFactory;

    public static void setAtheneBeanFactory(AtheneBeanFactory atheneBeanFactory) {
        BeanFactoryHolder.atheneBeanFactory = atheneBeanFactory;
    }

    public static Object getBean(String name) {
        return atheneBeanFactory.getBean(name);
    }
}
