/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.proxy.impl;

import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.cache.model.SubjectCacheBean;
import com.qinyin.education.cache.store.SubjectStore;
import com.qinyin.education.model.Subject;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.service.SubjectService;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SubjectProxyImpl implements SubjectProxy {
    public static Logger log = LoggerFactory.getLogger(SubjectProxyImpl.class);
    private SubjectStore subjectStore;
    private SubjectService subjectService;
    private PrivilegeInfo pvgInfo;

    @Override
    public SubjectCacheBean queryById(Integer id) {
        if (id == null || id == 0) return null;
        SubjectCacheBean bean = subjectStore.get(id);
        if (bean == null) {
            Subject dbSubject = subjectService.queryById(id);
            if (dbSubject == null) {
                log.error("can't find subject with id[" + id + "]");
                return null;
            }
            bean = new SubjectCacheBean(dbSubject);
            subjectStore.put(id, bean);
        }
        return bean;
    }

    @Override
    public List<SubjectCacheBean> queryListForCompany() {
        Integer companyId = pvgInfo.getCompanyId();
        List<SubjectCacheBean> rtnList = new ArrayList<SubjectCacheBean>(0);
        List list = subjectStore.getListByCompanyId(companyId);
        if (list == null || list.size() == 0) {
            List<Subject> subjectList = subjectService.queryListByCompanyId(companyId);
            if (subjectList == null || subjectList.size() == 0) {
                log.error("can't find any subject with companyId[" + companyId + "]");
                return rtnList;
            }
            list = (List) CollectionUtils.collect(subjectList, new BeanToPropertyValueTransformer(BaseModel.ID));
            subjectStore.putList(companyId, list);
        }
        for (Object obj : list) {
            Integer id = ParamUtils.getIdInteger(obj);
            SubjectCacheBean subjectCacheBean = this.queryById(id);
            if (subjectCacheBean != null) {
                rtnList.add(subjectCacheBean);
            }
        }
        return rtnList;
    }

    public void setSubjectStore(SubjectStore subjectStore) {
        this.subjectStore = subjectStore;
    }

    public void setSubjectService(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
