/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.validation.impl;

import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DefenseUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.athene.util.ValidationUtils;
import com.qinyin.athene.validation.UserValidation;
import com.qinyin.education.cache.model.SubjectCacheBean;
import com.qinyin.education.cache.model.TeacherCacheBean;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.proxy.TeacherProxy;
import com.qinyin.education.validation.TeacherValidation;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherValidationImpl implements TeacherValidation {
    private UserValidation userValidation;
    private TeacherProxy teacherProxy;
    private SubjectProxy subjectProxy;
    private UserProxy userProxy;
    private JdbcDao jdbcDao;

    @Override
    public DataInfoBean simpleValidate() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        DataInfoBean info = userValidation.fullValidate();
        String graduateSchool = ParamUtils.getString(paramMap.get("graduateSchool"));
        if (graduateSchool == null) {
            info.addValidationError("graduate-school-" + number + "-error", "必须输入毕业院校");
        } else if (!ValidationUtils.isChinese(graduateSchool)) {
            info.addValidationError("graduate-school-" + number + "-error", "学校名称必须为中文");
        } else if (!ValidationUtils.isAllowedChinese(graduateSchool, 16)) {
            info.addValidationError("graduate-school-" + number + "-error", "学校名称最大为16个汉字");
        }
        String level = ParamUtils.getString(paramMap.get("level"));
        if (level == null) {
            info.addValidationError("level-" + number + "-error", "必须选择教师等级");
        }
        String major = ParamUtils.getString(paramMap.get("major"));
        if (major == null) {
            info.addValidationError("major-" + number + "-error", "必须输入专业");
        } else if (!ValidationUtils.isChinese(major)) {
            info.addValidationError("major-" + number + "-error", "专业名称必须为中文");
        } else if (!ValidationUtils.isAllowedChinese(major, 16)) {
            info.addValidationError("major-" + number + "-error", "专业名称最大为16个汉字");
        }
        String experience = ParamUtils.getString(paramMap.get("experience"));
        if (experience == null) {
            info.addValidationError("experience-" + number + "-error", "必须选择工作经验");
        }
        String education = ParamUtils.getString(paramMap.get("education"));
        if (education == null) {
            info.addValidationError("education-" + number + "-error", "必须选择学历");
        }
        String description = ParamUtils.getString(paramMap.get("description"));
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
        TeacherCacheBean teacherBean = teacherProxy.queryById(ParamUtils.getIdInteger(paramMap.get(BaseModel.ID)));
        List subjectList = teacherBean.getSubjectList();
        List oldSubjectList = (List) CollectionUtils.collect(subjectList, new BeanToPropertyValueTransformer("id"));
        List newSubjectList = ParamUtils.getList(paramMap.get("subjectList"));
        List deleteSubjectList = (List) CollectionUtils.subtract(oldSubjectList, newSubjectList);
        List addSubjectList = (List) CollectionUtils.subtract(newSubjectList, oldSubjectList);
        if (deleteSubjectList != null) {
            for (int i = 0; i < deleteSubjectList.size(); i++) {
                Integer subjectId = (Integer) deleteSubjectList.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("subjectId", subjectId);
                map.put("teacherId", teacherBean.getId());
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
        TeacherCacheBean teacherBean = teacherProxy.queryById(ParamUtils.getIdInteger(paramMap.get(BaseModel.ID)));
        UserCacheBean userBean = userProxy.queryByLoginId(teacherBean.getLoginId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("teacherId", teacherBean.getId());
        if (jdbcDao.queryForCount("EduLesson", "QUERY_COUNT", map) > 0) {
            info.addValidationError("description-" + number + "-error", "请先删除" + userBean.getName() + "的课程");
        }
        return info;
    }

    public void setTeacherProxy(TeacherProxy teacherProxy) {
        this.teacherProxy = teacherProxy;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setUserValidation(UserValidation userValidation) {
        this.userValidation = userValidation;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }
}
