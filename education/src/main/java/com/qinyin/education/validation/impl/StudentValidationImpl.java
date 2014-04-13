/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-24
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.validation.impl;

import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.util.*;
import com.qinyin.athene.validation.UserValidation;
import com.qinyin.education.cache.model.StudentCacheBean;
import com.qinyin.education.cache.model.SubjectCacheBean;
import com.qinyin.education.model.Student;
import com.qinyin.education.proxy.StudentProxy;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.validation.StudentValidation;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentValidationImpl implements StudentValidation {
    private JdbcDao jdbcDao;
    private StudentProxy studentProxy;
    private SubjectProxy subjectProxy;
    private UserProxy userProxy;
    private UserValidation userValidation;

    @Override
    public DataInfoBean simpleValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        DataInfoBean info = userValidation.fullValidate();
        String gmtEnter = ParamUtils.getString(paramMap.get(Student.GMT_ENTER));
        if (gmtEnter == null) {
            info.addValidationError("gmt-enter-" + number + "-error", "必须输入入校时间");
        } else {
            Date enterDate = DateUtils.parseDate(gmtEnter);
            if (enterDate == null) {
                info.addValidationError("gmt-enter-" + number + "-error", "日期格式不正确");
            } else {
                paramMap.put("gmtEnter", enterDate);
            }
        }
        String school = ParamUtils.getString(paramMap.get(Student.SCHOOL));
        if (school != null) {
            if (!ValidationUtils.isChinese(school)) {
                info.addValidationError("school-" + number + "-error", "学校名称必须为中文");
            } else if (!ValidationUtils.isAllowedChinese(school, 16)) {
                info.addValidationError("school-" + number + "-error", "学校名称最大为16个汉字");
            }
        }
        String description = ParamUtils.getString(paramMap.get(Student.DESCRIPTION));
        if (description != null) {
            if (DefenseUtils.isUnlawfulChar(description)) {
                info.addValidationError("description-" + number + "-error", "整体描述" + DefenseUtils.UNLAWFUL_CHAR);
            }
        }
        List<Object> subjectList = ParamUtils.getList(paramMap.get("subjectList"));
        if (subjectList == null) {
            info.addValidationError("subject-list-" + number + "-error", "必须选择科目");
        } else {
            List list = (List) CollectionUtils.collect(subjectList, new BeanToPropertyValueTransformer("id"));
            paramMap.put("subjectList", list);
        }
        return info;
    }

    @Override
    public DataInfoBean saveValidate() {
        return userValidation.saveValidate();
    }

    @Override
    public DataInfoBean updateValidate() throws Exception {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        DataInfoBean info = AtheneUtils.getInfo();
        String number = ParamUtils.getString(requestMap.get("number"));
        StudentCacheBean studentBean = studentProxy.queryById(ParamUtils.getIdInteger(paramMap.get(BaseModel.ID)));
        List subjectList = studentBean.getSubjectList();
        List oldSubjectList = (List) CollectionUtils.collect(subjectList, new BeanToPropertyValueTransformer("id"));
        List newSubjectList = ParamUtils.getList(paramMap.get("subjectList"));
        List deleteSubjectList = (List) CollectionUtils.subtract(oldSubjectList, newSubjectList);
        List addSubjectList = (List) CollectionUtils.subtract(newSubjectList, oldSubjectList);
        if (deleteSubjectList != null) {
            for (int i = 0; i < deleteSubjectList.size(); i++) {
                Integer subjectId = (Integer) deleteSubjectList.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("subjectId", subjectId);
                map.put("studentId", studentBean.getId());
                if (jdbcDao.queryForCount("EduLesson", "QUERY_COUNT", map) > 0) {
                    SubjectCacheBean subjectBean = subjectProxy.queryById(subjectId);
                    String subjectName = subjectBean.getName();
                    String message = "存在" + subjectName + "科目课程,更新失败";
                    if (i != deleteSubjectList.size() - 1) {
                        message += "<br/>";
                    }
                    info.addValidationError("subject-list-" + number + "-error", message);
                }
            }
        }
        return info;
    }

    @Override
    public DataInfoBean deleteValidate() throws Exception {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        DataInfoBean info = AtheneUtils.getInfo();
        String number = ParamUtils.getString(requestMap.get("number"));
        StudentCacheBean studentBean = studentProxy.queryById(ParamUtils.getIdInteger(paramMap.get(BaseModel.ID)));
        UserCacheBean userBean = userProxy.queryByLoginId(studentBean.getLoginId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("studentId", studentBean.getId());
        if (jdbcDao.queryForCount("EduLesson", "QUERY_COUNT", map) > 0) {
            info.addValidationError("description-" + number + "-error", "请先删除" + userBean.getName() + "的课程");
        }
        return info;
    }

    @Override
    public DataInfoBean quitValidate() throws Exception {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        DataInfoBean info = AtheneUtils.getInfo();
        String number = ParamUtils.getString(requestMap.get("number"));
        String gmtQuit = ParamUtils.getString(paramMap.get(Student.GMT_QUIT));
        if (gmtQuit == null) {
            info.addValidationError("gmt-quit-" + number + "-error", "必须输入离校时间");
        } else {
            Date quitDate = DateUtils.parseDate(gmtQuit);
            if (quitDate == null) {
                info.addValidationError("gmt-quit-" + number + "-error", "日期格式不正确");
            } else {
                paramMap.put(Student.GMT_QUIT, quitDate);
            }
        }
        if (info.hasError()) return info;
        StudentCacheBean studentBean = studentProxy.queryById(ParamUtils.getIdInteger(paramMap.get(BaseModel.ID)));
        UserCacheBean userBean = userProxy.queryByLoginId(studentBean.getLoginId());
        Date quitDate = ParamUtils.getDate(paramMap.get(Student.GMT_QUIT));
        if (DateUtils.compare(quitDate, studentBean.getGmtEnter()) < 1) {
            info.addValidationError("gmt-quit-" + number + "-error", "离校时间应该大于入校时间");
        }
        if (info.hasError()) return info;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("studentId", studentBean.getId());
        if (jdbcDao.queryForCount("EduLesson", "QUERY_COUNT", map) > 0) {
            info.addValidationError("description-" + number + "-error", "请先删除" + userBean.getName() + "的课程");
        }
        return info;
    }

    @Override
    public DataInfoBean enterValidate() throws Exception {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        DataInfoBean info = AtheneUtils.getInfo();
        String number = ParamUtils.getString(requestMap.get("number"));
        String gmtEnter = ParamUtils.getString(paramMap.get(Student.GMT_ENTER));
        if (gmtEnter == null) {
            info.addValidationError("gmt-enter-" + number + "-error", "必须输入入校时间");
        } else {
            Date enterDate = DateUtils.parseDate(gmtEnter);
            if (enterDate == null) {
                info.addValidationError("gmt-enter-" + number + "-error", "日期格式不正确");
            } else {
                paramMap.put(Student.GMT_ENTER, enterDate);
            }
        }
        if (info.hasError()) return info;
        StudentCacheBean studentBean = studentProxy.queryById(ParamUtils.getIdInteger(paramMap.get(BaseModel.ID)));
        Date enterDate = ParamUtils.getDate(paramMap.get(Student.GMT_ENTER));
        if (DateUtils.compare(enterDate, studentBean.getGmtEnter()) < 1) {
            info.addValidationError("gmt-enter-" + number + "-error", "入校时间应该大于原来值");
        }
        return info;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setStudentProxy(StudentProxy studentProxy) {
        this.studentProxy = studentProxy;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }

    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }
}
