/**
 * @author zhaolie
 * @version 1.0
 * @create-time 11-5-2
 * @email zhaolie43@gmail.com
 */
package com.qinyin.athene.sql;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

public class CustomColumnMapRowMapper extends ColumnMapRowMapper {
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Object> mapOfColValues = createColumnMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
            Object obj = getColumnValue(rs, key);
            mapOfColValues.put(com.qinyin.athene.util.JdbcUtils.convertColumnName(key), obj);
        }
        return mapOfColValues;
    }

    public Object getColumnValue(ResultSet rs, String key) throws SQLException {
        return com.qinyin.athene.util.JdbcUtils.getResultSetValue(rs, key);
    }

}
