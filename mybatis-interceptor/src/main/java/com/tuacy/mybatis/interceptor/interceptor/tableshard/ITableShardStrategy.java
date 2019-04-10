package com.tuacy.mybatis.interceptor.interceptor.tableshard;

import org.apache.ibatis.reflection.MetaObject;

/**
 * 分表策略
 */
public interface ITableShardStrategy {

    /**
     * 分表策略
     */
    String tableShard(MetaObject metaStatementHandler, String tableName, String[] shardParamKey) throws Exception;

}
