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

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CheckboxGroup extends Field {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        init(env, params);
        params.put("_listValue", getListValue(env, params));
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


    protected String getField(Map params) throws TemplateModelException {
        String id = ParamUtils.getString(params.get("_id"));
        String name = ParamUtils.getString(params.get("_name"));
        String value = ParamUtils.getString(params.get("_value"));
        List<Map<String, Object>> valueList = (List<Map<String, Object>>) params.get("_listValue");
        String checkboxName = getString(params, "checkboxName");
        List<Map<String, Object>> list = getList(params, "list");
        String listKey = getString(params, "listKey");
        String listValue = getString(params, "listValue");
        Integer rowLimit = getInteger(params, "rowLimit");
        String bindingValue = getString(params, "bindingValue");
        String tdStyle = getString(params, "tdStyle");
        StringBuilder sb = new StringBuilder();
        sb.append("<table");
        if (name != null) {
            sb.append(" id=\"").append(id).append("\"");
            sb.append(" name=\"").append(name).append("\"");
        }
        sb.append("><tr>");
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            sb.append("<td");
            if (tdStyle != null) {
                sb.append(" style=\"").append(tdStyle).append("\"");
            }
            sb.append(">");
            sb.append("<input type=\"checkbox\"");
            if (id != null) {
                sb.append(" id=\"").append(id).append("-").append(i).append("\"");
            }
            if (checkboxName != null) {
                sb.append(" name=\"").append(checkboxName).append("\"");
            }
            if (bindingValue != null && valueList.size() > 0) {
                for (Map<String, Object> valueMap : valueList) {
                    if (valueMap.get(bindingValue).equals(map.get(listValue))) {
                        sb.append(" checked=\"checked\"");
                    }
                }
            }
            sb.append(" value=\"").append(map.get(listValue)).append("\"").append(">");
            sb.append("<label style=\"color: black; font-weight: normal;\"");
            if (id != null) {
                sb.append(" for=\"").append(id).append("-").append(i).append("\"");
            }
            sb.append(">").append(map.get(listKey)).append("</label>");
            sb.append("</td>");
            if ((i + 1) % rowLimit == 0) {
                sb.append("</tr><tr>");
            }
        }
        sb.append("</tr></table>");
        return sb.toString();
    }

}
