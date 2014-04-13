/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-23
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.proxy.impl;

import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppUser;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.cache.model.StudentCacheBean;
import com.qinyin.education.cache.store.LessonStore;
import com.qinyin.education.cache.store.StudentStore;
import com.qinyin.education.model.LessonStudent;
import com.qinyin.education.model.Student;
import com.qinyin.education.model.SubjectRelation;
import com.qinyin.education.proxy.StudentProxy;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.service.LessonStudentService;
import com.qinyin.education.service.StudentService;
import com.qinyin.education.service.SubjectRelationService;
import com.qinyin.education.wrap.StudentWrap;
import com.qinyin.education.wrap.StudentWrapHelper;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class StudentProxyImpl implements StudentProxy {
    public static Logger log = LoggerFactory.getLogger(StudentProxyImpl.class);
    private JdbcDao jdbcDao;
    private StudentStore studentStore;
    private StudentService studentService;
    private SubjectProxy subjectProxy;
    private LessonStudentService lessonStudentService;
    private SubjectRelationService subjectRelationService;
    private LessonStore lessonStore;
    private UserProxy userProxy;
    private PrivilegeInfo pvgInfo;
    private ResourceProxy resourceProxy;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return studentService.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        StudentWrapHelper helper = new StudentWrapHelper();
        List studentList = studentService.queryForList(paramMap);
        List idList = (List) CollectionUtils.collect(studentList, new BeanToPropertyValueTransformer(BaseModel.ID));
        for (Object obj : idList) {
            StudentCacheBean studentBean = this.queryById(ParamUtils.getIdInteger(obj));
            helper.addWrap(userProxy.queryByLoginId(studentBean.getLoginId()), studentBean);
        }
        return helper.getWrappedForList();
    }

    private Student getNewStudent(String loginId) {
        String creator = pvgInfo.getLoginId();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer companyId = pvgInfo.getCompanyId();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Student student = new Student();
        student.setGmtCreate(now);
        student.setCreator(loginId);
        student.setGmtModify(now);
        student.setModifier(loginId);
        student.setIsDeleted(ModelConstants.IS_DELETED_N);
        student.setVersion(ModelConstants.VERSION_INIT);
        student.setStatus(ModelConstants.STUDENT_STATUS_IN);
        student.setLoginId(loginId);
        student.setCompanyId(pvgInfo.getCompanyId());
        student.setGmtEnter(ParamUtils.getTimestamp(paramMap.get(Student.GMT_ENTER)));
        student.setSchool(ParamUtils.getString(paramMap.get(Student.SCHOOL)));
        student.setDescription(ParamUtils.getString(paramMap.get(Student.DESCRIPTION)));
        return student;
    }

    private Student getUpdateStudent(Integer id) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Student dbStudent = this.queryById(id);
        Student orphanStudent = null;
        try {
            orphanStudent = (Student) BeanUtils.cloneBean(dbStudent);
            orphanStudent.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanStudent.setModifier(pvgInfo.getLoginId());
            orphanStudent.setGmtEnter(ParamUtils.getTimestamp(paramMap.get(Student.GMT_ENTER)));
            orphanStudent.setSchool(ParamUtils.getString(paramMap.get(Student.SCHOOL)));
            orphanStudent.setDescription(ParamUtils.getString(paramMap.get(Student.DESCRIPTION)));
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        if (orphanStudent.getGmtEnter().getTime() != dbStudent.getGmtEnter().getTime()) {
            return orphanStudent;
        } else if (!StringUtils.equals(orphanStudent.getSchool(), dbStudent.getSchool())) {
            return orphanStudent;
        } else if (!StringUtils.equals(orphanStudent.getDescription(), dbStudent.getDescription())) {
            return orphanStudent;
        } else {
            return null;
        }
    }

    public Student getDeleteStudent(Integer id) {
        Student dbStudent = this.queryById(id);
        dbStudent.setGmtModify(new Timestamp(System.currentTimeMillis()));
        dbStudent.setModifier(pvgInfo.getLoginId());
        return dbStudent;
    }

    @Override
    public StudentCacheBean queryById(Integer id) {
        if (id == null || id == 0) return null;
        StudentCacheBean bean = studentStore.get(id);
        if (bean == null) {
            Student dbStudent = studentService.queryById(id);
            if (dbStudent == null) {
                log.error("can't find student with id[" + id + "]");
                return null;
            }
            bean = new StudentCacheBean(dbStudent);
            List<SubjectRelation> srList = subjectRelationService.queryByStudentId(id);
            for (SubjectRelation sr : srList) {
                bean.addSubject(subjectProxy.queryById(sr.getSubjectId()));
            }
            studentStore.put(id, bean);
        }
        return bean;
    }

    @Override
    public StudentWrap queryForWrap(Integer id) {
        StudentWrapHelper helper = new StudentWrapHelper();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        StudentCacheBean studentBean = this.queryById(id);
        helper.addWrap(userProxy.queryByLoginId(studentBean.getLoginId()), studentBean);
        return helper.getWrappedForObject();
    }

    @Override
    public void save() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        userProxy.save(ModelConstants.USER_TYPE_STUDENT);
        Student student = getNewStudent(ParamUtils.getString(paramMap.get(AppUser.LOGIN_ID)));
        int studentId = studentService.save(student);
        List<Object> list = ParamUtils.getList(paramMap.get("subjectList"));
        for (Object obj : list) {
            SubjectRelation sr = new SubjectRelation(studentId, ModelConstants.SUBJECT_RELATION_TYPE_STUDENT);
            sr.setSubjectId(ParamUtils.getIdInteger(obj));
            subjectRelationService.save(sr);
        }
    }

    @Override
    public void update() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        //------------------------------------------------------------------//
        List<LessonStudent> lessonStudentList = lessonStudentService.queryListByStudentId(id);
        if (lessonStudentList != null && lessonStudentList.size() > 0) {
            String name = ParamUtils.getString(paramMap.get(AppUser.NAME));
            LessonStudent ls = lessonStudentList.get(0);
            if (!StringUtils.equals(name, ls.getStudentName())) {
                lessonStudentService.updateStudentName(id, name);
                for (LessonStudent lessonStudent : lessonStudentList) {
                    lessonStore.remove(lessonStudent.getLessonId());
                }
            }
        }
        //------------------------------------------------------------------//
        String loginId = ParamUtils.getString(paramMap.get(AppUser.LOGIN_ID));
        userProxy.update(loginId);
        Student student = this.getUpdateStudent(id);
        boolean removeable = false;
        if (student != null) {
            studentService.update(student);
            removeable = true;
        }
        Boolean listChanged = ParamUtils.getBooleanValue(paramMap.get("subjectListChanged"));
        if (listChanged != null && listChanged == true) {
            StudentCacheBean studentBean = this.queryById(ParamUtils.getIdInteger(paramMap.get(BaseModel.ID)));
            List subjectList = studentBean.getSubjectList();
            List oldSubjectList = (List) CollectionUtils.collect(subjectList, new BeanToPropertyValueTransformer(BaseModel.ID));
            List newSubjectList = ParamUtils.getList(paramMap.get("subjectList"));
            List deleteSubjectList = (List) CollectionUtils.subtract(oldSubjectList, newSubjectList);
            List addSubjectList = (List) CollectionUtils.subtract(newSubjectList, oldSubjectList);
            if (deleteSubjectList != null && deleteSubjectList.size() > 0) {
                for (Object obj : deleteSubjectList) {
                    SubjectRelation sr = new SubjectRelation(studentBean.getId(), ModelConstants.SUBJECT_RELATION_TYPE_STUDENT);
                    sr.setSubjectId(ParamUtils.getIdInteger(obj));
                    subjectRelationService.delete(sr);
                }
                removeable = true;
            }
            if (addSubjectList != null && addSubjectList.size() > 0) {
                for (Object obj : addSubjectList) {
                    SubjectRelation sr = new SubjectRelation(studentBean.getId(), ModelConstants.SUBJECT_RELATION_TYPE_STUDENT);
                    sr.setSubjectId(ParamUtils.getIdInteger(obj));
                    subjectRelationService.save(sr);
                }
                removeable = true;
            }
        }
        if (removeable) {
            studentStore.remove(id);
        }
    }

    @Override
    public void delete() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Student studentBean = this.getDeleteStudent(id);
        SubjectRelation sr = new SubjectRelation(id, ModelConstants.SUBJECT_RELATION_TYPE_STUDENT);
        subjectRelationService.delete(sr);
        studentService.delete(studentBean);
        studentStore.remove(id);
        userProxy.delete(studentBean.getLoginId());
    }

    @Override
    public void updateStatus(String status) {
        if (StringUtils.isBlank(status) ||
                (!status.equals(ModelConstants.STUDENT_STATUS_IN) && !status.equals(ModelConstants.STUDENT_STATUS_OUT)))
            return;
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Student studentBean = this.getDeleteStudent(id);
        studentBean.setStatus(status);
        if (status.equals(ModelConstants.STUDENT_STATUS_OUT)) {
            Timestamp gmtQuit = ParamUtils.getTimestamp(paramMap.get(Student.GMT_QUIT));
            studentBean.setGmtQuit(gmtQuit);
        } else if (status.equals(ModelConstants.STUDENT_STATUS_IN)) {
            Timestamp gmtEnter = ParamUtils.getTimestamp(paramMap.get(Student.GMT_ENTER));
            studentBean.setGmtEnter(gmtEnter);
            studentBean.setGmtQuit(null);
        }
        studentService.updateStatus(studentBean);
        studentStore.remove(id);
        if (status.equals(ModelConstants.STUDENT_STATUS_OUT)) {
            userProxy.updateStatus(studentBean.getLoginId(), ModelConstants.USER_STATUS_LOCK);
        }
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setStudentStore(StudentStore studentStore) {
        this.studentStore = studentStore;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }

    public void setSubjectRelationService(SubjectRelationService subjectRelationService) {
        this.subjectRelationService = subjectRelationService;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }

    public void setLessonStudentService(LessonStudentService lessonStudentService) {
        this.lessonStudentService = lessonStudentService;
    }

    public void setLessonStore(LessonStore lessonStore) {
        this.lessonStore = lessonStore;
    }
}
