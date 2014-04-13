/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Map;

public class ExistError implements TemplateDirectiveModel {
    public static final String ERROR = "error";
    public static final String ERROR_PAGE = "template/error.ftl";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body) throws TemplateException, IOException {
        SimpleScalar error = (SimpleScalar) env.getDataModel().get(ERROR);
        if (error != null && StringUtils.isNotBlank(error.getAsString())) {
            Template template = env.getConfiguration().getTemplate(ERROR_PAGE);
            template.process(env.getDataModel(), env.getOut());
            env.getOut().close();
        }
    }
}
