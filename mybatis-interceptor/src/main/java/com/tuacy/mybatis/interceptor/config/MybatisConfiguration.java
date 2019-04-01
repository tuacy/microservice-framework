package com.tuacy.mybatis.interceptor.config;

import com.tuacy.mybatis.interceptor.interceptor.MyBatisPageInterceptor;
import com.tuacy.mybatis.interceptor.interceptor.MyBatisTableSplitInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis配置
 */
@Configuration
public class MybatisConfiguration {

    @Bean
    public MyBatisPageInterceptor batisPageInterceptor() {
        return new MyBatisPageInterceptor();
    }

    @Bean
    public MyBatisTableSplitInterceptor batisTableSplitInterceptor() {
        return new MyBatisTableSplitInterceptor();
    }

}
