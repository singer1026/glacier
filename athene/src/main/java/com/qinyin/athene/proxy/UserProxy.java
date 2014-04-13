/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-9-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.proxy;

import com.qinyin.athene.cache.model.UserCacheBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface UserProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public UserCacheBean queryByLoginId(String loginId);

    @Transactional(rollbackFor = Exception.class)
    public int save(String type);

    @Transactional(rollbackFor = Exception.class)
    public void update(String loginId);

    @Transactional(rollbackFor = Exception.class)
    public void delete(String loginId);

    @Transactional(rollbackFor = Exception.class)
    public void updateOwn();

    @Transactional(rollbackFor = Exception.class)
    public void updatePassword();

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String loginId, String status);

}
