/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-4-20
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.sql.velocity;

import com.qinyin.athene.sql.DynamicSqlCache;
import com.qinyin.athene.sql.DynamicSqlCacheBean;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class VelocityCacheLoader extends ResourceLoader {
    public static Logger log = LoggerFactory.getLogger(VelocityCacheLoader.class);

    @Override
    public void init(ExtendedProperties configuration) {
    }

    @Override
    public InputStream getResourceStream(String id) throws ResourceNotFoundException {
        if (StringUtils.isBlank(id)) {
            throw new ResourceNotFoundException("id is blank");
        }
        InputStream result = null;
        DynamicSqlCacheBean bean = DynamicSqlCache.getInstance().getSql(id);
        if (bean != null && StringUtils.equals(bean.getType(), "velocity")) {
            try {
                result = new ByteArrayInputStream(bean.getContent().getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("catch exception ", e);
            }
        }
        return result;
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }
}
