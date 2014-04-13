/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-26
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import java.util.*;

public class GroupListMap {
    private Map<String, List<Object>> map = new HashMap<String, List<Object>>();

    public void put(String key, Object obj) {
        List<Object> list = map.get(key);
        if (list == null) {
            List<Object> newList = new ArrayList<Object>();
            newList.add(obj);
            map.put(key, newList);
        } else {
            list.add(obj);
        }
    }

    public List<Object> get(String key) {
        return map.get(key);
    }

    public Map<String, List<Object>> getGroupListMap() {
        return map;
    }

    public List<String> getKeys() {
        Set<String> set = map.keySet();
        List<String> list = new ArrayList<String>();
        if (set != null) {
            for (String key : set) {
                list.add(key);
            }
        }
        return list;
    }
}
