/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.validation.impl;

import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DefenseUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.cache.model.SubjectCacheBean;
import com.qinyin.education.model.Lesson;
import com.qinyin.education.model.LessonStudent;
import com.qinyin.education.model.LessonTeacher;
import com.qinyin.education.model.SubjectRelation;
import com.qinyin.education.proxy.SubjectProxy;
import com.qinyin.education.service.SubjectRelationService;
import com.qinyin.education.validation.LessonValidation;

import java.util.List;
import java.util.Map;

public class LessonValidationImpl implements LessonValidation {
    private SubjectRelationService subjectRelationService;
    private SubjectProxy subjectProxy;

    @Override
    public DataInfoBean simpleValidate() {
        DataInfoBean info = AtheneUtils.getInfo();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        List<Object> lessonTeacherList = ParamUtils.getList(paramMap.get("lessonTeacher"));
        if (lessonTeacherList == null || lessonTeacherList.size() == 0) {
            info.addValidationError("teacher-name-list-" + number + "-error", "必须选择最少一个教师");
        }
        List<Object> lessonStudentList = ParamUtils.getList(paramMap.get("lessonStudent"));
        if (lessonStudentList == null || lessonStudentList.size() == 0) {
            info.addValidationError("student-name-list-" + number + "-error", "必须选择最少一个学生");
        }
        String subjectId = ParamUtils.getString(paramMap.get(Lesson.SUBJECT_ID));
        if (subjectId == null) {
            info.addValidationError("subject-id-" + number + "-error", "必须选择科目");
        }
        List<Object> lessonPlanList = ParamUtils.getList(paramMap.get("lessonPlan"));
        if (lessonPlanList == null || lessonPlanList.size() == 0) {
            info.addValidationError("lp-list-" + number + "-error", "必须设置课程安排");
        }
        String remark = ParamUtils.getString(paramMap.get(Lesson.REMARK));
        if (remark != null) {
            if (DefenseUtils.isUnlawfulChar(remark)) {
                info.addValidationError("remark-" + number + "-error", "备注" + DefenseUtils.UNLAWFUL_CHAR);
            }
        }
        return info;
    }

    @Override
    public DataInfoBean deepValidate() throws Exception {
        DataInfoBean info = AtheneUtils.getInfo();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Map<String, Object> requestMap = AtheneUtils.getRequestMap();
        String number = ParamUtils.getString(requestMap.get("number"));
        Integer subjectId = ParamUtils.getInteger(paramMap.get(Lesson.SUBJECT_ID));
        SubjectCacheBean subjectBean = subjectProxy.queryById(subjectId);
        String subjectName = subjectBean.getName();
        List<Object> lessonStudentList = ParamUtils.getList(paramMap.get("lessonStudent"));
        for (int i = 0; i < lessonStudentList.size(); i++) {
            Map<String, Object> student = (Map<String, Object>) lessonStudentList.get(i);
            String name = ParamUtils.getString(student.get(LessonStudent.STUDENT_NAME));
            List<SubjectRelation> srList = subjectRelationService.queryByStudentId(ParamUtils.getIdInteger(student.get(LessonStudent.STUDENT_ID)));
            if (!hasSubject(srList, subjectId)) {
                String message = "学生[" + name + "]没有被安排" + subjectName + "科目";
                if (i != lessonStudentList.size() - 1) {
                    message += "<br/>";
                }
                info.addValidationError("student-name-list-" + number + "-error", message);
            }
        }
        List<Object> lessonTeacherList = ParamUtils.getList(paramMap.get("lessonTeacher"));
        for (int i = 0; i < lessonTeacherList.size(); i++) {
            Map<String, Object> teacher = (Map<String, Object>) lessonTeacherList.get(i);
            String name = ParamUtils.getString(teacher.get(LessonTeacher.TEACHER_NAME));
            List<SubjectRelation> srList = subjectRelationService.queryByTeacherId(ParamUtils.getIdInteger(teacher.get(LessonTeacher.TEACHER_ID)));
            if (!hasSubject(srList, subjectId)) {
                String message = "教师[" + name + "]没有被安排" + subjectName + "科目";
                if (i != lessonTeacherList.size() - 1) {
                    message += "<br/>";
                }
                info.addValidationError("teacher-name-list-" + number + "-error", message);
            }
        }
        return info;
    }

    private boolean hasSubject(List<SubjectRelation> srList, Integer subjectId) {
        for (SubjectRelation subjectRelation : srList) {
            if (subjectRelation.getSubjectId().equals(subjectId)) {
                return true;
            }
        }
        return false;
    }

    public void setSubjectRelationService(SubjectRelationService subjectRelationService) {
        this.subjectRelationService = subjectRelationService;
    }

    public void setSubjectProxy(SubjectProxy subjectProxy) {
        this.subjectProxy = subjectProxy;
    }
}
