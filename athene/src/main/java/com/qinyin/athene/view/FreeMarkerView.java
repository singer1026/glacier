/**
 * @author zhaolie
 * @create-time 2010-11-28
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.view;

import com.qinyin.athene.freemarker.tag.Button;
import com.qinyin.athene.freemarker.tag.ExistError;
import com.qinyin.athene.freemarker.tag.Json;
import com.qinyin.athene.freemarker.tag.field.*;
import com.qinyin.athene.singleton.CompressorFactory;
import com.qinyin.athene.singleton.SystemConfiguration;
import com.qinyin.athene.util.ParamUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

public class FreeMarkerView implements View {
    public static Logger log = LoggerFactory.getLogger(FreeMarkerView.class);
    private Configuration configuration;

    @Override
    public void init(ServletContext servletContext) {
        configuration = new Configuration();
        configuration.setDefaultEncoding(SystemConfiguration.getInstance().getProperty("freemarker.encoding"));
        configuration.setServletContextForTemplateLoading(servletContext, SystemConfiguration.getInstance().getProperty("freemarker.template.dir"));
        configuration.setSharedVariable("ExistError", new ExistError());
        configuration.setSharedVariable("TextField", new TextField());
        configuration.setSharedVariable("DateField", new DateField());
        configuration.setSharedVariable("LabelField", new LabelField());
        configuration.setSharedVariable("ComboBox", new ComboBox());
        configuration.setSharedVariable("TextArea", new TextArea());
        configuration.setSharedVariable("Password", new Password());
        configuration.setSharedVariable("Hidden", new Hidden());
        configuration.setSharedVariable("Button", new Button());
        configuration.setSharedVariable("CheckboxGroup", new CheckboxGroup());
        configuration.setSharedVariable("Json", new Json());
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, Object model, String path) throws Exception {
        StringWriter sw = null;
        PrintWriter pw = null;
        PrintWriter out = null;
        try {
            Template template = configuration.getTemplate(path);
            response.setContentType("text/html");
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            template.process(model, pw);
            sw.flush();//不需要pw.flush();
            String html = sw.toString();
            Boolean compress = ParamUtils.getBooleanValue(SystemConfiguration.getInstance().getProperty("compress"));
            if (compress != null && compress) {
                html = CompressorFactory.getCompressor().compress(html);
            }
            out = response.getWriter();
            out.write(html);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (pw != null) pw.close();
            if (sw != null) sw.close();
            if (out != null) out.close();
        }
    }

    public String getFileContent(Object model, String path) throws Exception {
        String html = StringUtils.EMPTY;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            Template template = configuration.getTemplate(path);
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            template.process(model, pw);
            sw.flush();
            html = sw.toString();
            html = CompressorFactory.getCompressor().compress(html);
        } catch (Exception e) {
            throw e;
        } finally {
            if (pw != null) pw.close();
            if (sw != null) sw.close();
        }
        return html;
    }
}
