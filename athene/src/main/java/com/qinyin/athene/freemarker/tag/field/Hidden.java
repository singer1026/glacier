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

public class Hidden extends Field {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        String name = getName(params);
        String id = getId(env, name);
        String value = ParamUtils.getString(getValue(env, params));
        String defaultValue = getString(params, "defaultValue");
        StringBuilder sb = new StringBuilder();
        sb.append("<input type=\"hidden\"");
        if (name != null) {
            sb.append(" id=\"").append(id).append("\"");
            sb.append(" name=\"").append(name).append("\"");
        }
        if (value != null) {
            sb.append(" value=\"").append(value).append("\"");
        } else if (defaultValue != null) {
            sb.append(" value=\"").append(defaultValue).append("\"");
        }
        sb.append(">");
        env.getOut().write(sb.toString());
    }
}
