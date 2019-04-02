package com.tuacy.mybatis.interceptor.config;

import com.tuacy.mybatis.interceptor.interceptor.LogInterceptor;
import com.tuacy.mybatis.interceptor.interceptor.PageInterceptor;
import com.tuacy.mybatis.interceptor.interceptor.TableSplitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis配置
 */
@Configuration
public class MybatisConfiguration {

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

    @Bean
    public TableSplitInterceptor tableSplitInterceptor() {
        return new TableSplitInterceptor();
    }

    @Bean
    public LogInterceptor logInterceptor() {
        LogInterceptor interceptor = new LogInterceptor();
        Properties properties = new Properties();
        // 设置是否显示数据库执行语句的执行时间
        properties.setProperty(LogInterceptor.PROPERTIES_KEY_ENABLE_EXECUTOR_TIME, LogInterceptor.ENABLE_EXECUTOR_TIME);
        interceptor.setProperties(properties);
        return interceptor;
    }

}
