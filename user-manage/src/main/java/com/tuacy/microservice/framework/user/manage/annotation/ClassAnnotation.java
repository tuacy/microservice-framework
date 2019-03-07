package com.tuacy.microservice.framework.user.manage.annotation;

import java.lang.annotation.*;

/**
 * 添加在类上的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ClassAnnotation {
    public String value() default "";
}
