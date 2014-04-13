/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-26
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class Button extends BaseTag {
    private final String className = "btn-default";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        Writer out = env.getOut();
        boolean hasPermission = checkPermission(params);
        if (!hasPermission) return;
        String cls = getString(params, "cls");
        String fullClassName = className;
        if (cls != null) {
            fullClassName += (" " + cls);
        } else {
            fullClassName += " btn-gray";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"" + fullClassName);
        sb.append("\" onselectstart=\"return false;\"");
        sb.append(" onmouseup=\"athene.dom.removeClass(this,'" + className + "-active')\"");
        sb.append(" onmousedown=\"athene.dom.addClass(this,'" + className + "-active')\"");
        sb.append(" onmouseout=\"athene.dom.replaceClass(this,'" + fullClassName + "')\"");
        sb.append(" onmousemove=\"athene.dom.addClass(this,'" + className + "-hover')\"");
        if (body != null) {
            sb.append(" onclick=\"");
            out.write(sb.toString());
            body.render(out);
            out.write("\">");
        } else {
            sb.append(">");
            out.write(sb.toString());
        }
        String iconCls = getString(params, "iconCls");
        StringBuilder sb2 = new StringBuilder();
        if (iconCls != null) {
            sb2.append("<span class=\"btn-inner\">");
        } else {
            sb2.append("<span class=\"btn-inner\" style=\"padding:4px 14px 4px 16px;\">");
        }
        sb2.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr>");
        if (iconCls != null) {
            sb2.append("<td><span class=\"btn-icon ");
            sb2.append(iconCls);
            sb2.append("\"></span></td>");
        }
        sb2.append("<td style=\"padding-left:3px;\">");
        sb2.append(getString(params, "label"));
        sb2.append("</td></tr></table></span></div>");
        out.write(sb2.toString());
    }
}
