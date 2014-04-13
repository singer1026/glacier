package com.qinyin.athene.cache.store;

import com.qinyin.athene.cache.model.WallpaperLinkCacheBean;

public interface WallpaperLinkStore extends BaseStore<WallpaperLinkCacheBean, String> {
    public final String PREFIX = "AppWallpaperLink-";
    public final long MAX_AGE = 0;
}
