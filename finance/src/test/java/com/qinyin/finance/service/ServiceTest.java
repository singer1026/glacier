/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.finance.service;

import com.qinyin.athene.util.DateUtils;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class ServiceTest extends TestCase {
    private String[] files = {"applicationContext.xml"};
    private ClassPathXmlApplicationContext ac = null;
    private AccountStatisticsService service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ac = new ClassPathXmlApplicationContext(files);
        service = (AccountStatisticsService) ac.getBean("accountStatisticsService");
    }

    public void testDao() {
        Date today = DateUtils.getToday();
//        service.rebuildAccountStatistics();
//        service.rebuildRemainAccountStatistics(1, today);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
