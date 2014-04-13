/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.dao;

import com.qinyin.athene.model.AppUrl;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class JdbcDaoTest extends TestCase {
    private String[] files = {"applicationContext.xml"};
    private ClassPathXmlApplicationContext ac = null;
    private JdbcDao jdbcDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ac = new ClassPathXmlApplicationContext(files);
        jdbcDao = (JdbcDao) ac.getBean("jdbcDao");
    }

    public void testDao() {
    		assertNotNull(jdbcDao);
    }

    /*public void testCommon() {

    }*/

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
