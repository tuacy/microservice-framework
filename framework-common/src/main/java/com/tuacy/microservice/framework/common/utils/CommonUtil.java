package com.tuacy.microservice.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class.getName());

    /**
     * 对象转换为List
     *
     * @param obj
     * @return
     */
    public static List<Object> itemToList(Object obj) {
        List<Object> objList = new ArrayList<Object>();
        objList.add(obj);
        return objList;
    }

    /**
     * 对象转换为List
     *
     * @param list
     * @return
     */
    public static List<Object> listToObjectList(List<? extends Object> list) {
        List<Object> objList = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            objList.add(list.get(i));
        }
        return objList;
    }

    /**
     * 获取请求URL参数,适用于GET和POST请求
     *
     * @param name
     * @param str
     * @return
     */
    public static String getQueryStringValue(String name, String str) {
        if (StringUtil.validEmpty(str)) {
            String[] paramArr = str.split("&");
            for (String param : paramArr) {
                if (StringUtil.validEmpty(param) && param.indexOf("=") > -1) {
                    String[] valArr = param.split("=");
                    if (name.equalsIgnoreCase(valArr[0])) {
                        return valArr[1];
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取请求Token
     *
     * @return
     */
    public static String getRequestAttribute(HttpServletRequest request, String code, String queryStr) {
        return request.getHeader(code) == null ? CommonUtil.getQueryStringValue(code, queryStr) : request.getHeader(code);
    }

}
