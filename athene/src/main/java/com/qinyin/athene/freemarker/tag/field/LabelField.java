/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag.field;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;

public class LabelField extends Field {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        init(env, params);
        StringBuilder sb = new StringBuilder();
        sb.append(getTdHead(params)).append(getLayoutHead(params));
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
        String label = getString(params, "label");
        boolean require = getBoolean(params, "require", false);
        StringBuilder sb = new StringBuilder();
        if (label != null) {
            sb.append("<label class=\"fm-label\"");
            sb.append(">");
            if (require) {
                sb.append("<span class=\"required\">*</span>");
            }
            sb.append(label);
            sb.append("</label>");
        }
        return sb.toString();
    }
}
