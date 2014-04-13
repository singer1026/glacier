package com.qinyin.education.service;

import com.qinyin.athene.service.BaseService;
import com.qinyin.education.model.SubjectRelation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubjectRelationService extends BaseService<SubjectRelation, Integer> {
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<SubjectRelation> queryByTeacherId(Integer teacherId);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List<SubjectRelation> queryByStudentId(Integer studentId);
}
