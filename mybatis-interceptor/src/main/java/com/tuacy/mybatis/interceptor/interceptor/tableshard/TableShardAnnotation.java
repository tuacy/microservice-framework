package com.tuacy.mybatis.interceptor.interceptor.tableshard;

import java.lang.annotation.*;

/**
 * 分表注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TableShardAnnotation {
    /**
     * 待分表的表名
     */
    String tableName();

    /**
     * 分表策略
     */
    Class<? extends ITableShardStrategy> shadeStrategy();

    /**
     * 分表条件key, 通过key去参数列表里取对应的值，作为分表条件处理
     */
    String[] shardParamKey() default {};
}
