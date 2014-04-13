/**
 * @author zhaolie
 * @create-time 2010-11-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.view;

public class ViewFactoryHolder {
    private static ViewFactory viewFactory;

    public static ViewFactory getViewFactory() {
        return viewFactory;
    }

    public static void setViewFactory(ViewFactory viewFactory) {
        ViewFactoryHolder.viewFactory = viewFactory;
    }

    public static FreeMarkerView getFreeMarkerView() {
        return viewFactory.getFreeMarkerView();
    }
}
