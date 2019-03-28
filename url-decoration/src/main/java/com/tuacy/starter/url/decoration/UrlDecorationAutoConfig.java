package com.tuacy.starter.url.decoration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 实现自动化配置
 */
@Configuration//开启配置
@EnableConfigurationProperties(UrlDecorationServiceProperties.class) //开启使用映射实体对象
@ConditionalOnClass(UrlDecorationService.class) //存在UrlDecorationService时初始化该配置类
@ConditionalOnProperty //存在对应配置信息时初始化该配置类
        (
                prefix = "url",//存在配置前缀hello
                value = "enabled",//开启
                matchIfMissing = true//缺失检查
        )
public class UrlDecorationAutoConfig {


    private UrlDecorationServiceProperties properties;

    @Autowired
    public void setProperties(UrlDecorationServiceProperties properties) {
        this.properties = properties;
    }

    /**
     * 根据条件判断不存在UrlDecorationService时初始化新bean到SpringIoc
     */
    @Bean //创建UrlDecorationService实体bean
    @ConditionalOnMissingBean(UrlDecorationService.class) //缺失UrlDecorationService实体bean时，初始化UrlDecorationService并添加到SpringIoc
    UrlDecorationService urlDecorationService() {
        return new UrlDecorationService(properties);
    }

}
