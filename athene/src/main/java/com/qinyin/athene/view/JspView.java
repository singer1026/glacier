/**
 * @author zhaolie
 * @create-time 2010-11-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.view;

import com.qinyin.athene.singleton.SystemConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {
    private ServletContext servletContext;
    private String dir;

    @Override
    public void init(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.dir = SystemConfiguration.getInstance().getProperty("jsp.dir");
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, Object model, String path) throws Exception {
        servletContext.getRequestDispatcher(this.dir + path).forward(request, response);
    }
}
