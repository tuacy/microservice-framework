package com.tuacy.microservice.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 通过post方式调用http请求接口的工具类
 */
public class HttpUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 获取request 中的 QueryString
     *
     * @param request 请求对象
     * @return 返回QueryString 字符串
     */
    public static String getRequestQueryString(HttpServletRequest request) {
        String result = "";
        try {
            request.setCharacterEncoding("UTF-8");
            result = request.getQueryString();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return result;
    }
}
