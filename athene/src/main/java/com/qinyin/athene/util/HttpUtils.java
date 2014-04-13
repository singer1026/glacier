/**
 * @author zhaolie
 * @create-time 2010-11-29
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.util;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public static String getRelativeUrl(HttpServletRequest req) {
        String url = req.getRequestURI();
        String path = req.getContextPath();
        return url.substring(path.length());
    }

    public static Map getPutParameter(ServletInputStream servletInputStream) throws IOException {
        BufferedReader in = null;
        Map rtnMap = null;
        try {
            in = new BufferedReader(new InputStreamReader(servletInputStream));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String s = sb.toString();
            rtnMap = new HashMap();
            if (s.contains("&")) {
                String[] arr1 = s.split("&");
                for (String s1 : arr1) {
                    if (s1.contains("=")) {
                        String[] arr2 = s1.split("=");
                        rtnMap.put(URLDecoder.decode(arr2[0], "UTF-8"), URLDecoder.decode(arr2[1], "UTF-8"));
                    }
                }
            } else {
                if (s.contains("=")) {
                    String[] arr = s.split("=");
                    rtnMap.put(URLDecoder.decode(arr[0], "UTF-8"), URLDecoder.decode(arr[1], "UTF-8"));
                }
            }
            return rtnMap;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw e;
            }
        }
    }
}
