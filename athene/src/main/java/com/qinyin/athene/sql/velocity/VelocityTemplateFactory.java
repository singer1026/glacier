/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-4-20
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.sql.velocity;

import com.qinyin.athene.sql.SqlTemplate;
import com.qinyin.athene.sql.TemplateFactory;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;

import java.util.Properties;

public class VelocityTemplateFactory implements TemplateFactory {

    public void init(Properties properties) throws Exception {
        Velocity.init(properties);
    }

    public SqlTemplate getTemplate(String key) {
        Template template = null;
        try {
            template = Velocity.getTemplate(key);
        } catch (Exception e) {
        }
        if (template != null) {
            return new VelocitySqlTemplate(template);
        }
        return null;
    }
}
