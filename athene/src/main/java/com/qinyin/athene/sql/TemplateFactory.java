package com.qinyin.athene.sql;


import java.util.Properties;

/**
 * 功能说明: 通用模板工厂接口<br>
 * 系统版本: v1.0.0<br>
 * 开发人员: 赵烈 <br>
 * 开发时间: 2011-4-19<br>
 * 功能描述: <br>
 */
public interface TemplateFactory {
    /**
     * 模板工厂初始化
     *
     * @param properties 初始化参数
     */
    public void init(Properties properties) throws Exception;

    /**
     * 根据name来获得模板，如果找不到模板，则返回null
     *
     * @param name 模板name
     * @return 通用模板接口
     */
    public SqlTemplate getTemplate(String key);
}