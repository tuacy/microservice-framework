package com.tuacy.microservice.framework.quartz.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan(basePackages = "com.tuacy.microservice.framework.quartz.manage.mapper")
@EnableFeignClients
@EnableHystrix
public class FrameworkQuartzManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameworkQuartzManageApplication.class, args);
    }

}
