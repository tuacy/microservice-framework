package com.tuacy.mybatis.interceptor.interceptor.page;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import javax.xml.bind.PropertyException;
import java.sql.SQLException;
import java.util.*;

/**
 * Mybatis - 分页拦截器
 */
@Intercepts({
        @Signature
                (
                        type = Executor.class,
                        method = "query",
                        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
                ),
        @Signature
                (
                        type = Executor.class,
                        method = "query",
                        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
                ),
})
public class PageInterceptor implements Interceptor {

    public static final String DATABASE_TYPE_MYSQL = "mysql";
    public static final String DATABASE_TYPE_ORACLE = "oracle";

    /**
     * 数据库类型,目前值支持两种：mysql、oracle
     */
    public static final String PROPERTIES_KEY_DATABASE_TYPE = "database_type";
    /**
     * mybatis数据库xml映射文件里面如果方法名里面出现了下面的字段就进行拦截，做分页处理
     * 正则匹配
     */
    public static final String PROPERTIES_KEY_PAGE_EXPRESSION_MATCHING = "page_flag";
    /**
     * 当需要写手动count查询是，方法对应的后缀名
     */
    public static final String PROPERTIES_KEY_COUNT_SUFFIX = "count_suffix";

    /**
     * 我们自己去组装count查询的时候 mappedStatement id的后缀
     */
    private final static String AUTO_COUNT_MAPPED_STATEMENT_SUFFIX = "_AUTO_COUNT";

