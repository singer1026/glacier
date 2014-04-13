/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.resource;

import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.proxy.LessonScheduleProxy;

import java.util.Map;

public class LessonScheduleResource {
    private LessonScheduleProxy lessonScheduleProxy;

    @UrlMapping(value = "/teacherScheduleView", type = ReturnType.FREEMARKER, url = "education/teacherScheduleView.ftl")
    public Map<String, Object> teacherScheduleView(@Args Map<String, Object> paramMap) {
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        if (id == null || id == 0) {
            paramMap.put("error", "ID为空");
        } else {
            paramMap.putAll(lessonScheduleProxy.queryTeacherScheduleByTeacherId(id));
        }
        return paramMap;
    }

    public void setLessonScheduleProxy(LessonScheduleProxy lessonScheduleProxy) {
        this.lessonScheduleProxy = lessonScheduleProxy;
    }
}
