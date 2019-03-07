package com.tuacy.microservice.framework.user.manage.aspect;

import com.tuacy.microservice.framework.user.manage.api.request.UserParam;
import com.tuacy.microservice.framework.user.manage.controller.UserController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 日志AOP 切面类
 */

@Aspect
@Component("logAspect")
public class LoggingAspect {

    /**
     * 匹配com.tuacy.microservice.framework.user.manage.controller包下面直接子类所有方法。
     */
    @Pointcut("execution(* com.tuacy.microservice.framework.user.manage.controller.*.*(..))")
    public void operateLog() {
    }

    /**
     * 匹配com.tuacy.microservice.framework.user.manage.controller包下直接子类所有方法。
     */
//    @Pointcut("within(com.tuacy.microservice.framework.user.manage.controller.*)")
//    public void operateLog() {
//    }

    /**
     * 匹配所有添加了LogginAnnotation注解的方法
     */
//    @Pointcut("@within(com.tuacy.microservice.framework.user.manage.annotation.LoggingAnnotation)")
//    public void operateLog() {
//    }

    /**
     * 匹配所有添加了LogginAnnotation注解的方法(TODO: 测试发现只有后置通知)
     */
//    @Pointcut("within(@com.tuacy.microservice.framework.user.manage.annotation.LoggingAnnotation *)")
//    public void operateLog() {
//    }

    /**
     * 匹配所有实现了IUserControllerApi接口类的所有方法
     */
//    @Pointcut("this(com.tuacy.microservice.framework.user.manage.api.api.IUserControllerApi)")
//    public void operateLog() {
//    }

    /**
     * 匹配UserController类里面所有的方法
     */
//    @Pointcut("this(com.tuacy.microservice.framework.user.manage.controller.UserController)")
//    public void operateLog() {
//    }

    /**
     * 匹配所有实现了IUserControllerApi接口类的所有方法
     */
//    @Pointcut("target(com.tuacy.microservice.framework.user.manage.api.api.IUserControllerApi)")
//    public void operateLog() {
//    }

    /**
     * 匹配所有类添加了ClassAnnotation注解的类的所有方法
     */
//    @Pointcut("@within(com.tuacy.microservice.framework.user.manage.annotation.ClassAnnotation)")
//    public void operateLog() {
//    }

    /**
     * 匹配所有类添加了ClassAnnotation注解的类的所有方法 -- TODO: 进过测试直接就编译不过
     */
//    @Pointcut("@target(com.tuacy.microservice.framework.user.manage.annotation.ClassAnnotation)")
//    public void operateLog() {
//    }

    /**
     * 匹配所有添加了LoggingAnnotation注解的方法
     */
//    @Pointcut("@annotation(com.tuacy.microservice.framework.user.manage.annotation.LoggingAnnotation)")
//    public void operateLog() {
//    }

    /**
     * 前置通知：目标方法执行之前执行以下方法体的内容
     */
    @Before(value = "operateLog() && args(param)")
    public void beforeMethod(JoinPoint jp, UserParam param) {
        System.out.println("aaaaaaaaaa");
        System.out.println(param.toString());
        String methodName = jp.getSignature().getName();
        System.out.println("【前置通知】the method 【" + methodName + "】");
    }

    /**
     * 返回通知：目标方法正常执行完毕时执行以下代码
     */
    @AfterReturning(value = "operateLog()", returning = "result")
    public void afterReturningMethod(JoinPoint jp, Object result) {
        String methodName = jp.getSignature().getName();
        System.out.println("【返回通知】the method 【" + methodName + "】 ends with 【" + result + "】");
    }

    /**
     * 后置通知：目标方法执行之后执行以下方法体的内容，不管是否发生异常。
     */
    @After(value = "operateLog()")
    public void afterMethod(JoinPoint jp) {
        System.out.println("【后置通知】this is a afterMethod advice...");
    }

    /**
     * 异常通知：目标方法发生异常的时候执行以下代码
     */
    @AfterThrowing(value = "operateLog()", throwing = "e")
    public void afterThorwingMethod(JoinPoint jp, NullPointerException e) {
        String methodName = jp.getSignature().getName();
        System.out.println("【异常通知】the method 【" + methodName + "】 occurs exception: " + e);
    }

    /**
     * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
     */
//    @Around(value = "operateLog()")
//    public Object aroundMethod(ProceedingJoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//        Object result = null;
//        try {
//            System.out.println("【环绕通知中的--->前置通知】：the method 【" + methodName + "】");
//            //执行目标方法
//            result = jp.proceed();
//            System.out.println("【环绕通知中的--->返回通知】：the method 【" + methodName + "】 ends with " + result);
//        } catch (Throwable e) {
//            System.out.println("【环绕通知中的--->异常通知】：the method 【" + methodName + "】 occurs exception " + e);
//        }
//
//        System.out.println("【环绕通知中的--->后置通知】：-----------------end.----------------------");
//        return result;
//    }


}
