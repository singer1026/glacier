/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-9-6
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.proxy.impl;

import com.qinyin.athene.cache.model.UserCacheBean;
import com.qinyin.athene.cache.store.UserStore;
import com.qinyin.athene.constant.ModelConstants;
import com.qinyin.athene.login.PrivilegeInfo;
import com.qinyin.athene.model.AppUser;
import com.qinyin.athene.model.AppUserRoleOrg;
import com.qinyin.athene.proxy.UserProxy;
import com.qinyin.athene.service.UserRoleOrgService;
import com.qinyin.athene.service.UserService;
import com.qinyin.athene.util.AtheneUtils;
import com.qinyin.athene.util.DigestUtils;
import com.qinyin.athene.util.ParamUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class UserProxyImpl implements UserProxy {
    public static Logger log = LoggerFactory.getLogger(UserProxyImpl.class);
    private UserStore userStore;
    private UserService userService;
    private UserRoleOrgService userRoleOrgService;
    private PrivilegeInfo pvgInfo;

    public UserCacheBean queryByLoginId(String loginId) {
        if (StringUtils.isBlank(loginId)) return null;
        UserCacheBean bean = userStore.get(loginId);
        if (bean == null) {
            AppUser dbAppUser = userService.queryById(loginId);
            if (dbAppUser == null) {
                log.error("can't find user with loginId[" + loginId + "]");
                return null;
            }
            bean = new UserCacheBean(dbAppUser);
            List<AppUserRoleOrg> userRoleOrgList = userRoleOrgService.queryListByLoginId(loginId);
            bean.setUserRoleOrgList(userRoleOrgList);
            userStore.put(bean.getLoginId(), bean);
        }
        return bean;
    }

    private AppUser getNewUser(String type) {
        String creator = pvgInfo.getLoginId();
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        AppUser appUser = new AppUser();
        appUser.setGmtCreate(now);
        appUser.setCreator(creator);
        appUser.setGmtModify(now);
        appUser.setModifier(creator);
        appUser.setIsDeleted(ModelConstants.IS_DELETED_N);
        appUser.setVersion(ModelConstants.VERSION_INIT);
        appUser.setStatus(ModelConstants.USER_STATUS_LOCK);
        appUser.setLoginId(ParamUtils.getString(paramMap.get(AppUser.LOGIN_ID)));
        appUser.setPassword(DigestUtils.getInitPassword());
        appUser.setCompanyId(pvgInfo.getCompanyId());
        appUser.setType(type);
        appUser.setName(ParamUtils.getString(paramMap.get(AppUser.NAME)));
        appUser.setSex(ParamUtils.getString(paramMap.get(AppUser.SEX)));
        appUser.setAddress(ParamUtils.getString(paramMap.get(AppUser.ADDRESS)));
        appUser.setQq(ParamUtils.getString(paramMap.get(AppUser.QQ)));
        appUser.setEmail(ParamUtils.getString(paramMap.get(AppUser.EMAIL)));
        appUser.setTelephone(ParamUtils.getString(paramMap.get(AppUser.TELEPHONE)));
        appUser.setMobile(ParamUtils.getString(paramMap.get(AppUser.MOBILE)));
        return appUser;
    }

    private AppUser getUpdateUser(String loginId) {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        AppUser dbAppUser = this.queryByLoginId(loginId);
        AppUser orphanAppUser = null;
        try {
            orphanAppUser = (AppUser) BeanUtils.cloneBean(dbAppUser);
            orphanAppUser.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanAppUser.setModifier(pvgInfo.getLoginId());
            orphanAppUser.setName(ParamUtils.getString(paramMap.get(AppUser.NAME)));
            orphanAppUser.setSex(ParamUtils.getString(paramMap.get(AppUser.SEX)));
            orphanAppUser.setAddress(ParamUtils.getString(paramMap.get(AppUser.ADDRESS)));
            orphanAppUser.setQq(ParamUtils.getString(paramMap.get(AppUser.QQ)));
            orphanAppUser.setEmail(ParamUtils.getString(paramMap.get(AppUser.EMAIL)));
            orphanAppUser.setTelephone(ParamUtils.getString(paramMap.get(AppUser.TELEPHONE)));
            orphanAppUser.setMobile(ParamUtils.getString(paramMap.get(AppUser.MOBILE)));
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        if (!StringUtils.equals(orphanAppUser.getAddress(), dbAppUser.getAddress())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getName(), dbAppUser.getName())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getSex(), dbAppUser.getSex())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getQq(), dbAppUser.getQq())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getEmail(), dbAppUser.getEmail())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getTelephone(), dbAppUser.getTelephone())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getMobile(), dbAppUser.getMobile())) {
            return orphanAppUser;
        } else {
            return null;
        }
    }

    private AppUser getUpdateOwnUser() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        AppUser dbAppUser = this.queryByLoginId(pvgInfo.getLoginId());
        AppUser orphanAppUser = null;
        try {
            orphanAppUser = (AppUser) BeanUtils.cloneBean(dbAppUser);
            orphanAppUser.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanAppUser.setModifier(pvgInfo.getLoginId());
            orphanAppUser.setAddress(ParamUtils.getString(paramMap.get(AppUser.ADDRESS)));
            orphanAppUser.setQq(ParamUtils.getString(paramMap.get(AppUser.QQ)));
            orphanAppUser.setEmail(ParamUtils.getString(paramMap.get(AppUser.EMAIL)));
            orphanAppUser.setTelephone(ParamUtils.getString(paramMap.get(AppUser.TELEPHONE)));
            orphanAppUser.setMobile(ParamUtils.getString(paramMap.get(AppUser.MOBILE)));
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        if (!StringUtils.equals(orphanAppUser.getAddress(), dbAppUser.getAddress())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getQq(), dbAppUser.getQq())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getEmail(), dbAppUser.getEmail())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getTelephone(), dbAppUser.getTelephone())) {
            return orphanAppUser;
        } else if (!StringUtils.equals(orphanAppUser.getMobile(), dbAppUser.getMobile())) {
            return orphanAppUser;
        } else {
            return null;
        }
    }

    private AppUser getDeleteUser(String loginId) {
        AppUser dbAppUser = this.queryByLoginId(loginId);
        dbAppUser.setGmtModify(new Timestamp(System.currentTimeMillis()));
        dbAppUser.setModifier(pvgInfo.getLoginId());
        return dbAppUser;
    }

    private AppUser getUpdatePasswordUser() {
        Map<String, Object> paramMap = AtheneUtils.getParamMap();
        AppUser dbAppUser = this.queryByLoginId(pvgInfo.getLoginId());
        AppUser orphanAppUser = null;
        try {
            orphanAppUser = (AppUser) BeanUtils.cloneBean(dbAppUser);
            orphanAppUser.setGmtModify(new Timestamp(System.currentTimeMillis()));
            orphanAppUser.setModifier(pvgInfo.getLoginId());
            orphanAppUser.setPassword(DigestUtils.md5Hex(ParamUtils.getString(paramMap.get("passwordNew"))));
        } catch (Exception e) {
            log.error("catch exception ", e);
            return null;
        }
        return orphanAppUser;
    }

    @Override
    public int save(String type) {
        AppUser appUser = this.getNewUser(type);
        return userService.save(appUser);
    }

    @Override
    public void update(String loginId) {
        AppUser appUser = this.getUpdateUser(loginId);
        if (appUser != null) {
            userService.update(appUser);
            userStore.remove(appUser.getLoginId());
        }
    }

    @Override
    public void delete(String loginId) {
        AppUser appUser = this.getDeleteUser(loginId);
        if (appUser != null) {
            userService.delete(appUser);
            userStore.remove(appUser.getLoginId());
        }
    }

    @Override
    public void updateOwn() {
        AppUser appUser = this.getUpdateOwnUser();
        if (appUser != null) {
            userService.update(appUser);
            userStore.remove(appUser.getLoginId());
        }
    }

    @Override
    public void updatePassword() {
        AppUser appUser = this.getUpdatePasswordUser();
        if (appUser != null) {
            if (!appUser.getLoginId().startsWith("test")) {
                userService.updatePassword(appUser);
                userStore.remove(appUser.getLoginId());
            }
        }
    }

    @Override
    public void updateStatus(String loginId, String status) {
        AppUser appUser = this.getDeleteUser(loginId);
        if (appUser != null) {
            appUser.setStatus(status);
            userService.updateStatus(appUser);
            userStore.remove(appUser.getLoginId());
        }
    }

    public void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setUserRoleOrgService(UserRoleOrgService userRoleOrgService) {
        this.userRoleOrgService = userRoleOrgService;
    }

    public void setPvgInfo(PrivilegeInfo pvgInfo) {
        this.pvgInfo = pvgInfo;
    }
}
