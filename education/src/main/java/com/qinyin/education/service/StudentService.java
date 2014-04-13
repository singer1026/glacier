package com.qinyin.education.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.education.model.Student;
import org.springframework.transaction.annotation.Transactional;

public interface StudentService extends BaseService<Student, Integer> {
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Student student);
}
