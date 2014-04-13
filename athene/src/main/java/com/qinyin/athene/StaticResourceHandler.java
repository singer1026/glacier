/**
 * @author zhaolie
 * @create-time 2010-11-24
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene;

import com.qinyin.athene.cache.model.WallpaperLinkCacheBean;
import com.qinyin.athene.proxy.WallpaperLinkProxy;
import com.qinyin.athene.singleton.BeanFactoryHolder;
import com.qinyin.athene.singleton.SystemConfiguration;
import com.qinyin.athene.util.HttpUtils;
import com.qinyin.athene.util.ParamUtils;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.util.*;

public class StaticResourceHandler {
    private static final String MIME_OCTET_STREAM = "application/octet-stream";
    private static final int MAX_BUFFER_SIZE = 4096;
    public static Logger log = LoggerFactory.getLogger(StaticResourceHandler.class);
    private final ServletContext servletContext;
    private long expires = 1000 * 60 * 60 * 12;
    private String maxAge = 60 * 60 * 12 + "";

    public StaticResourceHandler(ServletConfig config) {
        this.servletContext = config.getServletContext();
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String url = HttpUtils.getRelativeUrl(req);
        if (url.toUpperCase().startsWith("/WEB-INF/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        File file = null;
        if (url.equals("/getWallpaper.jpg")) {
            file = getWallpaper(req, resp);
        } else {
            file = new File(servletContext.getRealPath(url));
        }
        if (file == null || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Boolean fileLinkEnable = ParamUtils.getBooleanValue(SystemConfiguration.getInstance().getProperty("file.link.enable"));
        WallpaperLinkProxy wallpaperLinkProxy = (WallpaperLinkProxy) BeanFactoryHolder.getBean("wallpaperLinkProxy");
        if (fileLinkEnable != null && fileLinkEnable == true) {
            if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".gif") || url.endsWith(".js")) {
                String realPath = file.getPath().substring(servletContext.getRealPath("/").length());
                String filePath = realPath.replace(File.separator, "/");
                WallpaperLinkCacheBean wallpaperLinkCacheBean = wallpaperLinkProxy.queryByFilePath(filePath);
                if (wallpaperLinkCacheBean != null) {
                    String fileLink = wallpaperLinkCacheBean.getFileLink();
                    if (StringUtils.isNotBlank(fileLink)) {
                        resp.sendRedirect(fileLink);
                        return;
                    }
                }
            }
        }
        long clientModifyDate = req.getDateHeader("If-Modified-Since");
        long serverModifyDate = file.lastModified();
        if (clientModifyDate != (-1) && clientModifyDate / 1000 >= serverModifyDate / 1000) {
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        resp.setDateHeader("Expires", (new Date()).getTime() + expires);
        resp.setHeader("Cache-Control", "max-age=" + maxAge);
        resp.setDateHeader("Last-Modified", serverModifyDate);
        resp.setContentLength((int) file.length());
        resp.setContentType(getMimeType(file.getName()));
        sendFile(file, resp.getOutputStream());
    }

    private File getWallpaper(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String x = req.getParameter("x"), y = req.getParameter("y");
//        x = "1366";
//        y = "768";
        File resolution = new File(this.servletContext.getRealPath("/images/wallpaper/" + x + "_" + y));
        if (resolution.isDirectory()) {
            File[] files = resolution.listFiles();
            if (files == null || files.length == 0) {
                return getSuitable(x, y);
            } else {
                return files[getRandom(files.length)];
            }
        } else {
            return getSuitable(x, y);
        }
    }

    private File getSuitable(String x, String y) {
        File images = new File(this.servletContext.getRealPath("/images/wallpaper"));
        String[] resolutions = images.list();
        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
        for (String name : resolutions) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            String[] array = name.split("_");
            map.put("x", Integer.parseInt(array[0]));
            map.put("y", Integer.parseInt(array[1]));
            list.add(map);
        }
        Collections.sort(list, new resolutionComparator());
        int intX = Integer.parseInt(x), intY = Integer.parseInt(y);
        Map<String, Integer> select = null;
        for (Map<String, Integer> map : list) {
            int mapX = map.get("x"), mapY = map.get("y");
            if (mapX >= intX && mapY >= intY) {
                select = map;
                break;
            }
        }
        if (select == null) {
            select = list.get(list.size() - 1);
        }
        File rtnResolution = new File(this.servletContext.getRealPath("/images/wallpaper/" + select.get("x") + "_" + select.get("y")));
        File[] files = rtnResolution.listFiles();
        if (files == null || files.length == 0) {
            return null;
        } else {
            return files[getRandom(files.length)];
        }
    }

    private class resolutionComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Map<String, Integer> obj1 = (Map<String, Integer>) o1;
            Map<String, Integer> obj2 = (Map<String, Integer>) o2;
            int x1 = obj1.get("x");
            int x2 = obj2.get("x");
            if (x1 == x2) {
                int y1 = obj1.get("y");
                int y2 = obj2.get("y");
                return y1 - y2;
            } else {
                return x1 - x2;
            }
        }
    }

    private int getRandom(int length) {
        return (int) (Math.random() * length);
    }

    private void sendFile(File file, OutputStream output) throws IOException {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            while (true) {
                int n = input.read(buffer);
                if (n == (-1))
                    break;
                output.write(buffer, 0, n);
            }
            output.flush();
        } catch (SocketException e) {
        } catch (ClientAbortException e) {
        } catch (Exception e) {
            log.error("catch exception ", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private String getMimeType(String fileName) {
        if (fileName.contains(".")) {
            int lastIndex = fileName.lastIndexOf(".");
            String suffix = fileName.substring(lastIndex + 1);
            if ("js".equalsIgnoreCase(suffix)) {
                return "text/javascript";
            } else if ("bmp".equalsIgnoreCase(suffix)) {
                return "image/bmp";
            } else if ("jpg".equalsIgnoreCase(suffix)) {
                return "image/jpg";
            } else if ("gif".equalsIgnoreCase(suffix)) {
                return "image/gif";
            } else if ("png".equalsIgnoreCase(suffix)) {
                return "image/png";
            } else if ("css".equalsIgnoreCase(suffix)) {
                return "text/css";
            } else if ("".equalsIgnoreCase(suffix)) {
                return "";
            } else if ("".equalsIgnoreCase(suffix)) {
                return "";
            } else if ("".equalsIgnoreCase(suffix)) {
                return "";
            }
        }
        return null;
    }
}
