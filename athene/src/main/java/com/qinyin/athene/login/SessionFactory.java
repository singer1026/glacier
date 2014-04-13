/**
 * @author zhaolie
 * @create-time 2010-7-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.login;

import com.qinyin.athene.singleton.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionFactory {
    public static Logger log = LoggerFactory.getLogger(SessionFactory.class);
    private ConcurrentHashMap<String, SessionDo> sessionContext = new ConcurrentHashMap<String, SessionDo>();

    public void init() {
    }

    public SessionDo getSession(String seesionId) {
        SessionDo sessionDo = sessionContext.get(seesionId);
        if (sessionDo == null) {
            log.trace("session with id[{}] is null", seesionId);
            return null;
        }
        if (sessionDo.isExpire(new Date())) {
            log.trace("session with id[{}] is expire", seesionId);
            return null;
        }
        return sessionDo;
    }

    public SessionDo createSession() {
        String sessionExpire = SystemConfiguration.getInstance().getProperty("session.expire");
        Long expire = (new Date()).getTime() + Long.valueOf(sessionExpire);
        String uuid = UUID.randomUUID().toString();
        SessionDo sessionDo = new SessionDo(uuid, expire);
        sessionContext.put(uuid, sessionDo);
        log.info("create a new session with id[{}]", uuid);
        return sessionDo;
    }

    public void removeSession(String sessionId) {
        log.info("remove a session with id[{}]", sessionId);
        sessionContext.remove(sessionId);
    }

    public void cleanSession() {
        Date now = new Date();
        List<String> list = new ArrayList<String>();
        Set<Map.Entry<String, SessionDo>> sessionContextSet = sessionContext.entrySet();
        for (Map.Entry<String, SessionDo> sessionContextEntry : sessionContextSet) {
            SessionDo sessionDo = sessionContextEntry.getValue();
            if (sessionDo.isExpire(now)) {
                list.add(sessionDo.getSessionId());
            }
        }
        for (String sessionId : list) {
            this.removeSession(sessionId);
        }
    }
}