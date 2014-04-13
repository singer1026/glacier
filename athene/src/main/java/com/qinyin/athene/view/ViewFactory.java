/**
 * @author zhaolie
 * @create-time 2010-11-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.view;

import javax.servlet.ServletContext;

public class ViewFactory {
    private FreeMarkerView freeMarkerView;
    private JsonView jsonView;
    private JspView jspView;

    public void init(ServletContext servletContext) {
        freeMarkerView = new FreeMarkerView();
        freeMarkerView.init(servletContext);
        jsonView = new JsonView();
        jsonView.init(servletContext);
        jspView = new JspView();
        jspView.init(servletContext);
    }

    public FreeMarkerView getFreeMarkerView() {
        return freeMarkerView;
    }

    public JsonView getJsonView() {
        return jsonView;
    }

    public JspView getJspView() {
        return jspView;
    }
}
