package com.tuacy.mybatis.interceptor.strategy;

import com.tuacy.mybatis.interceptor.interceptor.tableshard.ITableShardStrategy;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 告警表分表策略 -- 根据告警发生时间来分表,天
 * shardParamKey 对应时间格式 yyyy-MM-dd HH:mm:ss
 */
public class AlarmTableShardStrategy implements ITableShardStrategy {

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    /**
     * 分表策略
     *
     * @param metaStatementHandler MetaObject包装的RoutingStatementHandler对象
     * @param tableName            原始表名
     * @param shardParamKey        可以在mapper文件的方法里面传递一些参数key过来，在分表策略里面通过key获取到对应的值
     * @return 包装之后的sql语句
     */
    @SuppressWarnings("unchecked")
    @Override
    public String tableShard(MetaObject metaStatementHandler, String tableName, String[] shardParamKey) throws Exception {
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        String originSql = boundSql.getSql();
        if (shardParamKey == null || shardParamKey.length == 0) {
            return originSql;
        }
        String alarmOccurTimeParamKey = shardParamKey[0];
        String alarmOccurTime = null;
        Object parameterObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");//获取参数
        if (parameterObject instanceof String) {
            // 参数是一个String,那我们就认为这个String就是用来分表的参数了
            alarmOccurTime = (String) parameterObject;
        } else if (parameterObject instanceof Map) {
            // 参数是一个Map
            Map<String, Object> map = (Map<String, Object>) parameterObject;
            Set<String> set = map.keySet();
            for (String key : set) {
                if (key.equals(alarmOccurTimeParamKey)) {
                    alarmOccurTime = map.get(alarmOccurTimeParamKey).toString();
                    break;
                }
            }
        } else {
            // 参数为某个对象
            MetaObject metaParamObject = MetaObject.forObject(parameterObject, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
            alarmOccurTime = (String) metaParamObject.getValue(alarmOccurTimeParamKey);
        }
        // 确定表名字
        if (alarmOccurTime != null) {
            SimpleDateFormat parseSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date occurDate = parseSdf.parse(alarmOccurTime);
            SimpleDateFormat formatSdf = new SimpleDateFormat("yyyyMMdd");
            String shardTableName = tableName + "_" + formatSdf.format(occurDate);
            return originSql.replaceAll(tableName, shardTableName);
        }
        return originSql;
    }
}
