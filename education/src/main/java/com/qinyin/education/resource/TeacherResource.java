/**
 * @author zhaolie
 * @create-time 2010-12-8
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.resource;

import com.qinyin.athene.Queryable;
import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.QueryableMapping;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.proxy.TeacherProxy;
import com.qinyin.education.validation.TeacherValidation;
import com.qinyin.education.wrap.TeacherWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@QueryableMapping("/teacherQuery")
public class TeacherResource implements Queryable {
    public static Logger log = LoggerFactory.getLogger(TeacherResource.class);
    private TeacherProxy teacherProxy;
    private TeacherValidation teacherValidation;
    private ResourceProxy resourceProxy;
    private SubjectProxy subjectProxy;

    @Override
    public int queryForCount(Map<String, Object> paramMap) {
        return teacherProxy.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap, Integer startIndex, Integer pageSize) {
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        return teacherProxy.queryForList(paramMap);
    }

    @UrlMapping(value = "/teacherList", type = ReturnType.FREEMARKER, url = "education/teacherList.ftl")
    public Map<String, Object> list(@Args Map<String, Object> paramMap) {
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        paramMap.put("teacherStatusList", resourceProxy.getCommonBean("teacherStatus").getAppResourceList());
        return paramMap;
    }

    @UrlMapping(value = "/teacherAdd", type = ReturnType.FREEMARKER, url = "education/teacherEdit.ftl")
    public Map<String, Object> add(@Args Map<String, Object> paramMap) {
        return this.prepareResource(paramMap);
    }

    @UrlMapping(value = "/teacherEdit", type = ReturnType.FREEMARKER, url = "education/teacherEdit.ftl")
    public Map<String, Object> edit(@Args Map<String, Object> paramMap) {
        paramMap = this.prepareResource(paramMap);
        return queryById(paramMap);
    }

    @UrlMapping(value = "/teacherView", type = ReturnType.FREEMARKER, url = "education/teacherView.ftl")
    public Map<String, Object> view(@Args Map<String, Object> paramMap) {
        return queryById(paramMap);
    }

    private Map<String, Object> prepareResource(Map<String, Object> paramMap) {
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        paramMap.put("sexList", resourceProxy.getCommonBean("userSex").getAppResourceList());
        paramMap.put("educationList", resourceProxy.getCommonBean("education").getAppResourceList());
        paramMap.put("experienceList", resourceProxy.getCommonBean("experience").getAppResourceList());
        paramMap.put("teacherLevelList", resourceProxy.getCompanyBean("teacherLevel").getAppResourceList());
        return paramMap;
    }

    private Map<String, Object> queryById(Map<String, Object> paramMap) {
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        if (id == null || id == 0) {
            paramMap.put("error", "ID为空");
        } else {
            TeacherWrap wrap = teacherProxy.queryForWrap(id);
            if (wrap == null) {
                paramMap.put("error", "数据已删除");
            } else {
                paramMap.put("teacher", wrap);
            }
        }
        return paramMap;
    }

    @UrlMapping(value = "/teacherSave", type = ReturnType.JSON)
    public DataInfoBean save() {
        DataInfoBean info = null;
        try {
            info = teacherValidation.simpleValidate();
            if (info.hasError()) return info;
            info = teacherValidation.saveValidate();
            if (info.hasError()) return info;
            teacherProxy.save();
        } catch (Exception e) {
            log.error("save teacher error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/teacherUpdate", type = ReturnType.JSON)
    public DataInfoBean update() {
        DataInfoBean info = null;
        try {
            info = teacherValidation.simpleValidate();
            if (info.hasError()) return info;
            info = teacherValidation.updateValidate();
            if (info.hasError()) return info;
            teacherProxy.update();
        } catch (Exception e) {
            log.error("update teacher error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/teacherDelete", type = ReturnType.JSON)
    public DataInfoBean delete() {
        DataInfoBean info = null;
        try {
            info = teacherValidation.deleteValidate();
            if (info.hasError()) return info;
            teacherProxy.delete();
        } catch (Exception e) {
            log.error("delete teacher error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/teacherQuit", type = ReturnType.JSON)
    public DataInfoBean quit() {
        DataInfoBean info = null;
        try {
            info = teacherValidation.deleteValidate();
            if (info.hasError()) return info;
            teacherProxy.updateStatus(ModelConstants.TEACHER_STATUS_OUT);
        } catch (Exception e) {
            log.error("quit teacher error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/teacherEnter", type = ReturnType.JSON)
    public DataInfoBean enter() {
        DataInfoBean info = null;
        try {
            teacherProxy.updateStatus(ModelConstants.TEACHER_STATUS_IN);
        } catch (Exception e) {
            log.error("enter teacher error", e);
            info.setException(e);
        }
        return info;
    }

    public void setTeacherProxy(TeacherProxy teacherProxy) {
        this.teacherProxy = teacherProxy;
    }

    public void setTeacherValidation(TeacherValidation teacherValidation) {
        this.teacherValidation = teacherValidation;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }
}
