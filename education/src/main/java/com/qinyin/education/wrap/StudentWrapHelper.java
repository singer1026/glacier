/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.wrap;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.education.cache.model.StudentCacheBean;

import java.util.ArrayList;
import java.util.List;

public class StudentWrapHelper {
    private List studentWrapList = new ArrayList();
    private ResourceCacheBean userSexResource;
    private ResourceCacheBean studentStatusResource;
    private Privilege pvg;

    public StudentWrapHelper() {
        ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean("resourceProxy");
        pvg = (Privilege) BeanFactoryHolder.getBean("pvg");
        userSexResource = resourceProxy.getCommonBean("userSex");
        studentStatusResource = resourceProxy.getCommonBean("studentStatus");
    }

    public void addWrap(UserCacheBean userCacheBean, StudentCacheBean studentCacheBean) {
        if (userCacheBean == null || studentCacheBean == null) return;
        studentWrapList.add(new StudentWrap(userCacheBean, studentCacheBean));
    }

    public List getWrappedForList() {
        for (Object obj : studentWrapList) {
            StudentWrap wrap = (StudentWrap) obj;
            wrap.setGmtCreateDisplay(DateUtils.formatDateTime(wrap.getGmtCreate()));
            wrap.setGmtModifyDisplay(DateUtils.formatDateTime(wrap.getGmtModify()));
            wrap.setCreatorDisplay(pvg.getUserNameByLoginId(wrap.getCreator()));
            wrap.setModifierDisplay(pvg.getUserNameByLoginId(wrap.getModifier()));
            wrap.setGmtEnterDisplay(DateUtils.formatDate(wrap.getGmtEnter()));
            wrap.setSexDisplay(userSexResource.getName(wrap.getSex()));
            wrap.setStatusDisplay(studentStatusResource.getName(wrap.getStatus()));
        }
        return studentWrapList;
    }

    public StudentWrap getWrappedForObject() {
        List wrappedList = this.getWrappedForList();
        return wrappedList.size() > 0 ? (StudentWrap) wrappedList.get(0) : null;
    }
}
