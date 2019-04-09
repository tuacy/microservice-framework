package com.tuacy.mybatis.interceptor.interceptor.utils;

import java.lang.reflect.Field;

/**
 * 反射工具
 */
public class ReflectHelper {

    /**
     * 获取obj对象fieldName的Field
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @return 字段
     */
    private static Field getFieldByFieldName(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }
        // 遍历父类
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                // 获得某个类的所有声明的字段，即包括public、private和protected，但是不包括父类的申明字段
                return superClass.getDeclaredField(fieldName);
            } catch (Exception e) {
                // 当前对象没有该字段，继续去父类里面查找
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @return obj对象fieldName的属性值
     */
    public static Object getValueByFieldName(Object obj, String fieldName) {
        Object value = null;
        try {
            Field field = getFieldByFieldName(obj, fieldName);
            if (field != null) {
                // 获取字段对应的值
                if (field.isAccessible()) {
                    value = field.get(obj);
                } else {
                    field.setAccessible(true);
                    value = field.get(obj);
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     *
     * @param obj       对象
     * @param fieldName 属性名称
     * @param value     属性值
     */
    public static boolean setValueByFieldName(Object obj, String fieldName, Object value) {
        try {
            //java.lang.Class.getDeclaredField()方法用法实例教程 - 方法返回一个Field对象，它反映此Class对象所表示的类或接口的指定已声明字段。
            //此方法返回这个类中的指定字段的Field对象
            Field field = obj.getClass().getDeclaredField(fieldName);
            /**
             * public void setAccessible(boolean flag)
             *       throws SecurityException将此对象的 accessible 标志设置为指示的布尔值。值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
             * 	首先，如果存在安全管理器，则在 ReflectPermission("suppressAccessChecks") 权限下调用 checkPermission 方法。
             * 	如果 flag 为 true，并且不能更改此对象的可访问性（例如，如果此元素对象是 Class 类的 Constructor 对象），则会引发 SecurityException。
             * 	如果此对象是 java.lang.Class 类的 Constructor 对象，并且 flag 为 true，则会引发 SecurityException。
             * 	参数：
             * 	flag - accessible 标志的新值
             * 	抛出：
             * 	SecurityException - 如果请求被拒绝。
             */
            if (field.isAccessible()) {//获取此对象的 accessible 标志的值。
                field.set(obj, value);//将指定对象变量上此 Field 对象表示的字段设置为指定的新值
            } else {
                field.setAccessible(true);
                field.set(obj, value);
                field.setAccessible(false);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 去obj对象里面找某种类型对应的值
     * 比如去obj对象里面查找 String类型对应的值
     *
     * @param obj       对象
     * @param fieldType 属性列席
     * @return 属性对应值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValueByFieldType(Object obj, Class<T> fieldType) {
        Object value = null;
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field[] fields = superClass.getDeclaredFields();
                for (Field f : fields) {
                    // 遍历每个属性，判断属性类型是否对应
                    if (f.getType() == fieldType) {
                        if (f.isAccessible()) {
                            value = f.get(obj);
                            break;
                        } else {
                            f.setAccessible(true);
                            value = f.get(obj);
                            f.setAccessible(false);
                            break;
                        }
                    }
                }
                if (value != null) {
                    // 找到
                    break;
                }
            } catch (Exception e) {
                // 当前对象没有该字段，继续去父类里面查找
            }
        }
        return (T) value;
    }

}
