/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.proxy.impl;

import com.qinyin.athene.cache.model.CompanyCacheBean;
import com.qinyin.athene.cache.store.CompanyStore;
import com.qinyin.athene.model.AppCompany;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.proxy.CompanyProxy;
import com.qinyin.athene.service.CompanyService;
import com.qinyin.athene.util.ParamUtils;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompanyProxyImpl implements CompanyProxy {
    public static Logger log = LoggerFactory.getLogger(CompanyProxyImpl.class);
    private CompanyStore companyStore;
    private CompanyService companyService;

    @Override
    public CompanyCacheBean queryById(Integer id) {
        if (id == null || id == 0) return null;
        CompanyCacheBean bean = companyStore.get(id);
        if (bean == null) {
            AppCompany dbCompany = companyService.queryById(id);
            if (dbCompany == null) {
                log.error("can't find company with id[" + id + "]");
                return null;
            }
            bean = new CompanyCacheBean(dbCompany);
            companyStore.put(id, bean);
        }
        return bean;
    }

    @Override
    public List<CompanyCacheBean> queryAllList() {
        List<CompanyCacheBean> rtnList = new ArrayList<CompanyCacheBean>(0);
        List list = companyStore.getList();
        if (list == null || list.size() == 0) {
            List<AppCompany> companyList = companyService.queryAllList();
            if (companyList == null || companyList.size() == 0) {
                log.error("can't find any company");
                return rtnList;
            }
            list = (List) CollectionUtils.collect(companyList, new BeanToPropertyValueTransformer(BaseModel.ID));
            companyStore.putList(list);
        }
        for (Object obj : list) {
            Integer id = ParamUtils.getIdInteger(obj);
            CompanyCacheBean companyCacheBean = this.queryById(id);
            if (companyCacheBean != null) {
                rtnList.add(companyCacheBean);
            }
        }
        return rtnList;
    }

    public void setCompanyStore(CompanyStore companyStore) {
        this.companyStore = companyStore;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}
