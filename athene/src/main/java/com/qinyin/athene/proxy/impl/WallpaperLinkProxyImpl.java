/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-15
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.proxy.impl;

import com.qinyin.athene.cache.model.WallpaperLinkCacheBean;
import com.qinyin.athene.cache.store.WallpaperLinkStore;
import com.qinyin.athene.model.AppWallpaperLink;
import com.qinyin.athene.proxy.WallpaperLinkProxy;
import com.qinyin.athene.service.WallpaperLinkService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WallpaperLinkProxyImpl implements WallpaperLinkProxy {
    public static Logger log = LoggerFactory.getLogger(WallpaperLinkProxyImpl.class);
    private WallpaperLinkService wallpaperLinkService;
    private WallpaperLinkStore wallpaperLinkStore;

    @Override
    public WallpaperLinkCacheBean queryByFilePath(String filePath) {
        if (StringUtils.isBlank(filePath)) return null;
        WallpaperLinkCacheBean bean = wallpaperLinkStore.get(filePath);
        if (bean == null) {
            AppWallpaperLink dbAppWallpaperLink = wallpaperLinkService.queryById(filePath);
            bean = new WallpaperLinkCacheBean(dbAppWallpaperLink);
            wallpaperLinkStore.put(filePath, bean);
        }
        return bean;
    }

    public void setWallpaperLinkService(WallpaperLinkService wallpaperLinkService) {
        this.wallpaperLinkService = wallpaperLinkService;
    }

    public void setWallpaperLinkStore(WallpaperLinkStore wallpaperLinkStore) {
        this.wallpaperLinkStore = wallpaperLinkStore;
    }
}
