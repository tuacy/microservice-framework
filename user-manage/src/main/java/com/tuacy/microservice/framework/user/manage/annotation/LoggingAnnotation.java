package com.tuacy.microservice.framework.user.manage.annotation;

import java.lang.annotation.*;

/**
 * 添加在方法上的注解
 * 编写自定义注解。实现对方法所实现的功能进行描述
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LoggingAnnotation {
}
