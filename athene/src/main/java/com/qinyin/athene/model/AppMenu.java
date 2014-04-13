package com.qinyin.athene.model;

public class AppMenu extends AdvanceModel {
    public static final String HAS_PERMISSION = "hasPermission";
    private String hasPermission;
    public static final String NAME = "name";
    private String name;
    public static final String TYPE = "type";
    private String type;
    public static final String ONCLICK = "onclick";
    private String onclick;
    public static final String IMAGE_URL = "imageUrl";
    private String imageUrl;
    public static final String DESCRIPTION = "description";
    private String description;
    public static final String PARENT_ID = "parentId";
    private Integer parentId;
    public static final String ORDERING = "ordering";
    private Integer ordering;

    public static final String SQL_FILE_NAME = "AppMenu";

    public String getHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(String hasPermission) {
        this.hasPermission = hasPermission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }
}