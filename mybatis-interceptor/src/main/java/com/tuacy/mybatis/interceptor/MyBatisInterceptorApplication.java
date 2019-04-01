package com.tuacy.mybatis.interceptor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan(basePackages = "com.tuacy.mybatis.interceptor.mapper")
//@EnableFeignClients
//@EnableHystrix
public class MyBatisInterceptorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBatisInterceptorApplication.class, args);
    }


}
