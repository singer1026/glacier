/**
 * @author zhaolie
 * @create-time 2010-7-4
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.login;

import com.qinyin.athene.util.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtil {
    public static Logger log = LoggerFactory.getLogger(CookieUtil.class);
    public static final String COOKIE_NAME = "_glacier_";
    public static final String COOKIE_JOINT = "|";

    public void init() {
    }

    public LoginDo getCookie(HttpServletRequest request) throws UnsupportedEncodingException {
        String loginDoStr = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CookieUtil.COOKIE_NAME.equals(cookie.getName())) {
                    if (cookie.getValue() != null) {
                        loginDoStr = cookie.getValue();
                    }
                }
            }
        }
        if (loginDoStr == null) {
            return null;
        }
        return str2LoginDo(loginDoStr);
    }

    public void write2Response(HttpServletResponse response, LoginDo loginDo) throws UnsupportedEncodingException {
        String base64 = loginDo2Str(loginDo);
        String value = URLEncoder.encode(base64, "UTF-8");
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getCookieValue(LoginDo loginDo) throws UnsupportedEncodingException {
        String base64 = loginDo2Str(loginDo);
        return URLEncoder.encode(base64, "UTF-8");
    }

    private LoginDo str2LoginDo(String loginDoStr) throws UnsupportedEncodingException {
        String bas64 = URLDecoder.decode(loginDoStr, "UTF-8");
        String s = DigestUtils.decodeBase64(bas64);
        String[] arr = StringUtils.split(s, COOKIE_JOINT);
        LoginDo loginDo = new LoginDo();
        loginDo.setSessionId(arr[0]);
        loginDo.setLoginId(arr[1]);
        loginDo.setPassword(arr[2]);
        return loginDo;
    }

    private String loginDo2Str(LoginDo loginDo) {
        StringBuilder sb = new StringBuilder();
        sb.append(loginDo.getSessionId()).append(COOKIE_JOINT);
        sb.append(loginDo.getLoginId()).append(COOKIE_JOINT);
        sb.append(loginDo.getPassword());
        return DigestUtils.encodeBase64(sb.toString());
    }
}
