/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-9-13
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PermissionUtils {
    public static Logger log = LoggerFactory.getLogger(PermissionUtils.class);

    public static boolean hasBaseRole(String loginId, String baseRoleName) {
        Privilege pvg = (Privilege) BeanFactoryHolder.getBean("pvg");
        List<String> userBaseRoleList = pvg.getBaseRoleListByLoginId(loginId);
        return contain(userBaseRoleList, baseRoleName);
    }

    public static boolean contain(List src, List tar) {
        if (src == null || src.size() == 0) return false;
        if (tar == null || tar.size() == 0) return false;
        for (Object srcObj : src) {
            for (Object tarObj : tar) {
                if (srcObj.equals(tarObj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contain(List src, String tar) {
        if (src == null || src.size() == 0) return false;
        if (StringUtils.isBlank(tar)) return false;
        for (Object srcObj : src) {
            if (srcObj.equals(tar)) {
                return true;
            }
        }
        return false;
    }
}
