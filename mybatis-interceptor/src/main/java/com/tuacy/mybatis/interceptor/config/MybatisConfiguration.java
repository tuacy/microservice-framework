package com.tuacy.mybatis.interceptor.config;

import com.tuacy.mybatis.interceptor.interceptor.PageInterceptor;
import com.tuacy.mybatis.interceptor.interceptor.TableSplitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
