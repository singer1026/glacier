/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-21
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.resource;

import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.education.manager.StaticResourceManager;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.proxy.ClassroomProxy;
import com.qinyin.education.validation.LessonPlanValidation;
import com.qinyin.education.wrap.LessonPlanWrapHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LessonPlanResource {
    public static Logger log = LoggerFactory.getLogger(LessonPlanResource.class);
    private ResourceProxy resourceProxy;
    private ClassroomProxy classroomProxy;
    private StaticResourceManager staticResourceManager;
    private LessonPlanValidation lessonPlanValidation;

    @UrlMapping(value = "/lessonPlanAdd", type = ReturnType.FREEMARKER, url = "education/lessonPlanEdit.ftl")
    public Map<String, Object> add(@Args Map<String, Object> paramMap) throws Exception {
        return this.prepareResource(paramMap);
    }

    @UrlMapping(value = "/lessonPlanEdit", type = ReturnType.FREEMARKER, url = "education/lessonPlanEdit.ftl")
    public Map<String, Object> edit(@Args Map<String, Object> paramMap) throws Exception {
        return this.prepareResource(paramMap);
    }

    private Map<String, Object> prepareResource(Map<String, Object> paramMap) throws Exception {
        paramMap.put("dayOfWeekList", resourceProxy.getCommonBean("dayOfWeek").getAppResourceList());
        paramMap.put("hourList", staticResourceManager.get("hourList"));
        paramMap.put("minuteList", staticResourceManager.get("minuteList"));
        paramMap.put("classroomList", classroomProxy.queryListForCompany());
        paramMap.put("lessonTimeLengthList", resourceProxy.getCompanyBean("lessonTimeLength").getAppResourceList());
        paramMap.put("lessonFrequencyList", resourceProxy.getCompanyBean("lessonFrequency").getAppResourceList());
        return paramMap;
    }

    @UrlMapping(value = "/lessonPlanSave", type = ReturnType.JSON)
    public DataInfoBean save() {
        DataInfoBean info = null;
        try {
            info = lessonPlanValidation.validate();
            if (info.hasError()) return info;
            LessonPlanWrapHelper helper = new LessonPlanWrapHelper();
            helper.addWrap((LessonPlan) info.getData());
            info.setData(helper.getWrappedForObject());
        } catch (Exception e) {
            log.error("save lessonPlan error", e);
            info.setException(e);
        }
        return info;
    }

    public void setLessonPlanValidation(LessonPlanValidation lessonPlanValidation) {
        this.lessonPlanValidation = lessonPlanValidation;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }

    public void setClassroomProxy(ClassroomProxy classroomProxy) {
        this.classroomProxy = classroomProxy;
    }

    public void setStaticResourceManager(StaticResourceManager staticResourceManager) {
        this.staticResourceManager = staticResourceManager;
    }
}
