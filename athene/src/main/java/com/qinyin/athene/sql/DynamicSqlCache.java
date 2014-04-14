/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-1
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.sql;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicSqlCache {
    public static Logger log = LoggerFactory.getLogger(DynamicSqlCache.class);
    private Map<String, DynamicSqlCacheBean> sqlCache;
    private Map<String, Long> fileCache;
    private String path;

    public static class DynamicSqlCacheHolder {
        private static DynamicSqlCache instance = new DynamicSqlCache();
    }

    public static DynamicSqlCache getInstance() {
        return DynamicSqlCacheHolder.instance;
    }

    public void init(String path) throws Exception {
        this.path = path;
        sqlCache = new ConcurrentHashMap<String, DynamicSqlCacheBean>();	//sql 缓存 map
        /**
         *文件缓存 ConcurrentHashMap  
         *Java 5中支持高并发、高吞吐量的线程安全HashMap实现
         */
        fileCache = new ConcurrentHashMap<String, Long>();	
        File dir = null;
        if (StringUtils.isBlank(path)) {
            ClassLoader classLoader = this.getClass().getClassLoader();
            URI uri = classLoader.getResource("sql").toURI();
            dir = new File(uri);
        } else {
            dir = new File(path);
        }
        File[] files = dir.listFiles(new XmlFilter());
        if (files != null && files.length > 0) {
            for (File file : files) {
                parseOneSqlXml(file);
                fileCache.put(file.getName(), Long.valueOf(file.lastModified()));
            }
        }
    }

    public void refreshSqlCache() throws Exception {
        File dir = null;
        if (StringUtils.isBlank(path)) {
            ClassLoader classLoader = this.getClass().getClassLoader();
            URI uri = classLoader.getResource("sql").toURI();	//获取源文件下的sql 文件夹的路径
            dir = new File(uri);
        } else {
            dir = new File(path);
        }
        File[] files = dir.listFiles(new XmlFilter()); //获取sql 文件夹下的所有文件
        if (files != null && files.length > 0) {
            for (File file : files) {
                long lastModified = file.lastModified();	//获取当前文件的最后修改的时间
                //判断 fileCache 文件缓存类，是否包含对应文件或者 修改的时间是否相同
                if (fileCache.get(file.getName()) == null ||
                        fileCache.get(file.getName()).longValue() != lastModified) {
                    log.debug(file.getName() + " has changed , reload now");
                    parseOneSqlXml(file);
                    fileCache.put(file.getName(), Long.valueOf(lastModified));
                }
            }
        }
    }

    private void parseOneSqlXml(File file) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        if (StringUtils.equals(root.getName(), "sqls")) {
            String fileName = file.getName();
            String idPrefix = fileName.substring(0, fileName.length() - ".sql".length());
            for (Iterator i = root.elementIterator("sql"); i.hasNext(); ) {
                Element sql = (Element) i.next();
                String text = sql.getTextTrim();
                String id = idPrefix + "." + sql.attributeValue("id");
                String type = sql.attributeValue("type");
                DynamicSqlCacheBean bean = new DynamicSqlCacheBean();
                bean.setId(id);
                bean.setContent(text);
                bean.setDescription(sql.attributeValue("description"));
                bean.setType(type == null ? "velocity" : type);
                sqlCache.put(id, bean);
            }
        }
    }

    public class XmlFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".xml") ? true : false;
        }
    }

    public DynamicSqlCacheBean getSql(String key) {
        return sqlCache.get(key);
    }
}
