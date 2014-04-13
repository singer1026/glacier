package com.qinyin.education.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.education.model.Subject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubjectService extends BaseService<Subject, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<Subject> queryListByCompanyId(Integer companyId);
}
