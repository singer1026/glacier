/**
 * @author zhaolie
 * @create-time 2010-11-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    public void init(ServletContext servletContext);

    public void render(HttpServletRequest request, HttpServletResponse response, Object model, String path) throws Exception;
}
