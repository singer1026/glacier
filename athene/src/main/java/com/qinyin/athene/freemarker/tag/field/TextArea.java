/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag.field;

import com.qinyin.athene.util.ParamUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;

public class TextArea extends Field {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        init(env, params);
        StringBuilder sb = new StringBuilder();
        sb.append(getTdHead(params)).append(getLayoutHead(params));
        sb.append(getLabel(params));
        sb.append(getField(params));
        sb.append(getExplain(params));
        sb.append(getErrorLabel(params));
        sb.append(getLayoutFoot(params)).append(getTdFoot(params));
        env.getOut().write(sb.toString());
    }

    protected String getField(Map params) {
        String id = ParamUtils.getString(params.get("_id"));
        String name = ParamUtils.getString(params.get("_name"));
        String value = ParamUtils.getString(params.get("_value"));
        String cls = getString(params, "cls");
        String style = getString(params, "style");
        boolean existReadOnly = getBoolean(params, "existReadOnly", false);
        StringBuilder sb = new StringBuilder();
        sb.append("<textarea");
        if (style != null) {
            sb.append(" style=\"").append(style).append("\"");
        }
        if (name != null) {
            sb.append(" id=\"").append(id).append("\"");
            sb.append(" name=\"").append(name).append("\"");
        }
        sb.append(">");
        if (value != null) {
            sb.append(value);
        }
        sb.append("</textarea>");
        return sb.toString();
    }
}
