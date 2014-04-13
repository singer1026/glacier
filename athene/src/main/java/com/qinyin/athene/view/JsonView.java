/**
 * @author zhaolie
 * @create-time 2010-11-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.view;

import com.qinyin.athene.singleton.JacksonJsonMapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public class JsonView implements View {

    @Override
    public void init(ServletContext servletContext) {
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, Object model, String path) throws Exception {
        OutputStream out = response.getOutputStream();
        response.setContentType("application/json");
        JacksonJsonMapper.getInstance().writeValue(out, model);
    }
}
