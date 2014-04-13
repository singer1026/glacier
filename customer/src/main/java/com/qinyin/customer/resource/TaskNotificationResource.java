/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-8-7
 * @email zhaolie43@gmail.com
 */
package com.qinyin.customer.resource;

import com.qinyin.athene.annotation.UrlMapping;
import com.qinyin.athene.constant.ReturnType;
import com.qinyin.athene.model.DataInfoBean;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.customer.proxy.TaskNotificationProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskNotificationResource {
    public static Logger log = LoggerFactory.getLogger(TaskNotificationResource.class);
    private TaskNotificationProxy taskNotificationProxy;

    @UrlMapping(value = "/queryTodayTaskNotification", type = ReturnType.JSON)
    public DataInfoBean queryTodayList() {
        DataInfoBean info = AtheneUtils.getInfo();
        try {
            info.setData(taskNotificationProxy.queryTodayList());
        } catch (Exception e) {
            log.error("query today taskNotification error", e);
            info.setException(e);
        }
        return info;
    }

    public void setTaskNotificationProxy(TaskNotificationProxy taskNotificationProxy) {
        this.taskNotificationProxy = taskNotificationProxy;
    }
}
