/**
 * @author zhaolie
 * @version 1.0
 * @create-time 10-12-15
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.execution.impl;

import com.qinyin.athene.Queryable;
import com.qinyin.athene.execution.Execution;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryExecution implements Execution {
    private Queryable queryable;
    private Map<String, Object> paramMap;
    private Integer pageSize;//每页元素个数
    private Integer totalPage;//总页数
    private Integer curPage;//当前页
    private Integer totalCount;//总记录数
    private Integer start;
    private List<Object> items;//元素集合

    public QueryExecution() {
    }

    public QueryExecution(HttpServletRequest req, Queryable queryable, Map<String, Object> argsMap) {
        String _pageSize = req.getParameter("pageSize");
        String _curPage = req.getParameter("curPage");
        if (StringUtils.isNotBlank(_pageSize) && StringUtils.isNotBlank(_curPage)) {
            this.pageSize = Integer.parseInt(_pageSize);
            Integer curPageInteger = Integer.parseInt(_curPage);
            if (curPageInteger < 1) {
                this.curPage = 1;
            } else {
                this.curPage = curPageInteger;
            }
        }
        this.queryable = queryable;
        this.paramMap = argsMap;
    }

    public Object excute() throws Exception {
        if (this.pageSize != null && this.curPage != null) {
            this.totalCount = queryable.queryForCount(paramMap);
            this.totalPage = totalCount / pageSize;
            if (totalCount % pageSize > 0) {
                totalPage++;
            }
            if (curPage > totalPage) {
                this.curPage = totalPage;
            }
            if (totalPage == 0) {
                this.start = 0;
                this.items = new ArrayList<Object>(0);
            } else {
                this.start = (curPage - 1) * pageSize;
                this.items = queryable.queryForList(paramMap, start, pageSize);
            }
        } else {
            this.items = queryable.queryForList(paramMap, null, null);
            this.start = 0;
            this.totalCount = this.items.size();
            this.totalPage = 1;
        }
        return this;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public List<Object> getItems() {
        return items;
    }
}
