package com.tuacy.mybatis.interceptor.interceptor.tableshard;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.util.Properties;

/**
 * mybatis分表拦截器 -- 水平切分
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
public class TableShardInterceptor implements Interceptor {

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof RoutingStatementHandler) {
            try {
                RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
                // MetaObject是mybatis里面提供的一个工具类，类似反射的效果
                MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
                BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");//获取sql语句
                String originSql = boundSql.getSql();
                if (!StringUtils.isEmpty(originSql)) {
                    MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
                    String id = mappedStatement.getId();
                    String className = id.substring(0, id.lastIndexOf("."));
                    Class<?> clazz = Class.forName(className);
                    // 判断方法上是否添加了 TableShard 注解，因为只有添加了TableShard注解的方法我们才会去做分表处理
                    TableShard tableShard = clazz.getAnnotation(TableShard.class);
                    if (tableShard != null) {
                        String tableName = tableShard.tableName();
                        Class<?> strategyClazz = tableShard.shadeStrategy();
                        String[] shardParamKey = tableShard.shardParamKey();
                        ITableShardStrategy tableStrategy = (ITableShardStrategy) strategyClazz.newInstance();
                        String newSql = tableStrategy.tableShard(metaStatementHandler, tableName, shardParamKey);
                        // 把新语句设置回去
                        metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
                    }

                }
            } catch (Exception e) {
                // ignore 任何一个地方有异常都去执行原始操作 -- invocation.proceed()
            }

        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        return (target instanceof RoutingStatementHandler) ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
