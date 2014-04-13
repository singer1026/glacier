/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag.field;

import com.qinyin.athene.freemarker.tag.Widget;
import com.qinyin.athene.util.ParamUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateModelException;

import java.util.Map;

public abstract class Field extends Widget {

    protected void init(Environment env, Map params) throws TemplateModelException {
        String name = getName(params);
        params.put("_name", name);
        params.put("_id", getId(env, name));
        params.put("_value", getValue(env, params));
    }

    protected String getTdHead(Map params) {
        boolean td = getBoolean(params, "td", true);
        return td ? "<td valign=\"top\">" : "";
    }

    protected String getTdFoot(Map params) {
        boolean td = getBoolean(params, "td", true);
        return td ? "</td>" : "";
    }

    protected String getLayoutHead(Map params) {
        boolean layout = getBoolean(params, "layout", true);
        if (!layout) return "";
        String layoutCls = getString(params, "layoutCls");
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"fm-item");
        if (layoutCls != null) {
            sb.append(" ").append(layoutCls);
        }
        sb.append("\">");
        return sb.toString();
    }

    protected String getLayoutFoot(Map params) {
        boolean layout = getBoolean(params, "layout", true);
        if (!layout) return "";
        return "</div>";
    }

    protected String getLabel(Map params) {
        String label = getString(params, "label");
        if (label == null) return "";
        String labelCls = getString(params, "labelCls");
        String id = ParamUtils.getString(params.get("_id"));
        boolean require = getBoolean(params, "require", false);
        StringBuilder sb = new StringBuilder();
        sb.append("<label class=\"fm-label");
        if (labelCls != null) {
            sb.append(" ").append(labelCls).append("\"");
        } else {
            sb.append("\"");
        }
        if (id != null) {
            sb.append(" for=\"").append(id).append("\"");
        }
        sb.append(">");
        if (require) {
            sb.append("<span class=\"required\">*</span>");
        }
        sb.append(label).append("</label>");
        return sb.toString();
    }

    protected String getExplain(Map params) {
        String explain = getString(params, "explain");
        if (explain == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"fm-explain\">").append(explain).append("</div>");
        return sb.toString();
    }

    protected String getErrorLabel(Map params) {
        boolean error = getBoolean(params, "error", true);
        if (!error) return "";
        String id = ParamUtils.getString(params.get("_id"));
        if (id == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<div id=\"").append(id).append("-error\" class=\"fm-error\">");
        sb.append("<div class=\"btn-icon icon-error fl\"></div>");
        sb.append("<div class=\"fm-error-content fl\"></div><div class=\"clear\"/></div>");
        return sb.toString();
    }
}
