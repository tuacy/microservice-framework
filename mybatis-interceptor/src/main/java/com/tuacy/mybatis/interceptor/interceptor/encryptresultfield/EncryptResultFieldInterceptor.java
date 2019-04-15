package com.tuacy.mybatis.interceptor.interceptor.encryptresultfield;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.*;

/**
 * 通过拦截器对返回结果中的某个字段进行加密处理
 */
@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class}
        )
})
public class EncryptResultFieldInterceptor implements Interceptor {

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取到返回结果
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaResultSetHandler = MetaObject.forObject(resultSetHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
        MappedStatement mappedStatement = (MappedStatement) metaResultSetHandler.getValue("mappedStatement");
        EncryptResultFieldAnnotation annotation = getEncryptResultFieldAnnotation(mappedStatement);
        Object returnValue = invocation.proceed();
        if (annotation != null && returnValue != null) {
            String[] fieldKeyList = annotation.fieldKey();
            Class<? extends IEncryptResultFieldStrategy>[] strategyClassList = annotation.encryptStrategy();
            if (strategyClassList.length != 0 && fieldKeyList.length == strategyClassList.length) {
                Map<String, Class<? extends IEncryptResultFieldStrategy>> strategyMap = null;
                for (int index = 0; index < fieldKeyList.length; index++) {
                    if (strategyMap == null) {
                        strategyMap = new HashMap<>();
                    }
                    strategyMap.put(fieldKeyList[index], strategyClassList[index]);
                }
                // 对结果进行处理
                try {
                    if (returnValue instanceof ArrayList<?>) {
                        List<?> list = (ArrayList<?>) returnValue;
                        for (int index = 0; index < list.size(); index++) {
                            Object returnItem = list.get(index);
                            if (returnItem instanceof String) {
                                List<String> stringList = (List<String>) list;
                                IEncryptResultFieldStrategy encryptStrategy = strategyMap.get(fieldKeyList[0]).newInstance();
                                stringList.set(index, encryptStrategy.encrypt((String) returnItem));
                            } else {
                                MetaObject metaReturnItem = MetaObject.forObject(returnItem, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, REFLECTOR_FACTORY);
                                for (Map.Entry<String, Class<? extends IEncryptResultFieldStrategy>> entry : strategyMap.entrySet()) {
                                    String fieldKey = entry.getKey();
                                    IEncryptResultFieldStrategy fieldEncryptStrategy = entry.getValue().newInstance();
                                    Object fieldValue = metaReturnItem.getValue(fieldKey);
                                    if (fieldValue instanceof String) {
                                        metaReturnItem.setValue(fieldKey, fieldEncryptStrategy.encrypt((String) fieldValue));
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // ignore
                }

            }
        }
        return returnValue;

    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取方法上的EncryptResultFieldAnnotation注解
     *
     * @param mappedStatement MappedStatement
     * @return EncryptResultFieldAnnotation注解
     */
    private EncryptResultFieldAnnotation getEncryptResultFieldAnnotation(MappedStatement mappedStatement) {
        EncryptResultFieldAnnotation encryptResultFieldAnnotation = null;
        try {
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1);
            final Method[] method = Class.forName(className).getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(EncryptResultFieldAnnotation.class)) {
                    encryptResultFieldAnnotation = me.getAnnotation(EncryptResultFieldAnnotation.class);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encryptResultFieldAnnotation;
    }


}
