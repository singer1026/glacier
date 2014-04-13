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
import com.qinyin.education.cache.model.TeacherCacheBean;

import java.util.ArrayList;
import java.util.List;

public class TeacherWrapHelper {
    private List teacherWrapList = new ArrayList();
    private ResourceCacheBean userSexResource;
    private ResourceCacheBean teacherStatusResource;
    private ResourceCacheBean educationResource;
    private ResourceCacheBean experienceResource;
    private ResourceCacheBean teacherLevelResource;
    private Privilege pvg;

    public TeacherWrapHelper() {
        ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean("resourceProxy");
        pvg = (Privilege) BeanFactoryHolder.getBean("pvg");
        userSexResource = resourceProxy.getCommonBean("userSex");
        teacherStatusResource = resourceProxy.getCommonBean("teacherStatus");
        educationResource = resourceProxy.getCommonBean("education");
        experienceResource = resourceProxy.getCommonBean("experience");
        teacherLevelResource = resourceProxy.getCompanyBean("teacherLevel");
    }

    public void addWrap(UserCacheBean userCacheBean, TeacherCacheBean teacherCacheBean) {
        if (userCacheBean == null || teacherCacheBean == null) return;
        teacherWrapList.add(new TeacherWrap(userCacheBean, teacherCacheBean));
    }

    public List getWrappedForList() {
        for (Object obj : teacherWrapList) {
            TeacherWrap wrap = (TeacherWrap) obj;
            wrap.setGmtCreateDisplay(DateUtils.formatDateTime(wrap.getGmtCreate()));
            wrap.setGmtModifyDisplay(DateUtils.formatDateTime(wrap.getGmtModify()));
            wrap.setCreatorDisplay(pvg.getUserNameByLoginId(wrap.getCreator()));
            wrap.setModifierDisplay(pvg.getUserNameByLoginId(wrap.getModifier()));
            wrap.setSexDisplay(userSexResource.getName(wrap.getSex()));
            wrap.setStatusDisplay(teacherStatusResource.getName(wrap.getStatus()));
            wrap.setEducationDisplay(educationResource.getName(wrap.getEducation()));
            wrap.setExperienceDisplay(experienceResource.getName(wrap.getExperience()));
            wrap.setLevelDisplay(teacherLevelResource.getName(wrap.getLevel()));
        }
        return teacherWrapList;
    }

    public TeacherWrap getWrappedForObject() {
        List wrappedList = this.getWrappedForList();
        return wrappedList.size() > 0 ? (TeacherWrap) wrappedList.get(0) : null;
    }
}
