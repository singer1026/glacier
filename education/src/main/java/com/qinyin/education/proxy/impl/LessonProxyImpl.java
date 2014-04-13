/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-16
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.proxy.impl;

import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.cache.model.LessonCacheBean;
import com.qinyin.education.cache.model.SubjectCacheBean;
import com.qinyin.education.cache.store.LessonStore;
import com.qinyin.education.model.Lesson;
import com.qinyin.education.model.LessonPlan;
import com.qinyin.education.model.LessonStudent;
import com.qinyin.education.model.LessonTeacher;
import com.qinyin.education.proxy.LessonProxy;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.service.LessonPlanService;
import com.qinyin.education.service.LessonService;
import com.qinyin.education.service.LessonStudentService;
import com.qinyin.education.service.LessonTeacherService;
import com.qinyin.education.util.LessonUtils;
import com.qinyin.education.wrap.LessonWrap;
import com.qinyin.education.wrap.LessonWrapHelper;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.*;

public class LessonProxyImpl implements LessonProxy {
    public static Logger log = LoggerFactory.getLogger(LessonProxyImpl.class);
    private LessonService lessonService;
    private LessonStore lessonStore;
    private PrivilegeInfo pvgInfo;
    private JdbcDao jdbcDao;
    private LessonPlanService lessonPlanService;
    private LessonTeacherService lessonTeacherService;
    private LessonStudentService lessonStudentService;
    private SubjectProxy subjectProxy;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return lessonService.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        LessonWrapHelper helper = new LessonWrapHelper();
        List lessonList = lessonService.queryForList(paramMap);
        List idList = (List) CollectionUtils.collect(lessonList, new BeanToPropertyValueTransformer(BaseModel.ID));
        for (Object obj : idList) {
            helper.addWrap(this.queryById(ParamUtils.getIdInteger(obj)));
        }
        return helper.getWrappedForList();
    }

    private Lesson getNewLesson() {
        String creator = pvgInfo.getLoginId();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer companyId = pvgInfo.getCompanyId();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Lesson lesson = new Lesson();
        lesson.setGmtCreate(now);
        lesson.setCreator(creator);
        lesson.setGmtModify(now);
        lesson.setModifier(creator);
        lesson.setIsDeleted(ModelConstants.IS_DELETED_N);
        lesson.setVersion(ModelConstants.VERSION_INIT);
        lesson.setCompanyId(companyId);
        lesson.setSubjectId(ParamUtils.getIdInteger(paramMap.get(Lesson.SUBJECT_ID)));
        lesson.setRemark(ParamUtils.getString(paramMap.get(Lesson.REMARK)));
        return lesson;
    }

    private Lesson getUpdateLesson(Integer id) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Lesson dbLesson = this.queryById(id);
        Lesson orphanLesson = null;
        try {
            orphanLesson = (Lesson) BeanUtils.cloneBean(dbLesson);
            orphanLesson.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanLesson.setModifier(pvgInfo.getLoginId());
            orphanLesson.setSubjectId(ParamUtils.getIdInteger(paramMap.get(Lesson.SUBJECT_ID)));
            orphanLesson.setRemark(ParamUtils.getString(paramMap.get(Lesson.REMARK)));
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        if (!dbLesson.getSubjectId().equals(orphanLesson.getSubjectId())) {
            return orphanLesson;
        } else if (!StringUtils.equals(dbLesson.getRemark(), orphanLesson.getRemark())) {
            return orphanLesson;
        } else {
            return null;
        }
    }

    private Lesson getDeleteLesson(Integer id) {
        Lesson dbLesson = this.queryById(id);
        dbLesson.setGmtModify(new Timestamp(System.currentTimeMillis()));
        dbLesson.setModifier(pvgInfo.getLoginId());
        return dbLesson;
    }

    @Override
    public LessonWrap queryForWrap(Integer id) {
        LessonWrapHelper helper = new LessonWrapHelper();
        helper.addWrap(this.queryById(id));
        return helper.getWrappedForObject();
    }

    public LessonCacheBean queryById(Integer id) {
        if (id == null || id == 0) return null;
        LessonCacheBean bean = lessonStore.get(id);
        if (bean == null) {
            Lesson dbLesson = lessonService.queryById(id);
            if (dbLesson == null) {
                log.error("can't find lesson with id[" + id + "]");
                return null;
            }
            bean = new LessonCacheBean(dbLesson);
            List<LessonPlan> lessonPlanList = lessonPlanService.queryListByLessonId(id);
            List<LessonTeacher> lessonTeacherList = lessonTeacherService.queryListByLessonId(id);
            List<LessonStudent> lessonStudentList = lessonStudentService.queryListByLessonId(id);
            bean.setLessonPlanList(lessonPlanList);
            bean.setLessonStudentList(lessonStudentList);
            bean.setLessonTeacherList(lessonTeacherList);
            lessonStore.put(id, bean);
        }
        return bean;
    }

    @Override
    public void save() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Lesson lesson = getNewLesson();
        int lessonId = lessonService.save(lesson);
        this.saveLessonPlan(lessonId);
        this.saveLessonTeacher(lessonId);
        this.saveLessonStudent(lessonId);
    }

    @Override
    public void update() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Lesson lesson = this.getUpdateLesson(id);
        boolean removeable = false;
        if (lesson != null) {
            lessonService.update(lesson);
            removeable = true;
        }
        Boolean lessonPlanChanged = ParamUtils.getBooleanValue(paramMap.get("lessonPlanChanged"));
        if (lessonPlanChanged != null && lessonPlanChanged == true) {
            lessonPlanService.deleteByLessonId(id);
            this.saveLessonPlan(id);
            removeable = true;
        }
        Boolean lessonTeacherChanged = ParamUtils.getBooleanValue(paramMap.get("lessonTeacherChanged"));
        if (lessonTeacherChanged != null && lessonTeacherChanged == true) {
            lessonTeacherService.deleteByLessonId(id);
            this.saveLessonTeacher(id);
            removeable = true;
        }
        Boolean lessonStudentChanged = ParamUtils.getBooleanValue(paramMap.get("lessonStudentChanged"));
        if (lessonStudentChanged != null && lessonStudentChanged == true) {
            lessonStudentService.deleteByLessonId(id);
            this.saveLessonStudent(id);
            removeable = true;
        }
        if (removeable) {
            lessonStore.remove(id);
        }
    }

    private void saveLessonPlan(Integer lessonId) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        List<Object> lessonPlanList = ParamUtils.getList(paramMap.get("lessonPlan"));
        for (Object obj : lessonPlanList) {
            Map<String, Object> lpMap = (Map<String, Object>) obj;
            LessonPlan lessonPlan = new LessonPlan();
            lessonPlan.setLessonId(lessonId);
            lessonPlan.setDayOfWeek(ParamUtils.getInteger(lpMap.get(LessonPlan.DAY_OF_WEEK)));
            lessonPlan.setStartTime(ParamUtils.getInteger(lpMap.get(LessonPlan.START_TIME)));
            lessonPlan.setEndTime(ParamUtils.getInteger(lpMap.get(LessonPlan.END_TIME)));
            lessonPlan.setTimeLength(ParamUtils.getInteger(lpMap.get(LessonPlan.TIME_LENGTH)));
            lessonPlan.setFrequency(ParamUtils.getInteger(lpMap.get(LessonPlan.FREQUENCY)));
            lessonPlan.setClassroomId(ParamUtils.getInteger(lpMap.get(LessonPlan.CLASSROOM_ID)));
            lessonPlanService.save(lessonPlan);
        }
    }

    private void saveLessonTeacher(Integer lessonId) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        List<Object> lessonTeacherList = ParamUtils.getList(paramMap.get("lessonTeacher"));
        for (Object obj : lessonTeacherList) {
            Map<String, Object> ltMap = (Map<String, Object>) obj;
            String teacherName = ParamUtils.getString(ltMap.get(LessonTeacher.TEACHER_NAME));
            Integer teacherId = ParamUtils.getIdInteger(ltMap.get(LessonTeacher.TEACHER_ID));
            LessonTeacher lessonTeacher = new LessonTeacher(teacherId, teacherName);
            lessonTeacher.setLessonId(lessonId);
            lessonTeacherService.save(lessonTeacher);
        }
    }

    private void saveLessonStudent(Integer lessonId) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        List<Object> lessonStudentList = ParamUtils.getList(paramMap.get("lessonStudent"));
        for (Object obj : lessonStudentList) {
            Map<String, Object> lsMap = (Map<String, Object>) obj;
            String studentName = ParamUtils.getString(lsMap.get(LessonStudent.STUDENT_NAME));
            Integer studentId = ParamUtils.getIdInteger(lsMap.get(LessonStudent.STUDENT_ID));
            LessonStudent lessonStudent = new LessonStudent(studentId, studentName);
            lessonStudent.setLessonId(lessonId);
            lessonStudentService.save(lessonStudent);
        }
    }

    @Override
    public void delete() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Lesson lessonBean = this.getDeleteLesson(id);
        lessonService.delete(lessonBean);
        lessonPlanService.deleteByLessonId(id);
        lessonTeacherService.deleteByLessonId(id);
        lessonStudentService.deleteByLessonId(id);
        lessonStore.remove(id);
    }

    public List<Object> queryLessonStatistics() {
        List<Object> lessonStatisticsList = lessonService.queryLessonStatisticsByCompanyId(pvgInfo.getCompanyId());
        for (Object obj : lessonStatisticsList) {
            List<Object> list = ParamUtils.getList(obj);
            for (Object object : list) {
                Map<String, Object> statisticsMap = (Map<String, Object>) object;
                Integer startTime = ParamUtils.getInteger(statisticsMap.get(LessonPlan.START_TIME));
                Integer endTime = ParamUtils.getInteger(statisticsMap.get(LessonPlan.END_TIME));
                statisticsMap.put(LessonPlan.END_TIME, LessonUtils.formatTime(endTime));
                statisticsMap.put(LessonPlan.START_TIME, LessonUtils.formatTime(startTime));
                Integer subjectId = ParamUtils.getInteger(statisticsMap.get(Lesson.SUBJECT_ID));
                SubjectCacheBean subjectCacheBean = subjectProxy.queryById(subjectId);
                if (subjectCacheBean != null) {
                    statisticsMap.put("subjectName", subjectCacheBean.getName());
                }
            }
        }
        return lessonStatisticsList;
    }

    @Override
    public List<Object> queryLessonDayStatistics(List<Object> sourceList) {
        List<Object> rtnList = new ArrayList<Object>();
        for (int i = 0; i < 7; i++) {
            List<Object> singleList = (List<Object>) sourceList.get(i);
            Map<String, Object> total = new HashMap<String, Object>();
            int lessonCount = 0;
            Set<String> subjectNames = new HashSet<String>();
            Set<String> teacherNames = new HashSet<String>();
            for (Object obj : singleList) {
                Map<String, Object> statistics = (Map<String, Object>) obj;
                subjectNames.add(ParamUtils.getString(statistics.get("subjectName")));
                teacherNames.add(ParamUtils.getString(statistics.get("teacherName")));
                lessonCount += ParamUtils.getInteger(statistics.get("count"));
            }
            total.put("subjectNames", subjectNames);
            total.put("teacherCount", teacherNames.size());
            total.put("lessonCount", lessonCount);
            rtnList.add(total);
        }
        return rtnList;
    }

    @Override
    public Map<String, Object> queryLessonTotalStatistics(List<Object> sourceList) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        int lessonCount = 0;
        Set<String> subjectNames = new HashSet<String>();
        Set<String> teacherNames = new HashSet<String>();
        for (int i = 0; i < 7; i++) {
            List<Object> singleList = (List<Object>) sourceList.get(i);
            for (Object obj : singleList) {
                Map<String, Object> statistics = (Map<String, Object>) obj;
                subjectNames.add(ParamUtils.getString(statistics.get("subjectName")));
                teacherNames.add(ParamUtils.getString(statistics.get("teacherName")));
                lessonCount += ParamUtils.getInteger(statistics.get("count"));
            }
        }
        rtnMap.put("subjectNames", subjectNames);
        rtnMap.put("subjectCount", subjectNames.size());
        rtnMap.put("teacherNames", teacherNames);
        rtnMap.put("teacherCount", teacherNames.size());
        rtnMap.put("lessonCount", lessonCount);
        return rtnMap;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setLessonService(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    public void setLessonStore(LessonStore lessonStore) {
        this.lessonStore = lessonStore;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setLessonPlanService(LessonPlanService lessonPlanService) {
        this.lessonPlanService = lessonPlanService;
    }

    public void setLessonTeacherService(LessonTeacherService lessonTeacherService) {
        this.lessonTeacherService = lessonTeacherService;
    }

    public void setLessonStudentService(LessonStudentService lessonStudentService) {
        this.lessonStudentService = lessonStudentService;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }
}
