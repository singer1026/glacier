package com.qinyin.athene.model;

public class AppCompany extends AdvanceModel {
    public static final String NAME = "name";
    private String name;
    public static final String ADDRESS = "address";
    private String address;

    public static final String SQL_FILE_NAME = "AppCompany";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}