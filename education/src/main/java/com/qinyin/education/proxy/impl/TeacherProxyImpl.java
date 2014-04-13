/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-19
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.proxy.impl;

import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppUser;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.cache.model.TeacherCacheBean;
import com.qinyin.education.cache.store.LessonStore;
import com.qinyin.education.cache.store.TeacherStore;
import com.qinyin.education.model.LessonTeacher;
import com.qinyin.education.model.Subject;
import com.qinyin.education.model.SubjectRelation;
import com.qinyin.education.model.Teacher;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.proxy.TeacherProxy;
import com.qinyin.education.service.LessonTeacherService;
import com.qinyin.education.service.SubjectRelationService;
import com.qinyin.education.service.TeacherService;
import com.qinyin.education.wrap.TeacherWrap;
import com.qinyin.education.wrap.TeacherWrapHelper;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class TeacherProxyImpl implements TeacherProxy {
    public static Logger log = LoggerFactory.getLogger(TeacherProxyImpl.class);
    private TeacherStore teacherStore;
    private TeacherService teacherService;
    private SubjectProxy subjectProxy;
    private LessonTeacherService lessonTeacherService;
    private SubjectRelationService subjectRelationService;
    private UserProxy userProxy;
    private PrivilegeInfo pvgInfo;
    private LessonStore lessonStore;
    private ResourceProxy resourceProxy;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return teacherService.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        TeacherWrapHelper helper = new TeacherWrapHelper();
        List teacherList = teacherService.queryForList(paramMap);
        List idList = (List) CollectionUtils.collect(teacherList, new BeanToPropertyValueTransformer(BaseModel.ID));
        for (Object obj : idList) {
            TeacherCacheBean teacherBean = this.queryById(ParamUtils.getIdInteger(obj));
            helper.addWrap(userProxy.queryByLoginId(teacherBean.getLoginId()), teacherBean);
        }
        return helper.getWrappedForList();
    }

    private Teacher getNewTeacher(String loginId) {
        String creator = pvgInfo.getLoginId();
        Integer companyId = pvgInfo.getCompanyId();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Teacher teacher = new Teacher();
        teacher.setGmtCreate(now);
        teacher.setCreator(creator);
        teacher.setGmtModify(now);
        teacher.setModifier(creator);
        teacher.setIsDeleted(ModelConstants.IS_DELETED_N);
        teacher.setVersion(ModelConstants.VERSION_INIT);
        teacher.setStatus(ModelConstants.TEACHER_STATUS_IN);
        teacher.setLoginId(loginId);
        teacher.setCompanyId(companyId);
        teacher.setEducation(ParamUtils.getString(paramMap.get(Teacher.EDUCATION)));
        teacher.setGraduateSchool(ParamUtils.getString(paramMap.get(Teacher.GRADUATE_SCHOOL)));
        teacher.setLevel(ParamUtils.getString(paramMap.get(Teacher.LEVEL)));
        teacher.setMajor(ParamUtils.getString(paramMap.get(Teacher.MAJOR)));
        teacher.setExperience(ParamUtils.getString(paramMap.get(Teacher.EXPERIENCE)));
        teacher.setDescription(ParamUtils.getString(paramMap.get(Teacher.DESCRIPTION)));
        return teacher;
    }

    private Teacher getUpdateTeacher(Integer id) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Teacher dbTeacher = this.queryById(id);
        Teacher orphanTeacher = null;
        try {
            orphanTeacher = (Teacher) BeanUtils.cloneBean(dbTeacher);
            orphanTeacher.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanTeacher.setModifier(pvgInfo.getLoginId());
            orphanTeacher.setEducation(ParamUtils.getString(paramMap.get(Teacher.EDUCATION)));
            orphanTeacher.setGraduateSchool(ParamUtils.getString(paramMap.get(Teacher.GRADUATE_SCHOOL)));
            orphanTeacher.setLevel(ParamUtils.getString(paramMap.get(Teacher.LEVEL)));
            orphanTeacher.setMajor(ParamUtils.getString(paramMap.get(Teacher.MAJOR)));
            orphanTeacher.setExperience(ParamUtils.getString(paramMap.get(Teacher.EXPERIENCE)));
            orphanTeacher.setDescription(ParamUtils.getString(paramMap.get(Teacher.DESCRIPTION)));
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        if (!dbTeacher.getEducation().equals(orphanTeacher.getEducation())) {
            return orphanTeacher;
        } else if (!StringUtils.equals(dbTeacher.getGraduateSchool(), orphanTeacher.getGraduateSchool())) {
            return orphanTeacher;
        } else if (!StringUtils.equals(dbTeacher.getLevel(), orphanTeacher.getLevel())) {
            return orphanTeacher;
        } else if (!StringUtils.equals(dbTeacher.getMajor(), orphanTeacher.getMajor())) {
            return orphanTeacher;
        } else if (!StringUtils.equals(dbTeacher.getExperience(), orphanTeacher.getExperience())) {
            return orphanTeacher;
        } else if (!StringUtils.equals(dbTeacher.getDescription(), orphanTeacher.getDescription())) {
            return orphanTeacher;
        } else {
            return null;
        }
    }

    private Teacher getDeleteTeacher(Integer id) {
        Teacher dbTeacher = this.queryById(id);
        dbTeacher.setGmtModify(new Timestamp(System.currentTimeMillis()));
        dbTeacher.setModifier(pvgInfo.getLoginId());
        return dbTeacher;
    }

    @Override
    public TeacherWrap queryForWrap(Integer id) {
        TeacherWrapHelper helper = new TeacherWrapHelper();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        TeacherCacheBean teacherBean = this.queryById(id);
        helper.addWrap(userProxy.queryByLoginId(teacherBean.getLoginId()), teacherBean);
        return helper.getWrappedForObject();
    }

    @Override
    public TeacherCacheBean queryById(Integer id) {
        if (id == null || id == 0) return null;
        TeacherCacheBean bean = teacherStore.get(id);
        if (bean == null) {
            Teacher dbTeacher = teacherService.queryById(id);
            if (dbTeacher == null) {
                log.error("can't find teacher with id[" + id + "]");
                return null;
            }
            bean = new TeacherCacheBean(dbTeacher);
            List<SubjectRelation> srList = subjectRelationService.queryByTeacherId(id);
            for (SubjectRelation sr : srList) {
                bean.addSubject(subjectProxy.queryById(sr.getSubjectId()));
            }
            teacherStore.put(id, bean);
        }
        return bean;
    }

    @Override
    public void save() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        userProxy.save(ModelConstants.USER_TYPE_TEACHER);
        Teacher teacher = getNewTeacher(ParamUtils.getString(paramMap.get(AppUser.LOGIN_ID)));
        int teacherId = teacherService.save(teacher);
        List<Object> list = ParamUtils.getList(paramMap.get("subjectList"));
        for (Object obj : list) {
            SubjectRelation sr = new SubjectRelation(teacherId, ModelConstants.SUBJECT_RELATION_TYPE_TEACHER);
            sr.setSubjectId(ParamUtils.getIdInteger(obj));
            subjectRelationService.save(sr);
        }
    }

    @Override
    public void update() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        //------------------------------------------------------------------//
        List<LessonTeacher> lessonTeacherList = lessonTeacherService.queryListByTeacherId(id);
        if (lessonTeacherList != null && lessonTeacherList.size() > 0) {
            String name = ParamUtils.getString(paramMap.get(AppUser.NAME));
            LessonTeacher lt = lessonTeacherList.get(0);
            if (!StringUtils.equals(name, lt.getTeacherName())) {
                lessonTeacherService.updateTeacherName(id, name);
                for (LessonTeacher lessonTeacher : lessonTeacherList) {
                    lessonStore.remove(lessonTeacher.getLessonId());
                }
            }
        }
        //------------------------------------------------------------------//
        String loginId = ParamUtils.getString(paramMap.get(AppUser.LOGIN_ID));
        userProxy.update(loginId);
        Teacher teacher = this.getUpdateTeacher(id);
        boolean removeable = false;
        if (teacher != null) {
            teacherService.update(teacher);
            removeable = true;
        }
        Boolean listChanged = ParamUtils.getBooleanValue(paramMap.get("subjectListChanged"));
        if (listChanged != null && listChanged == true) {
            TeacherCacheBean teacherBean = this.queryById(ParamUtils.getIdInteger(paramMap.get(BaseModel.ID)));
            List<Subject> subjectList = teacherBean.getSubjectList();
            List oldSubjectList = (List) CollectionUtils.collect(subjectList, new BeanToPropertyValueTransformer(BaseModel.ID));
            List newSubjectList = ParamUtils.getList(paramMap.get("subjectList"));
            List deleteSubjectList = (List) CollectionUtils.subtract(oldSubjectList, newSubjectList);
            List addSubjectList = (List) CollectionUtils.subtract(newSubjectList, oldSubjectList);
            if (deleteSubjectList != null && deleteSubjectList.size() > 0) {
                for (Object obj : deleteSubjectList) {
                    SubjectRelation sr = new SubjectRelation(teacherBean.getId(), ModelConstants.SUBJECT_RELATION_TYPE_TEACHER);
                    sr.setSubjectId(ParamUtils.getIdInteger(obj));
                    subjectRelationService.delete(sr);
                }
                removeable = true;
            }
            if (addSubjectList != null && addSubjectList.size() > 0) {
                for (Object obj : addSubjectList) {
                    SubjectRelation sr = new SubjectRelation(teacherBean.getId(), ModelConstants.SUBJECT_RELATION_TYPE_TEACHER);
                    sr.setSubjectId(ParamUtils.getIdInteger(obj));
                    subjectRelationService.save(sr);
                }
                removeable = true;
            }
        }
        if (removeable) {
            teacherStore.remove(id);
        }
    }

    @Override
    public void delete() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Teacher teacherBean = this.getDeleteTeacher(id);
        SubjectRelation sr = new SubjectRelation(id, ModelConstants.SUBJECT_RELATION_TYPE_TEACHER);
        subjectRelationService.delete(sr);
        teacherService.delete(teacherBean);
        teacherStore.remove(id);
        userProxy.delete(teacherBean.getLoginId());
    }

    @Override
    public void updateStatus(String status) {
        if (StringUtils.isBlank(status) ||
                (!status.equals(ModelConstants.TEACHER_STATUS_IN) && !status.equals(ModelConstants.TEACHER_STATUS_OUT)))
            return;
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Teacher teacherBean = this.getDeleteTeacher(id);
        teacherBean.setStatus(status);
        teacherService.updateStatus(teacherBean);
        teacherStore.remove(id);
        if (status.equals(ModelConstants.TEACHER_STATUS_OUT)) {
            userProxy.updateStatus(teacherBean.getLoginId(), ModelConstants.USER_STATUS_LOCK);
        }
    }

    public void setTeacherStore(TeacherStore teacherStore) {
        this.teacherStore = teacherStore;
    }

    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public void setSubjectRelationService(SubjectRelationService subjectRelationService) {
        this.subjectRelationService = subjectRelationService;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }

    public void setLessonTeacherService(LessonTeacherService lessonTeacherService) {
        this.lessonTeacherService = lessonTeacherService;
    }

    public void setLessonStore(LessonStore lessonStore) {
        this.lessonStore = lessonStore;
    }
}
