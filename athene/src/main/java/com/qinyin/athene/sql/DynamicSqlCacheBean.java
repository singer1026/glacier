/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-4-20
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.sql;

public class DynamicSqlCacheBean {
    private String id;
    private String type;
    private String description;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
