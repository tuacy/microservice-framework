package com.tuacy.mybatis.interceptor.interceptor.log;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


/**
 * 1. 打印mysql完整的执行语句
 * 2. 打印mysql语句执行时间
 * 这里我们拦截Executor里面的query和update方法
 */
@Intercepts({
        @Signature(
                method = "query",
                type = Executor.class,
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class LogInterceptor implements Interceptor {

    /**
     * 是否显示语句的执行时间
     */
    public static final String PROPERTIES_KEY_ENABLE_EXECUTOR_TIME = "enableExecutorTIme";
    public static final String ENABLE_EXECUTOR_TIME = "0"; // 显示

    private boolean enableExecutorTime = false;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取执行方法的MappedStatement参数,不管是Executor的query方法还是update方法，第一个参数都是MappedStatement
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        long sqlStartTime = System.currentTimeMillis();
        Object re = invocation.proceed();
        long sqlEndTime = System.currentTimeMillis();
        // 打印mysql执行语句
        String sql = getSql(configuration, boundSql, sqlId);
        System.out.println(sql);
        // 打印mysql执行时间
        if (enableExecutorTime) {
            String sqlTimeLog = sqlId + " 方法对应sql执行时间:" + (sqlEndTime - sqlStartTime) + " ms";
            System.out.println(sqlTimeLog);
        }
        return re;
    }

    /**
     * 通过该方法决定要返回的对象是目标对象还是对应的代理
     * 不要想的太复杂，一般就两种情况：
     * <p>
     * 1. return target;  直接返回目标对象，相当于当前Interceptor没起作用，不会调用上面的intercept()方法
     * 2. return Plugin.wrap(target, this);  返回代理对象，会调用上面的intercept()方法
     *
     * @param target 目标对象
     * @return 目标对象或者代理对象
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 用于获取在Configuration初始化当前的Interceptor时时候设置的一些参数
     *
     * @param properties Properties参数
     */
    @Override
    public void setProperties(Properties properties) {
        if (properties != null) {
            String executorTImeValue = properties.getProperty(PROPERTIES_KEY_ENABLE_EXECUTOR_TIME);
            if (executorTImeValue != null) {
                enableExecutorTime = executorTImeValue.equals(ENABLE_EXECUTOR_TIME);
            }
        }
    }

    private static String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
        return sqlId + " 方法对应sql执行语句:" + assembleSql(configuration, boundSql);
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{}
     * \\需要第一个替换，否则replace方法替换时会有逻辑bug
     */
    private static String makeQueryStringAllRegExp(String str) {
        if (str != null && !str.equals("")) {
            return str.replace("\\", "\\\\").replace("*", "\\*")
                    .replace("+", "\\+").replace("|", "\\|")
                    .replace("{", "\\{").replace("}", "\\}")
                    .replace("(", "\\(").replace(")", "\\)")
                    .replace("^", "\\^").replace("$", "\\$")
                    .replace("[", "\\[").replace("]", "\\]")
                    .replace("?", "\\?").replace(",", "\\,")
                    .replace(".", "\\.").replace("&", "\\&");
        }
        return str;
    }

    /**
     * 获取参数对应的string值
     *
     * @param obj 参数对应的值
     * @return string
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        // 对特殊字符进行转义，方便之后处理替换
        return value != null ? makeQueryStringAllRegExp(value) : value;
    }

    /**
     * 组装完整的sql语句 -- 把对应的参数都代入到sql语句里面
     *
     * @param configuration Configuration
     * @param boundSql      BoundSql
     * @return sql完整语句
     */
    private static String assembleSql(Configuration configuration, BoundSql boundSql) {
        // 获取mapper里面方法上的参数
        Object sqlParameter = boundSql.getParameterObject();
        // sql语句里面需要的参数 -- 真实需要用到的参数 因为sqlParameter里面的每个参数不一定都会用到
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql原始语句(?还没有替换成我们具体的参数)
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && sqlParameter != null) {
            // sql语句里面的?替换成真实的参数
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(sqlParameter.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(sqlParameter));
            } else {
                MetaObject metaObject = configuration.newMetaObject(sqlParameter);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    // 一个一个把对应的值替换进去 按顺序把?替换成对应的值
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }
}
