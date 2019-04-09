package com.tuacy.mybatis.interceptor.interceptor;

import com.tuacy.mybatis.interceptor.interceptor.utils.ReflectHelper;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import javax.xml.bind.PropertyException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Mybatis - 分页拦截器
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class PageInterceptor implements Interceptor {

    /**
     * 先在mapper里面方法上的参数,上查找是否有PageView类型的参数，如果没有，在去查找pageNo，pageSize参数。多重保证
     */
    private final static String PAGE_NO = "pageNo";
    private final static String PAGE_SIZE = "pageSize";

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
    public static final String PROPERTIES_KEY_COUNT_SUFFIX = "_COUNT";

    /**
     * 数据库类型
     */
    private String databaseType;
    /**
     * mybatis数据库xml映射文件里面方法包含哪些字段的做分页处理，比如pageExpressionMatching = .*Page.*
     * 那么所有方法名里面包含Page的都会尝试去做分页处理
     */
    private String pageExpressionMatching;
    /**
     * 自定义count查询的后缀
     */
    private String countSuffix;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Object[] args = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) args[0];
            Object parameter = args[1];
            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            // 对应的方法是否添加了分页拦截标志 -- PROPERTIES_KEY_PAGE_EXPRESSION_MATCHING
            if (mappedStatement.getId().matches(pageExpressionMatching)) {
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                // 获取mapper里面方法上的参数
                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject != null) {
                    // 获取分页参数对象
                    PageView pageView = getPageInfoFromParameter(parameterObject);
                    if (pageView != null) {
                        String sql = boundSql.getSql();
                        // 获取总条数
                        if (pageView.isDoCount()) {
                            Long totalCount;
                            //先判断是否存在手写的 count 查询
                            if (isEmpty(countSuffix)) {
                                totalCount = executeAutoTotalCount(executor, sql, mappedStatement, boundSql, parameterObject, resultHandler);
                            } else {
                                String countMappedStatementId = mappedStatement.getId() + countSuffix;
                                MappedStatement countMappedStatement = getExistedMappedStatement(mappedStatement.getConfiguration(), countMappedStatementId);
                                if (countMappedStatement == null) {
                                    // 自动做分页处理
                                    totalCount = executeAutoTotalCount(executor, sql, mappedStatement, boundSql, parameterObject, resultHandler);
                                } else {
                                    totalCount = executeManualTotalCount(executor, countMappedStatement, parameter, boundSql, resultHandler);
                                }
                            }
                            if (totalCount != null) {
                                pageView.setTotalCount(totalCount);
                            }
                        }
                        // 分页查询对应的sql
                        String pageSql = buildPageSql(sql, pageView);
                        // 将分页sql语句反射回BoundSql里面去
                        ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql);
                    }
                }
            }
        } catch (Exception e) {
            // 任何一个地方有异常，都去执行原始操作
            return invocation.proceed();
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
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
    private PageView getPageInfoFromParameter(Object parameterObject) {
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
                    } else if (PAGE_NO.equals(entry.getKey().toString())) {
                        if (pageView == null) {
                            pageView = new PageView();
                        }
                        pageView.setPageNo((Integer) entry.getValue());
                    } else if (PAGE_SIZE.equals(entry.getKey().toString())) {
                        if (pageView == null) {
                            pageView = new PageView();
                        }
                        pageView.setPageSize((Integer) entry.getValue());
                    }

                    // 判断存在分页条件是否已经满足
                    if (pageView != null && pageView.getPageNo() > 0 && pageView.getPageSize() > 0) {
                        break;
                    }
                }

            } else {
                // 参数为某个对象，然后去对象里面查找PageView类型对应的属性
                pageView = ReflectHelper.getValueByFieldType(parameterObject, PageView.class);
                // 没有PageView属性，检测是否存在pageNo和pageSize属性
                if (pageView == null) {
                    Object pageNoObj = ReflectHelper.getValueByFieldName(parameterObject, PAGE_NO);
                    Object pageSizeObj = ReflectHelper.getValueByFieldName(parameterObject, PAGE_SIZE);

                    if (pageNoObj != null && pageSizeObj != null) {
                        pageView = new PageView();
                        pageView.setPageNo((Integer) pageNoObj);
                        pageView.setPageSize((Integer) pageSizeObj);
                    }
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return pageView;
    }

    /**
     * 从数据库里查询总的记录数，自动去拼装sql
     *
     * @param sql             原始的sql语句
     * @param mappedStatement mappedStatement
     * @param boundSql        BoundSql
     * @param parameterObject 参数列表
     * @return count
     * @throws SQLException 异常
     */
    private Long executeAutoTotalCount(Executor executor, String sql, MappedStatement mappedStatement,
                                       BoundSql boundSql, Object parameterObject, ResultHandler resultHandler) throws SQLException {
        // 获取总数对应的sql,临时表查询
        String countSql = "select count(1) from (" + sql + ") tmp_count";
        //创建 count 查询的缓存 key
        CacheKey countKey = executor.createCacheKey(mappedStatement, parameterObject, RowBounds.DEFAULT, boundSql);
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
        //执行 count 查询
        Object countResultList = executor.query(mappedStatement, parameterObject, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
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
        String beginRow = String.valueOf((page.getPageNo() - 1) * page.getPageSize());
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
        String beginRow = String.valueOf((page.getPageNo() - 1) * page.getPageSize());
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
     * 判断字符串变量是否为空
     *
     * @param source 字符串
     * @return 是否为空
     */
    private boolean isEmpty(String source) {
        return null == source || "".equals(source) || "".equals(source.trim()) || "null".equalsIgnoreCase(source);
    }
}
