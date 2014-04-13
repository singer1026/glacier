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
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ComboBox extends Field {
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

    protected String getField(Map params) throws TemplateModelException {
        String id = ParamUtils.getString(params.get("_id"));
        String name = ParamUtils.getString(params.get("_name"));
        String value = ParamUtils.getString(params.get("_value"));
        String labelCls = getString(params, "labelCls");
        String cls = getString(params, "cls");
        String style = getString(params, "style");
        List<Map<String, Object>> list = getList(params, "list");
        String listKey = getString(params, "listKey");
        String listValue = getString(params, "listValue");
        String defaultValue = getString(params, "defaultValue");
        boolean existReadOnly = getBoolean(params, "existReadOnly", false);
        StringBuilder sb = new StringBuilder();
        sb.append("<select class=\"fm-combo-box");
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
        sb.append(">");
        if (defaultValue == null) {
            sb.append("<option value=\"\">请选择</option>");
        }
        for (Map<String, Object> map : list) {
            sb.append("<option value=\"").append(map.get(listValue)).append("\"");
            if (value != null) {
                if (map.get(listValue) != null && StringUtils.equals(ParamUtils.getString(map.get(listValue)), value)) {
                    sb.append(" selected=\"selected\"");
                }
            } else if (defaultValue != null) {
                if (map.get(listValue) != null && StringUtils.equals(ParamUtils.getString(map.get(listValue)), defaultValue)) {
                    sb.append(" selected=\"selected\"");
                }
            }
            sb.append(">").append(map.get(listKey)).append("</option>");
        }
        sb.append("</select>");
        return sb.toString();
    }
}