    private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList<>(0);
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    /**
     * 数据库类型
     */
    private String databaseType;
    /**
     * mybatis数据库xml映射文件里面方法包含哪些字段的做分页处理，比如pageExpressionMatching = .*Page.*
     * 我们并不会去对所有的方法做分页拦截处理，只有方法名里面包含Page的都会尝试去做分页处理
     */
    private String pageExpressionMatching;
    /**
     * 自定义count查询的后缀 -- 可以手动去设置count查询
     */
    private String countSuffix;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!isEmpty(databaseType) && !isEmpty(pageExpressionMatching)) {
            try {
                Executor executor = (Executor) invocation.getTarget();
                Object[] args = invocation.getArgs();
                MappedStatement mappedStatement = (MappedStatement) args[0];
                Object parameter = args[1];
                ResultHandler resultHandler = (ResultHandler) args[3];
                // 对应的方法是否添加了分页拦截标志 -- PROPERTIES_KEY_PAGE_EXPRESSION_MATCHING
                if (mappedStatement.getId().matches(pageExpressionMatching)) {
                    BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                    String sql = boundSql.getSql();
                    // 获取mapper里面方法上的参数
                    Object parameterObject = boundSql.getParameterObject();
                    if (parameterObject != null) {
                        // 获取分页参数对象信息,只有在获取到了分页参数的基础上才可以进行分页处理
                        PageView pageView = getPageInfoParameter(parameterObject);
                        if (pageView != null) {
                            // 1. 获取总条数，需要判断是否需要去获取总数
                            if (pageView.isDoCount()) {
                                Long totalCount = getCount(executor, mappedStatement, parameter, resultHandler);
                                if (totalCount != null) {
                                    pageView.setTotalCount(totalCount);
                                }
                            }
                            // 2. 分页查询对应的sql
                            String pageSql = buildPageSql(sql, pageView);
                            // 将分页sql语句反射回BoundSql里面去
                            resetSql2Invocation(invocation, pageSql);
                        }
                    }
                }
            } catch (Exception e) {
                // 任何一个地方有异常，都去执行原始操作
                // ignore
            }
        }


        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return (target instanceof Executor) ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
        databaseType = properties.getProperty(PROPERTIES_KEY_DATABASE_TYPE);
        if (isEmpty(databaseType)) {
            try {
                throw new PropertyException("请设置数据库类型(mysql、oracle)");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }
        pageExpressionMatching = properties.getProperty(PROPERTIES_KEY_PAGE_EXPRESSION_MATCHING);//根据id来区分是否需要分页
        if (isEmpty(pageExpressionMatching)) {
            try {
                throw new PropertyException("请设置需要进行page处理的方法的，正则匹配表达式");
            } catch (PropertyException e) {
                e.printStackTrace();
            }
        }
        countSuffix = properties.getProperty(PROPERTIES_KEY_COUNT_SUFFIX);//当需要写手动count查询是，方法对应的后缀名

    }

    /**
     * 从mapper里面方法上的参数里面获取PageView参数对象
     *
     * @param parameterObject mapper里面方法上的参数
     * @return PageView信息
     */
    @SuppressWarnings("unchecked")
    private PageView getPageInfoParameter(Object parameterObject) {
        PageView pageView = null;
        try {
            if (parameterObject instanceof PageView) {
                // 参数就是PageView实体
                pageView = (PageView) parameterObject;
            } else if (parameterObject instanceof Map) {
                // 参数是一个map，遍历各个参数
                for (Map.Entry entry : (Set<Map.Entry>) ((Map) parameterObject).entrySet()) {
                    if (entry.getValue() instanceof PageView) {
                        pageView = (PageView) entry.getValue();
                        break;
                    }
                }

            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return pageView;
    }


    /**
     * 获取满足添加状态下总的条数
     *
     * @param executor        Executor
     * @param mappedStatement MappedStatement
     * @param parameter       Object
     * @param resultHandler   ResultHandler
     * @return 总条数
     * @throws SQLException 异常
     */
    private Long getCount(Executor executor, MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler)
            throws SQLException {
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Object parameterObject = boundSql.getParameterObject();
        Long totalCount;
        //先判断是否存在手写的count查询
        if (isEmpty(countSuffix)) {
            // 没有配置自定义count查询的后缀,说明没有自定义的count查询了
            totalCount = executeAutoTotalCount(executor, mappedStatement, boundSql, parameterObject, resultHandler);
        } else {
            // 尝试去查找手动设置的count查询对应的方法
            String manualCountMappedStatementId = mappedStatement.getId() + countSuffix;
            MappedStatement manualCountMappedStatement = getExistedMappedStatement(mappedStatement.getConfiguration(), manualCountMappedStatementId);
            if (manualCountMappedStatement == null) {
                // mapper里面没有找到自定义的count查询
                totalCount = executeAutoTotalCount(executor, mappedStatement, boundSql, parameterObject, resultHandler);
            } else {
                totalCount = executeManualTotalCount(executor, manualCountMappedStatement, parameter, boundSql, resultHandler);
            }
        }
        return totalCount;
    }

    /**
     * 从数据库里查询总的记录数，自动去拼装sql
     *
     * @param executor        Executor
     * @param mappedStatement mappedStatement
     * @param boundSql        BoundSql
     * @param parameterObject 参数列表
     * @param resultHandler   ResultHandler
     * @return count
     * @throws SQLException 异常
     */
    private Long executeAutoTotalCount(Executor executor, MappedStatement mappedStatement,
                                       BoundSql boundSql, Object parameterObject, ResultHandler resultHandler) throws SQLException {

        // 自己构造一个count查询对应的MappedStatement
        String autoCountMappedStatementId = mappedStatement.getId() + AUTO_COUNT_MAPPED_STATEMENT_SUFFIX;
        MappedStatement countMappedStatement = generalCountMappedStatement(mappedStatement, autoCountMappedStatementId);

        String sql = boundSql.getSql(); // 原始sql
        String countSql = "select count(1) from (" + sql + ") tmp_count"; //count查询对应的sql语句
        //创建 count 查询的缓存 key
        CacheKey countKey = executor.createCacheKey(countMappedStatement, parameterObject, RowBounds.DEFAULT, boundSql);
        BoundSql countBoundSql = new BoundSql(countMappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
        //执行 count 查询
        Object countResultList = executor.query(countMappedStatement, parameterObject, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        return (Long) ((List) countResultList).get(0);
    }

    /**
     * 根据数据库类型，生成特定的分页sql
     *
     * @param sql  原始的sql
     * @param page 分页信息
     * @return 分页sql
     */
    private String buildPageSql(String sql, PageView page) {
        if (page != null) {
            if (DATABASE_TYPE_MYSQL.equals(databaseType)) {
                return buildMysqlPageSql(sql, page).toString();
            } else if (DATABASE_TYPE_ORACLE.equals(databaseType)) {
                return buildOraclePageSql(sql, page).toString();
            }
        }
        return sql;
    }

    /**
     * mysql分页语句
     *
     * @param sql  原始sql语句
     * @param page 分页信息
     * @return page包装之后的sql
     */
    private StringBuilder buildMysqlPageSql(String sql, PageView page) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginRow = String.valueOf((page.getPageNo()) * page.getPageSize());
        pageSql.append(sql);
        pageSql.append(" limit ").append(beginRow).append(",").append(page.getPageSize());
        return pageSql;
    }

    /**
     * 参考hibernate的实现完成oracle的分页
     *
     * @param sql  原始sql语句
     * @param page 分页信息
     * @return page包装之后的sql
     */
    private StringBuilder buildOraclePageSql(String sql, PageView page) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginRow = String.valueOf(page.getPageNo() * page.getPageSize());
        String endRow = String.valueOf(page.getPageNo() * page.getPageSize());

        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp where rownum <= ").append(endRow);
        pageSql.append(") where row_id > ").append(beginRow);
        return pageSql;
    }

    /**
     * 尝试获取已经存在的在 MS，提供对手写count和page的支持
     *
     * @param configuration          Configuration
     * @param countMappedStatementId count查询对应的方法id
     * @return MappedStatement
     */
    private MappedStatement getExistedMappedStatement(Configuration configuration, String countMappedStatementId) {
        MappedStatement mappedStatement = null;
        try {
            mappedStatement = configuration.getMappedStatement(countMappedStatementId, false);
        } catch (Throwable t) {
            //ignore
        }
        return mappedStatement;
    }

    /**
     * 执行手动设置的 count 查询，该查询支持的参数必须和被分页的方法相同
     *
     * @param executor        Executor
     * @param mappedStatement MappedStatement
     * @param parameter       parameter
     * @param boundSql        boundSql
     * @param resultHandler   resultHandler
     * @return count
     * @throws SQLException 异常
     */
    private Long executeManualTotalCount(Executor executor, MappedStatement mappedStatement,
                                         Object parameter, BoundSql boundSql,
                                         ResultHandler resultHandler) throws SQLException {
        CacheKey countKey = executor.createCacheKey(mappedStatement, parameter, RowBounds.DEFAULT, boundSql);
        BoundSql countBoundSql = mappedStatement.getBoundSql(parameter);
        Object countResultList = executor.query(mappedStatement, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        return ((Number) ((List) countResultList).get(0)).longValue();
    }

    /**
     * 将sql反射回方法中
     *
     * @param invocation Invocation
     * @param sql        sql
     */
    private void resetSql2Invocation(Invocation invocation, String sql) {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        MappedStatement newMappedStatement = generalMappedStatement(statement, new BoundSqlSqlSource(boundSql));
        // MetaObject mybatis里面提供的一个工具类，类似反射的效果
        MetaObject msObject = MetaObject.forObject(newMappedStatement, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
        msObject.setValue("sqlSource.boundSql.sql", sql);
        args[0] = newMappedStatement;
    }

    private class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    /**
     * 创建新的 MappedStatement
     *
     * @param mappedStatement MappedStatement
     * @param sqlSource       SqlSource
     * @return MappedStatement
     */
    private MappedStatement generalMappedStatement(MappedStatement mappedStatement, SqlSource sqlSource) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), sqlSource, mappedStatement.getSqlCommandType());
        builder.resource(mappedStatement.getResource());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.statementType(mappedStatement.getStatementType());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        if (mappedStatement.getKeyProperties() != null && mappedStatement.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : mappedStatement.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(mappedStatement.getTimeout());
        builder.parameterMap(mappedStatement.getParameterMap());
        builder.resultMaps(mappedStatement.getResultMaps());
        builder.resultSetType(mappedStatement.getResultSetType());
        builder.cache(mappedStatement.getCache());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.useCache(mappedStatement.isUseCache());

        return builder.build();
    }

    /**
     * 新建count查询对应的MappedStatement
     *
     * @param mappedStatement   MappedStatement
     * @param mappedStatementId 方法id，自己取了一个名字
     * @return MappedStatement对象
     */
    private static MappedStatement generalCountMappedStatement(MappedStatement mappedStatement, String mappedStatementId) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatementId, mappedStatement.getSqlSource(), mappedStatement.getSqlCommandType());
        builder.resource(mappedStatement.getResource());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.statementType(mappedStatement.getStatementType());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        if (mappedStatement.getKeyProperties() != null && mappedStatement.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : mappedStatement.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(mappedStatement.getTimeout());
        builder.parameterMap(mappedStatement.getParameterMap());
        //count查询返回值Long
        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), Long.class, EMPTY_RESULT_MAPPING).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(mappedStatement.getResultSetType());
        builder.cache(mappedStatement.getCache());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.useCache(mappedStatement.isUseCache());

        return builder.build();
    }

    /**
     * 判断字符串变量是否为空
     *
     * @param source 字符串
     * @return 是否为空
     */
    private boolean isEmpty(String source) {
        return null == source || "".equals(source) || "".equals(source.trim()) || "null".equalsIgnoreCase(source);
    }
}
