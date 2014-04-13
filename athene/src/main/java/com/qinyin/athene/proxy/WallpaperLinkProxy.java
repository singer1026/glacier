package com.qinyin.athene.proxy;

import com.qinyin.athene.cache.model.WallpaperLinkCacheBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface WallpaperLinkProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public WallpaperLinkCacheBean queryByFilePath(String filePath);
}
