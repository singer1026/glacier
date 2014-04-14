/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-4-20
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.sql;


public class DynamicSqlCacheScaner implements Runnable {
    private long interval = 0;

    public DynamicSqlCacheScaner(long interval) {
        this.interval = interval;
    }

    public void run() {
        while (true) {
            try {
            		//刷新sql 缓存
                DynamicSqlCache.getInstance().refreshSqlCache();
                Thread.sleep(interval);
            } catch (Exception e) {
            }
        }
    }
}
