package com.tuacy.mybatis.interceptor.interceptor.encryptresultfield;

import java.lang.annotation.*;

/**
 * 通过注解来表明，我们需要对那个字段进行加密
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EncryptResultFieldAnnotation {

    /**
     * 加密策略 -- 和加密字段，一一对应
     */
    Class<? extends IEncryptResultFieldStrategy>[] encryptStrategy() default {};

    /**
     * 加密字段对应的key
     */
    String[] fieldKey() default {};

}
