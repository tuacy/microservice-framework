package com.tuacy.mybatis.interceptor.interceptor.encryptresultfield;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 通过拦截器对返回结果中的某个字段进行加密处理
 */

@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class}
        )
})
public class EncryptResultFieldInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement = (Statement) invocation.getArgs()[0];
        ResultSet resultSet = statement.getResultSet();
        while (resultSet != null && resultSet.next()) {
            try {
                String name = resultSet.getString("userName");
                if (name != null) {
                    resultSet.updateString("userName", name + ":update");
//                    resultSet.updateRow();
                }
                int a = 10;
            } catch (SQLException e) {
                //ignore
            }
        }
        if (resultSet != null) {
            resultSet.beforeFirst();
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
