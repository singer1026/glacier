/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-9
 * @email zhaolie43@gmail.com
 */
package com.qinyin.customer.proxy.impl;

import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.BaseModel;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.ParamUtils;
import com.qinyin.customer.constant.CustomerConstant;
import com.qinyin.customer.model.Task;
import com.qinyin.customer.proxy.TaskNotificationProxy;
import com.qinyin.customer.proxy.TaskProxy;
import com.qinyin.customer.service.TaskService;
import com.qinyin.customer.wrap.TaskWrap;
import com.qinyin.customer.wrap.TaskWrapHelper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class TaskProxyImpl implements TaskProxy {
    public static Logger log = LoggerFactory.getLogger(TaskProxyImpl.class);
    private TaskNotificationProxy taskNotificationProxy;
    private TaskService taskService;
    private PrivilegeInfo pvgInfo;

    @Override
    public Integer queryForCount(Map<String, Object> paramMap) {
        return taskService.queryForCount(paramMap);
    }

    @Override
    public List<Object> queryForList(Map<String, Object> paramMap) {
        TaskWrapHelper helper = new TaskWrapHelper();
        List taskList = taskService.queryForList(paramMap);
        for (Object obj : taskList) {
            helper.addWrap((Task) obj);
        }
        return helper.getWrappedForList();
    }

    private Task getNewTask() {
        String creator = pvgInfo.getLoginId();
        Integer companyId = pvgInfo.getCompanyId();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Task task = new Task();
        task.setGmtCreate(now);
        task.setCreator(creator);
        task.setGmtModify(now);
        task.setModifier(creator);
        task.setIsDeleted(ModelConstants.IS_DELETED_N);
        task.setVersion(ModelConstants.VERSION_INIT);
        task.setCompanyId(companyId);
        task.setStatus(CustomerConstant.TASK_STATUS_ACTIVE);
        task.setNotifyDate(ParamUtils.getTimestamp(paramMap.get(Task.NOTIFY_DATE)));
        task.setTitle(ParamUtils.getString(paramMap.get(Task.TITLE)));
        task.setDescription(ParamUtils.getString(paramMap.get(Task.DESCRIPTION)));
        return task;
    }

    private Task getUpdateTask(Integer id) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Task dbTask = this.queryById(id);
        Task orphanTask = null;
        try {
            orphanTask = (Task) BeanUtils.cloneBean(dbTask);
            orphanTask.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanTask.setModifier(pvgInfo.getLoginId());
            orphanTask.setTitle(ParamUtils.getString(paramMap.get(Task.TITLE)));
            orphanTask.setNotifyDate(ParamUtils.getTimestamp(paramMap.get(Task.NOTIFY_DATE)));
            orphanTask.setDescription(ParamUtils.getString(paramMap.get(Task.DESCRIPTION)));
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        if (!StringUtils.equals(dbTask.getTitle(), orphanTask.getTitle())) {
            return orphanTask;
        } else if (!StringUtils.equals(dbTask.getDescription(), orphanTask.getDescription())) {
            return orphanTask;
        } else if (DateUtils.compare(dbTask.getNotifyDate(), orphanTask.getNotifyDate()) != 0) {
            return orphanTask;
        } else {
            return null;
        }
    }

    private Task getDeleteTask(Integer id) {
        Task dbTask = this.queryById(id);
        dbTask.setGmtModify(new Timestamp(System.currentTimeMillis()));
        dbTask.setModifier(pvgInfo.getLoginId());
        return dbTask;
    }

    private Task getApproveTask(Integer id) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String disposer = pvgInfo.getLoginId();
        Task dbTask = this.queryById(id);
        dbTask.setGmtModify(now);
        dbTask.setModifier(disposer);
        dbTask.setDisposer(disposer);
        dbTask.setDisposeTime(now);
        dbTask.setStatus(CustomerConstant.TASK_STATUS_FINISH);
        dbTask.setDisposeOpinion(ParamUtils.getString(paramMap.get(Task.DISPOSE_OPINION)));
        return dbTask;
    }

    private Task queryById(Integer id) {
        if (id == null || id == 0) return null;
        Task dbTask = taskService.queryById(id);
        if (dbTask == null) {
            log.error("can't find task with id[" + id + "]");
            return null;
        }
        taskNotificationProxy.updateRead(id);
        return dbTask;
    }

    @Override
    public TaskWrap queryForWrap(Integer id) {
        TaskWrapHelper helper = new TaskWrapHelper();
        helper.addWrap(this.queryById(id));
        return helper.getWrappedForObject();
    }


    @Override
    public void save() {
        Task task = getNewTask();
        int taskId = taskService.save(task);
        task.setId(taskId);
        taskNotificationProxy.saveByTask(task);
    }

    @Override
    public void update() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Task task = getUpdateTask(id);
        if (task != null) {
            taskService.update(task);
        }
    }

    @Override
    public void delete() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Task task = getDeleteTask(id);
        taskService.delete(task);
        taskNotificationProxy.deleteByTaskId(id);
    }

    @Override
    public void approve() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Integer id = ParamUtils.getIdInteger(paramMap.get(BaseModel.ID));
        Task task = getApproveTask(id);
        taskService.approve(task);
        taskNotificationProxy.deleteByTaskId(id);
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }

    public void setTaskNotificationProxy(TaskNotificationProxy taskNotificationProxy) {
        this.taskNotificationProxy = taskNotificationProxy;
    }
}
