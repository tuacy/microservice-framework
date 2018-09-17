package com.tuacy.microservice.framework.common.controller;

import com.tuacy.microservice.framework.common.entity.param.BaseParam;
import com.tuacy.microservice.framework.common.utils.CommonUtil;
import com.tuacy.microservice.framework.common.utils.HttpUtil;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * http controller base
 */
public abstract class BaseController {

    protected <F> F getAuthRequestObject(BaseParam baseParam, HttpServletRequest request) {
        if (baseParam != null) {
            String token = CommonUtil.getRequestAttribute(request, "access_token", HttpUtil.getRequestQueryString(request));
        }
        return (F) baseParam;
    }

}
