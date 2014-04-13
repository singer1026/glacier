/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-12-11
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag;

import com.qinyin.athene.singleton.JacksonJsonMapper;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class Json extends BaseTag {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Writer out = env.getOut();
        Object _value = super.getList(params, "src");
        if (_value == null) {
            out.write(StringUtils.EMPTY);
        } else {
            out.write(JacksonJsonMapper.getInstance().writeValue(_value));
        }
    }
}
