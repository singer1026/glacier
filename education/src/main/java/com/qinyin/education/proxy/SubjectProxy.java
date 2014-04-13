/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.proxy;

import com.qinyin.education.cache.model.SubjectCacheBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubjectProxy {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public SubjectCacheBean queryById(Integer id);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<SubjectCacheBean> queryListForCompany();
}
