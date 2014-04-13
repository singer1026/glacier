/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-7-19
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.singleton;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

public class JacksonJsonMapper {
    public static Logger log = LoggerFactory.getLogger(JacksonJsonMapper.class);

    private static class JacksonJsonMapperHolder {
        private static JacksonJsonMapper instance = new JacksonJsonMapper();
    }

    public static JacksonJsonMapper getInstance() {
        return JacksonJsonMapperHolder.instance;
    }

    private static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public String writeValue(Object object) {
        ObjectMapper objectMapper = getObjectMapper();
        StringWriter sw = new StringWriter();
        try {
            objectMapper.writeValue(sw, object);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
        return sw.toString();
    }

    public void writeValue(OutputStream out, Object object) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            objectMapper.writeValue(out, object);
        } catch (Exception e) {
            log.error("catch exception ", e);
        }
    }

    public Object readValue(String s, Class clazz) {
        ObjectMapper objectMapper = getObjectMapper();
        Object value = null;
        try {
            value = objectMapper.readValue(s, clazz);
        } catch (IOException e) {
            log.error("catch exception ", e);
        }
        return value;
    }
}
