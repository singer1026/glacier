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
import com.qinyin.education.proxy.StudentProxy;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.validation.StudentValidation;
import com.qinyin.education.wrap.StudentWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@QueryableMapping("/studentQuery")
public class StudentResource implements Queryable {
    public static Logger log = LoggerFactory.getLogger(StudentResource.class);
    private StudentValidation studentValidation;
    private StudentProxy studentProxy;
    private ResourceProxy resourceProxy;
    private SubjectProxy subjectProxy;

    @Override
    public int queryForCount(Map<String, Object> paramMap) {
        return studentProxy.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap, Integer startIndex, Integer pageSize) {
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        return studentProxy.queryForList(paramMap);
    }

    @UrlMapping(value = "/studentList", type = ReturnType.FREEMARKER, url = "education/studentList.ftl")
    public Map<String, Object> list(@Args Map<String, Object> paramMap) {
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        paramMap.put("studentStatusList", resourceProxy.getCommonBean("studentStatus").getAppResourceList());
        paramMap.put("hasLessonList", resourceProxy.getCommonBean("hasLesson").getAppResourceList());
        return paramMap;
    }

    @UrlMapping(value = "/studentAdd", type = ReturnType.FREEMARKER, url = "education/studentEdit.ftl")
    public Map<String, Object> add(@Args Map<String, Object> paramMap) {
        paramMap = this.prepareResource(paramMap);
        return paramMap;
    }

    @UrlMapping(value = "/studentEdit", type = ReturnType.FREEMARKER, url = "education/studentEdit.ftl")
    public Map<String, Object> edit(@Args Map<String, Object> paramMap) {
        paramMap = this.prepareResource(paramMap);
        return this.queryById(paramMap);
    }

    @UrlMapping(value = "/studentView", type = ReturnType.FREEMARKER, url = "education/studentView.ftl")
    public Map<String, Object> view(@Args Map<String, Object> paramMap) {
        return queryById(paramMap);
    }

    private Map<String, Object> queryById(Map<String, Object> paramMap) {
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        if (id == null || id == 0) {
            paramMap.put("error", "ID为空");
        } else {
            StudentWrap wrap = studentProxy.queryForWrap(id);
            if (wrap == null) {
                paramMap.put("error", "数据已删除");
            } else {
                paramMap.put("student", wrap);
            }
        }
        return paramMap;
    }

    private Map<String, Object> prepareResource(Map<String, Object> paramMap) {
        paramMap.put("subjectList", subjectProxy.queryListForCompany());
        paramMap.put("sexList", resourceProxy.getCommonBean("userSex").getAppResourceList());
        return paramMap;
    }

    @UrlMapping(value = "/studentSave", type = ReturnType.JSON)
    public DataInfoBean save() {
        DataInfoBean info = null;
        try {
            info = studentValidation.simpleValidate();
            if (info.hasError()) return info;
            info = studentValidation.saveValidate();
            if (info.hasError()) return info;
            studentProxy.save();
        } catch (Exception e) {
            log.error("save student error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/studentUpdate", type = ReturnType.JSON)
    public DataInfoBean update() {
        DataInfoBean info = null;
        try {
            info = studentValidation.simpleValidate();
            if (info.hasError()) return info;
            info = studentValidation.updateValidate();
            if (info.hasError()) return info;
            studentProxy.update();
        } catch (Exception e) {
            log.error("update student error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/studentDelete", type = ReturnType.JSON)
    public DataInfoBean delete() {
        DataInfoBean info = null;
        try {
            info = studentValidation.deleteValidate();
            if (info.hasError()) return info;
            studentProxy.delete();
        } catch (Exception e) {
            log.error("delete student error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/studentQuit", type = ReturnType.JSON)
    public DataInfoBean quit() {
        DataInfoBean info = null;
        try {
            info = studentValidation.quitValidate();
            if (info.hasError()) return info;
            studentProxy.updateStatus(ModelConstants.STUDENT_STATUS_OUT);
        } catch (Exception e) {
            log.error("delete student error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/studentEnter", type = ReturnType.JSON)
    public DataInfoBean enter() {
        DataInfoBean info = null;
        try {
            info = studentValidation.enterValidate();
            if (info.hasError()) return info;
            studentProxy.updateStatus(ModelConstants.STUDENT_STATUS_IN);
        } catch (Exception e) {
            log.error("delete student error", e);
            info.setException(e);
        }
        return info;
    }

    public void setStudentValidation(StudentValidation studentValidation) {
        this.studentValidation = studentValidation;
    }

    public void setStudentProxy(StudentProxy studentProxy) {
        this.studentProxy = studentProxy;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }
}
