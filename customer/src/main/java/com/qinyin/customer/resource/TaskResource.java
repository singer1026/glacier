/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-7
 * @email zhaolie43@gmail.com
 */
package com.qinyin.customer.resource;

import com.qinyin.athene.Queryable;
import com.qinyin.athene.annotation.Args;
import com.qinyin.athene.annotation.QueryableMapping;
import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.customer.proxy.TaskProxy;
import com.qinyin.customer.validation.TaskValidation;
import com.qinyin.customer.wrap.TaskWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

@QueryableMapping("/taskQuery")
public class TaskResource implements Queryable {
    public static Logger log = LoggerFactory.getLogger(TaskResource.class);
    private TaskProxy taskProxy;
    private TaskValidation taskValidation;
    private ResourceProxy resourceProxy;

    @Override
    public int queryForCount(Map<String, Object> paramMap) {
        return taskProxy.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap, Integer startIndex, Integer pageSize) {
        paramMap.put("startIndex", startIndex);
        paramMap.put("pageSize", pageSize);
        return taskProxy.queryForList(paramMap);
    }

    @UrlMapping(value = "/taskList", type = ReturnType.FREEMARKER, url = "customer/taskList.ftl")
    public Map<String, Object> list(@Args Map<String, Object> paramMap) {
        paramMap.put("taskStatusList", resourceProxy.getCommonBean("taskStatus").getAppResourceList());
        Date today = DateUtils.getToday();
        paramMap.put("timeStart", today);
        paramMap.put("timeEnd", today);
        return paramMap;
    }

    @UrlMapping(value = "/taskAdd", type = ReturnType.FREEMARKER, url = "customer/taskEdit.ftl")
    public Map<String, Object> add(@Args Map<String, Object> paramMap) {
        return paramMap;
    }

    @UrlMapping(value = "/taskEdit", type = ReturnType.FREEMARKER, url = "customer/taskEdit.ftl")
    public Map<String, Object> edit(@Args Map<String, Object> paramMap) throws Exception {
        return queryById(paramMap);
    }

    private Map<String, Object> queryById(Map<String, Object> paramMap) throws Exception {
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        if (id == null || id == 0) {
            paramMap.put("error", "ID为空");
        } else {
            TaskWrap wrap = taskProxy.queryForWrap(id);
            if (wrap == null) {
                paramMap.put("error", "数据已删除");
            } else {
                paramMap.put("task", wrap);
            }
        }
        return paramMap;
    }

    @UrlMapping(value = "/taskSave", type = ReturnType.JSON)
    public DataInfoBean save() {
        DataInfoBean info = null;
        try {
            info = taskValidation.simpleValidate();
            if (info.hasError()) return info;
            taskProxy.save();
        } catch (Exception e) {
            log.error("save task error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/taskUpdate", type = ReturnType.JSON)
    public DataInfoBean update() {
        DataInfoBean info = null;
        try {
            info = taskValidation.simpleValidate();
            if (info.hasError()) return info;
            taskProxy.update();
        } catch (Exception e) {
            log.error("update task error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/taskDelete", type = ReturnType.JSON)
    public DataInfoBean delete() {
        DataInfoBean info = AtheneUtils.getInfo();
        try {
            taskProxy.delete();
        } catch (Exception e) {
            log.error("delete task error", e);
            info.setException(e);
        }
        return info;
    }

    @UrlMapping(value = "/taskApprove", type = ReturnType.JSON)
    public DataInfoBean approve() {
        DataInfoBean info = null;
        try {
            info = taskValidation.approveValidate();
            if (info.hasError()) return info;
            taskProxy.approve();
        } catch (Exception e) {
            log.error("approve task error", e);
            info.setException(e);
        }
        return info;
    }

    public void setTaskProxy(TaskProxy taskProxy) {
        this.taskProxy = taskProxy;
    }

    public void setTaskValidation(TaskValidation taskValidation) {
        this.taskValidation = taskValidation;
    }

    public void setResourceProxy(ResourceProxy resourceProxy) {
        this.resourceProxy = resourceProxy;
    }
}
