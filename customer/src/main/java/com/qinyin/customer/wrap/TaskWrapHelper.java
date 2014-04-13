/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-12
 * @email zhaolie43@gmail.com
 */
package com.qinyin.customer.wrap;

import com.qinyin.athene.cache.model.ResourceCacheBean;
import com.qinyin.athene.manager.Privilege;
import com.qinyin.athene.proxy.ResourceProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.util.DateUtils;
import com.qinyin.customer.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskWrapHelper {
    private List taskWrapList = new ArrayList();
    private Privilege pvg;
    private ResourceCacheBean taskStatusResource;


    public TaskWrapHelper() {
        ResourceProxy resourceProxy = (ResourceProxy) BeanFactoryHolder.getBean("resourceProxy");
        taskStatusResource = resourceProxy.getCommonBean("taskStatus");
        pvg = (Privilege) BeanFactoryHolder.getBean("pvg");
    }

    public void addWrap(Task task) {
        if (task == null) return;
        taskWrapList.add(new TaskWrap(task));
    }

    public List getWrappedForList() {
        for (Object obj : taskWrapList) {
            TaskWrap wrap = (TaskWrap) obj;
            wrap.setGmtCreateDisplay(DateUtils.formatDateTime(wrap.getGmtCreate()));
            wrap.setGmtModifyDisplay(DateUtils.formatDateTime(wrap.getGmtModify()));
            wrap.setDisposeTimeDisplay(DateUtils.formatDateTime(wrap.getDisposeTime()));
            wrap.setNotifyDateDisplay(DateUtils.formatDate(wrap.getNotifyDate()));
            wrap.setCreatorDisplay(pvg.getUserNameByLoginId(wrap.getCreator()));
            wrap.setModifierDisplay(pvg.getUserNameByLoginId(wrap.getModifier()));
            wrap.setDisposerDisplay(pvg.getUserNameByLoginId(wrap.getDisposer()));
            wrap.setStatusDisplay(taskStatusResource.getName(wrap.getStatus()));
        }
        return taskWrapList;
    }

    public TaskWrap getWrappedForObject() {
        List wrappedList = this.getWrappedForList();
        return wrappedList.size() > 0 ? (TaskWrap) wrappedList.get(0) : null;
    }
}
