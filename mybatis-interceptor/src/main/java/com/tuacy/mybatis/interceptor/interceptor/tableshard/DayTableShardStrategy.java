package com.tuacy.mybatis.interceptor.interceptor.tableshard;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 按日分表,根据当前时间
 */
public class DayTableShardStrategy implements ITableShardStrategy {

    /**
     * 分表策略
     *
     * @param metaStatementHandler MetaObject包装的RoutingStatementHandler对象
     * @param tableName            原始表名
     * @param shardParamKey        可以在mapper文件的方法里面传递一些参数key过来，在分表策略里面通过key获取到对应的值
     * @return 包装之后的sql语句
     */
    @Override
    public String tableShard(MetaObject metaStatementHandler, String tableName, String[] shardParamKey) {
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        String originSql = boundSql.getSql();
        // 当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String newTableName = tableName + "_" + sdf.format(new Date());
        return originSql.replaceAll(tableName, newTableName);
    }
}
