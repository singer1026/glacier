/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag.field;

import com.qinyin.athene.util.DateUtils;
import com.qinyin.athene.util.ParamUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class DateField extends Field {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        boolean hasPermission = checkPermission(params);
        if (!hasPermission) return;
        init(env, params);
        StringBuilder sb = new StringBuilder();
        sb.append(getTdHead(params)).append(getLayoutHead(params));
        sb.append(getLabel(params));
        sb.append(getField(params));
        env.getOut().write(sb.toString());
        if (body != null) body.render(env.getOut());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getExplain(params));
        sb2.append(getErrorLabel(params));
        sb2.append(getLayoutFoot(params)).append(getTdFoot(params));
        env.getOut().write(sb2.toString());
    }

    protected String getField(Map params) {
        String id = ParamUtils.getString(params.get("_id"));
        String name = ParamUtils.getString(params.get("_name"));
        Date value = ParamUtils.getDate(params.get("_value"));
        String cls = getString(params, "cls");
        String style = getString(params, "style");
        String onClick = getString(params, "onClick");
        String onFocus = getString(params, "onFocus");
        String format = getString(params, "format");
        boolean readOnly = getBoolean(params, "readOnly", false);
        boolean existReadOnly = getBoolean(params, "existReadOnly", false);
        StringBuilder sb = new StringBuilder();
        sb.append("<input type=\"text\" class=\"fm-text-field");
        if (cls != null) {
            sb.append(" ").append(cls).append("\"");
        } else {
            sb.append("\"");
        }
        if (style != null) {
            sb.append(" style=\"").append(style).append("\"");
        }
        if (name != null) {
            sb.append(" id=\"").append(id).append("\"");
            sb.append(" name=\"").append(name).append("\"");
        }
        if (value != null) {
            sb.append(" value=\"");
            if (format != null) {
                sb.append(DateUtils.formatDate(ParamUtils.getDate(value), format));
            } else {
                sb.append(DateUtils.formatDate(ParamUtils.getDate(value)));
            }
            sb.append("\"");
        }
        if (readOnly) {
            sb.append(" readOnly=\"readonly\"");
        } else if (value != null && existReadOnly) {
            sb.append(" readOnly=\"readonly\"");
        }
        if (onClick != null) {
            sb.append(" onclick=\"").append(onClick).append("\"");
        }
        if (onFocus != null) {
            sb.append(" onfocus=\"").append(onFocus).append("\"");
        }
        sb.append("/>");
        return sb.toString();
    }
}
