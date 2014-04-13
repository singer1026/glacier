/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.cache.model;

import com.qinyin.athene.model.AppWallpaperLink;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WallpaperLinkCacheBean extends AppWallpaperLink {
    public static Logger log = LoggerFactory.getLogger(WallpaperLinkCacheBean.class);

    public WallpaperLinkCacheBean(AppWallpaperLink appWallpaperLink) {
        try {
            if (appWallpaperLink == null) return;
            PropertyUtils.copyProperties(this, appWallpaperLink);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public WallpaperLinkCacheBean() {
    }

}
