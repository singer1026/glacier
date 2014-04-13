/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-14
 * @email zhaolie43@gmail.com
 */
package com.qinyin.customer.proxy.impl;

import com.qinyin.athene.cache.model.RoleCacheBean;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppUserRoleOrg;
import com.qinyin.athene.proxy.RoleProxy;
import com.qinyin.athene.service.UserRoleOrgService;
import com.qinyin.customer.constant.CustomerConstant;
import com.qinyin.customer.model.Task;
import com.qinyin.customer.model.TaskNotification;
import com.qinyin.customer.proxy.TaskNotificationProxy;
import com.qinyin.customer.service.TaskNotificationService;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskNotificationProxyImpl implements TaskNotificationProxy {
    private TaskNotificationService taskNotificationService;
    private UserRoleOrgService userRoleOrgService;
    private PrivilegeInfo pvgInfo;
    private RoleProxy roleProxy;

    @Override
    public void saveByTask(Task task) {
        Set<String> loginIdSet = new HashSet<String>();
        RoleCacheBean roleBean = roleProxy.queryByName(TaskNotification.SAVE_ROLE);
        List<String> userRoleList = roleBean.getUserRoleList();
        for (String roleName : userRoleList) {
            List<AppUserRoleOrg> userRoleOrgList = userRoleOrgService.queryListByUserRole(roleName);
            for (AppUserRoleOrg appUserRoleOrg : userRoleOrgList) {
                loginIdSet.add(appUserRoleOrg.getLoginId());
            }
        }
        for (String loginId : loginIdSet) {
            TaskNotification tn = new TaskNotification();
            tn.setLoginId(loginId);
            tn.setStatus(CustomerConstant.TASK_NOTIFICATION_STATUS_UNREAD);
            tn.setTaskId(task.getId());
            taskNotificationService.save(tn);
        }
    }

    @Override
    public List<Object> queryTodayList() {
        return taskNotificationService.queryListByNotifyDate(new Date());
    }

    @Override
    public void deleteByTaskId(Integer taskId) {
        taskNotificationService.deleteByTaskId(taskId);
    }

    @Override
    public void updateRead(Integer taskId) {
        TaskNotification tn = new TaskNotification();
        tn.setTaskId(taskId);
        tn.setLoginId(pvgInfo.getLoginId());
        tn.setStatus(CustomerConstant.TASK_NOTIFICATION_STATUS_READ);
        taskNotificationService.updateStatus(tn);
    }

    public void setTaskNotificationService(TaskNotificationService taskNotificationService) {
        this.taskNotificationService = taskNotificationService;
    }

    public void setRoleProxy(RoleProxy roleProxy) {
        this.roleProxy = roleProxy;
    }

    public void setUserRoleOrgService(UserRoleOrgService userRoleOrgService) {
        this.userRoleOrgService = userRoleOrgService;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
