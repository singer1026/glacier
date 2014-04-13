/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-5
 * @email zhaolie43@gmail.com
 */
package com.qinyin.education.proxy.impl;

import com.qinyin.athene.dao.JdbcDao;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.education.cache.model.ClassroomCacheBean;
import com.qinyin.education.cache.store.ClassroomStore;
import com.qinyin.education.model.Classroom;
import com.qinyin.education.proxy.ClassroomProxy;
import com.qinyin.education.service.ClassroomService;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ClassroomProxyImpl implements ClassroomProxy {
    public static Logger log = LoggerFactory.getLogger(ClassroomProxyImpl.class);
    private ClassroomStore classroomStore;
    private ClassroomService classroomService;
    private PrivilegeInfo pvgInfo;
    private JdbcDao jdbcDao;

    @Override
    public ClassroomCacheBean queryById(Integer id) {
        if (id == null || id == 0) return null;
        ClassroomCacheBean bean = classroomStore.get(id);
        if (bean == null) {
            Classroom dbClassroom = classroomService.queryById(id);
            if (dbClassroom == null) {
                log.error("can't find classroom with id[" + id + "]");
                return null;
            }
            bean = new ClassroomCacheBean(dbClassroom);
            classroomStore.put(id, bean);
        }
        return bean;
    }

    @Override
    public List<ClassroomCacheBean> queryListForCompany() {
        Integer companyId = pvgInfo.getCompanyId();
        List<ClassroomCacheBean> rtnList = new ArrayList<ClassroomCacheBean>();
        List list = classroomStore.getListByCompanyId(companyId);
        if (list == null || list.size() == 0) {
            List<Classroom> classroomList = classroomService.queryListByCompanyId(companyId);
            if (classroomList == null || classroomList.size() == 0) {
                log.error("can't find any classroom with companyId[" + companyId + "]");
                return rtnList;
            }
            list = (List) CollectionUtils.collect(classroomList, new BeanToPropertyValueTransformer(BaseModel.ID));
            classroomStore.putList(companyId, list);
        }
        for (Object obj : list) {
            Integer id = ParamUtils.getIdInteger(obj);
            ClassroomCacheBean classroomCacheBean = this.queryById(id);
            if (classroomCacheBean != null) {
                rtnList.add(classroomCacheBean);
            }
        }
        return rtnList;
    }

    public void setClassroomStore(ClassroomStore classroomStore) {
        this.classroomStore = classroomStore;
    }

    public void setClassroomService(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    public void setJdbcDao(JdbcDao jdbcDao) {
        this.jdbcDao = jdbcDao;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
