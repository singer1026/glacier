/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.sql;

import com.qinyin.athene.util.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DynamicSqlManagerImpl implements DynamicSqlManager {
    private Properties properties;
    private TemplateFactory VelocityTemplateFactory;

    public void init() throws Exception {
        properties = new Properties();
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream in = classLoader.getResource("dynamicSql.properties").openStream();
        properties.load(in);
        //遍历获取 dynamicSql.properties 包含 velocity. 开始的内容
        Properties vmProperties = fetchVelocityProperties(properties);
       //初始化DynamicSqlCache  获取模板的sql 配置路径
        DynamicSqlCache.getInstance().init(vmProperties.getProperty("cache.sql.path"));	
        VelocityTemplateFactory.init(vmProperties);
        String intervalS = vmProperties.getProperty("cache.check.interval");	// 检查模板缓存的间隔
        if (StringUtils.isNotBlank(intervalS)) {
            long intervalL = Long.parseLong(intervalS);
            if (intervalL > 0) {
            	//间隔5秒 开启线程  动态扫描 sql 缓存 进行刷新
                DynamicSqlCacheScaner scaner = new DynamicSqlCacheScaner(intervalL * 1000);
                Thread thread = new Thread(scaner);
                thread.start();
            }
        }
    }

    private Properties fetchVelocityProperties(Properties properties) {
        Properties p = new Properties();
        Enumeration names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (name.startsWith("velocity.")) {
                String newName = name.substring("velocity.".length());
                p.put(newName, properties.getProperty(name));
            }
        }
        return p;
    }


    public String getSql(String key, Object obj) {
        SqlTemplate template = VelocityTemplateFactory.getTemplate(key);
        if (template == null) return null;
        Map<String, Object> param = new HashMap<String, Object>();
        if (obj instanceof Map) {
            param = (Map) obj;
        } else {
            param = BeanUtils.toMap(obj);
        }
        return template.merge(param);
    }

    public void setVelocityTemplateFactory(TemplateFactory velocityTemplateFactory) {
        VelocityTemplateFactory = velocityTemplateFactory;
    }
}
