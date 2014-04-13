package com.qinyin.athene.sql;

import java.util.Map;

/**
 * 功能说明: 通用模板接口<br>
 * 系统版本: v1.0.0<br>
 * 开发人员: 赵烈 <br>
 * 开发时间: 2011-4-19<br>
 * 功能描述: <br>
 */
public interface SqlTemplate {
    /**
     * 把传入的数据和模板进行合并，然后输出结果
     *
     * @param param 条件参数map
     * @return 模板引擎生成的字符串
     */
    public String merge(Map<String, Object> param);
}
