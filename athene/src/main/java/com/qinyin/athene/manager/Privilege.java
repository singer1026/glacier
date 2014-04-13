package com.qinyin.athene.manager;


import java.util.List;

public interface Privilege {
    public List<String> getUserRoleListByLoginId(String loginId);

    public List<String> getBaseRoleListByLoginId(String loginId);

    public String getUserNameByLoginId(String loginId);
}
