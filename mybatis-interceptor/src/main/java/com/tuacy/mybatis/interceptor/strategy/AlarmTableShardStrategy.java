package com.tuacy.mybatis.interceptor.strategy;

import com.tuacy.mybatis.interceptor.interceptor.tableshard.ITableShardStrategy;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * 告警表分表策略
 */
public class AlarmTableShardStrategy implements ITableShardStrategy {

    @Override
    public String tableShard(MetaObject metaStatementHandler, String tableName, String[] shardParamKey) throws Exception {
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        String originSql = boundSql.getSql();
        boundSql.getParameterMappings();
        String userCode = shardParamKey[0];
        Object parameterObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");//获取参数
        if (parameterObject instanceof String) {
            // 参数是一个String
            originSql = originSql.replaceAll(tableName, tableName + "_" + parameterObject);
        } else if (parameterObject instanceof Map) {
            // 参数是一个Map
            Map<String, Object> map = (Map<String, Object>) parameterObject;
            Set<String> set = map.keySet();
            String value = "";
            for (String key : set) {
                if (key.equals(userCode)) {
                    value = map.get(userCode).toString();
                    break;
                }
            }
            originSql = originSql.replaceAll(tableName, tableName + "_" + value);
        } else {
            // 参数为某个对象
            Class<?> clazz = parameterObject.getClass();
            String value = "";
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (fieldName.equals(userCode)) {
                    value = field.get(parameterObject).toString();
                    break;
                }
            }
            originSql = originSql.replaceAll(tableName, tableName + "_" + value);
        }
        return originSql;
    }
}
