/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-6-25
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.freemarker.tag;

import com.qinyin.athene.util.BeanUtils;
import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Widget extends BaseTag {
    protected String getBinding(Map params) {
        return getString(params, "binding");
    }

    protected String getName(Map params) {
        String name = getString(params, "name");
        //如果定义name，则返回name
        if (StringUtils.isNotBlank(name)) return name;
        String binding = getBinding(params);
        //如果binding为空，则返回null
        if (StringUtils.isBlank(binding)) return null;
        //如果binding不包含"."，则返回bingding
        if (!binding.contains(".")) return binding;
        String[] bindings = binding.split("\\.");
        return bindings[bindings.length - 1];
    }

    protected String getNumber(Environment env) throws TemplateModelException {
        SimpleScalar _number = (SimpleScalar) env.getVariable("number");
        if (_number != null && StringUtils.isNotBlank(_number.getAsString())) {
            return _number.getAsString();
        } else {
            return null;
        }
    }

    protected String getId(Environment env, String name) throws TemplateModelException {
        if (StringUtils.isBlank(name)) return null;
        String number = getNumber(env);
        return convertId(name, number);
    }

    protected String convertId(String name, String number) {
        if (StringUtils.isBlank(name)) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (i == 0) {
                sb.append(Character.toLowerCase(name.charAt(i)));
                continue;
            }
            if (Character.isUpperCase(name.charAt(i))) {
                sb.append("-").append(Character.toLowerCase(name.charAt(i)));
            } else {
                sb.append(name.charAt(i));
            }
        }
        sb.append("-").append(number);
        return sb.toString();
    }

    protected Object getValue(Environment env, Map params) throws TemplateModelException {
        String binding = getBinding(params);
        if (StringUtils.isBlank(binding)) return null;
        if (!binding.contains(".")) return env.getVariable(binding);
        String[] bindings = binding.split("\\.");
        Object object = env.getVariable(bindings[0]);
        if (object == null) return null;
        if (object instanceof SimpleHash) {
            SimpleHash hash = (SimpleHash) object;
            return hash.get(bindings[1]);
        } else if (object instanceof StringModel) {
            StringModel model = (StringModel) object;
            return model.get(bindings[1]);
        } else {
            return null;
        }
    }

    protected List<Map<String, Object>> getList(Map params, String key) throws TemplateModelException {
        List list = super.getList(params, key);
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        if (list != null) {
            for (Object obj : list) {
                rtnList.add(BeanUtils.toMap(obj));
            }
        }
        return rtnList;
    }

    protected List<Map<String, Object>> getListValue(Environment env, Map params) throws TemplateModelException {
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        String binding = getBinding(params);
        if (StringUtils.isBlank(binding)) return rtnList;
        if (!binding.contains(".")) {
            SimpleSequence _value = (SimpleSequence) env.getVariable(binding);
            if (_value != null) {
                List list = _value.toList();
                for (Object obj : list) {
                    rtnList.add(BeanUtils.toMap(obj));
                }
            }
            return rtnList;
        }
        String[] bindings = binding.split("\\.");
        Object object = env.getVariable(bindings[0]);
        if (object == null) return rtnList;
        SimpleHash hash = (SimpleHash) object;
        SimpleSequence _value = (SimpleSequence) hash.get(bindings[1]);
        if (_value != null) {
            List list = _value.toList();
            for (Object obj : list) {
                rtnList.add(BeanUtils.toMap(obj));
            }
        }
        return rtnList;
    }
}
