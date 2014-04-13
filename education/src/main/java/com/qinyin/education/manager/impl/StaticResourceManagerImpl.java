/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-10
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.manager.impl;

import com.qinyin.education.manager.StaticResourceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticResourceManagerImpl implements StaticResourceManager {
    private Map<String, Object> staticResource = new ConcurrentHashMap<String, Object>();

    @Override
    public void init() {
        List<Map<String, Object>> hourList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 24; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String k = String.valueOf(i);
            map.put("name", k);
            map.put("value", k);
            hourList.add(map);
        }
        staticResource.put("hourList", hourList);
        List<Map<String, Object>> minuteList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 60; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String k = String.valueOf(i);
            map.put("name", k);
            map.put("value", k);
            minuteList.add(map);
        }
        staticResource.put("minuteList", minuteList);
    }

    @Override
    public Object get(String key) {
        return staticResource.get(key);
    }
}
