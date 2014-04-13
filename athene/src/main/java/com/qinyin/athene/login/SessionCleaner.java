/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-4-3
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.login;

import com.qinyin.athene.singleton.BeanFactoryHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionCleaner implements Runnable {
    public static Logger log = LoggerFactory.getLogger(SessionCleaner.class);

    @Override
    public void run() {
        while (true) {
            try {
                SessionFactory sessionFactory = (SessionFactory) BeanFactoryHolder.getBean("sessionFactory");
                sessionFactory.cleanSession();
                Thread.sleep(60000);
            } catch (InterruptedException e) {
//                log.error("catch exception ", e);
            }
        }
    }
}
