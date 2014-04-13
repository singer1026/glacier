/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-4-20
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.sql.velocity;

import com.qinyin.athene.sql.SqlTemplate;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.util.Map;

public class VelocitySqlTemplate implements SqlTemplate {
    private Template template;

    public VelocitySqlTemplate(Template template) {
        this.template = template;
    }

    public String merge(Map<String, Object> param) {
        VelocityContext context = new VelocityContext(param);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
