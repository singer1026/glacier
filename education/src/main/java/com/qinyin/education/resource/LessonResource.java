/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-2-21
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.resource;

import com.qinyin.athene.Queryable;
import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.QueryableMapping;
import com.qinyin.athene.annotation.RequestMap;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.proxy.LessonProxy;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.validation.LessonValidation;
import com.qinyin.education.wrap.LessonWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@QueryableMapping("/lessonQuery")
public class LessonResource implements Queryable {
    public static Logger log = LoggerFactory.getLogger(LessonResource.class);
    private ResourceProxy resourceProxy;
    private LessonValidation lessonValidation;
    private LessonProxy lessonProxy;
    private SubjectProxy subjectProxy;

    @Override
    public int queryForCount(Map<String, Object> paramMap) {
        return lessonProxy.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap, Integer startIndex, Integer pageSize) {
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        return lessonProxy.queryForList(paramMap);
    }

    @UrlMapping(value = "/lessonList", type = ReturnType.FREEMARKER, url = "education/lessonList.ftl")
    public Map<String, Object> list(@Args Map<String, Object> paramMap) throws Exception {
        paramMap.put("dayOfWeekList", resourceProxy.getCommonBean("dayOfWeek").getAppResourceList());
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        return paramMap;
    }

    @UrlMapping(value = "/lessonDayStatistics", type = ReturnType.FREEMARKER, url = "education/lessonDayStatistics.ftl")
    public Map<String, Object> dayStatistics(@Args Map<String, Object> paramMap) {
        List<Object> list = lessonProxy.queryLessonStatistics();
        paramMap.put("dayStatistics", list);
        paramMap.put("dayTotalStatistics", lessonProxy.queryLessonDayStatistics(list));
        paramMap.put("totalStatistics", lessonProxy.queryLessonTotalStatistics(list));
        return paramMap;
    }

    @UrlMapping(value = "/lessonTeacher", type = ReturnType.FREEMARKER, url = "education/lessonTeacher.ftl")
    public Map<String, Object> teacherList(@Args Map<String, Object> paramMap) throws Exception {
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        return paramMap;
    }

    @UrlMapping(value = "/lessonStudent", type = ReturnType.FREEMARKER, url = "education/lessonStudent.ftl")
    public Map<String, Object> studentList(@Args Map<String, Object> paramMap) throws Exception {
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        return paramMap;
    }

    @UrlMapping(value = "/lessonAdd", type = ReturnType.FREEMARKER, url = "education/lessonEdit.ftl")
    public Map<String, Object> add(@Args Map<String, Object> paramMap) throws Exception {
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        return paramMap;
    }

    @UrlMapping(value = "/lessonEdit", type = ReturnType.FREEMARKER, url = "education/lessonEdit.ftl")
    public Map<String, Object> edit(@Args Map<String, Object> paramMap) throws Exception {
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        if (id == null || id == 0) {
            paramMap.put("error", "ID为空");
        } else {
            LessonWrap wrap = lessonProxy.queryForWrap(id);
            if (wrap == null) {
                paramMap.put("error", "数据已删除");
            } else {
                paramMap.put("lesson", wrap);
            }
        }
        return paramMap;
    }

    @UrlMapping(value = "/lessonSave", type = ReturnType.JSON)
    public DataInfoBean save(@RequestMap Map<String, Object> requestMap) {
        DataInfoBean info = null;
        try {
            info = lessonValidation.simpleValidate();
            if (info.hasError()) return info;
            info = lessonValidation.deepValidate();
            if (info.hasError()) return info;
            lessonProxy.save();
        } catch (Exception e) {
            log.error("save lessonPlan error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/lessonUpdate", type = ReturnType.JSON)
    public DataInfoBean update() {
        DataInfoBean info = null;
        try {
            info = lessonValidation.simpleValidate();
            if (info.hasError()) return info;
            info = lessonValidation.deepValidate();
            if (info.hasError()) return info;
            lessonProxy.update();
        } catch (Exception e) {
            log.error("update lessonPlan error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/lessonDelete", type = ReturnType.JSON)
    public DataInfoBean delete() {
        DataInfoBean info = AtheneUtils.getInfo();
        try {
            lessonProxy.delete();
        } catch (Exception e) {
            log.error("delete lessonPlan error", e);
            info.setException(e);
        }
        return info;
    }

    public void setLessonProxy(LessonProxy lessonProxy) {
        this.lessonProxy = lessonProxy;
    }

    public void setLessonValidation(LessonValidation lessonValidation) {
        this.lessonValidation = lessonValidation;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }
}
