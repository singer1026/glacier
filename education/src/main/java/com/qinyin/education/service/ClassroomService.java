package com.qinyin.education.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.education.model.Classroom;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClassroomService extends BaseService<Classroom, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Classroom> queryListByCompanyId(Integer companyId);
}
