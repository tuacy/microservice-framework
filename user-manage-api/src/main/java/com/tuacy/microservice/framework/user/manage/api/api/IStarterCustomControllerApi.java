package com.tuacy.microservice.framework.user.manage.api.api;

import com.tuacy.microservice.framework.common.entity.response.ResponseDataEntity;
import com.tuacy.microservice.framework.user.manage.api.request.UrlDecorationParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/starter/")
public interface IStarterCustomControllerApi {

    @RequestMapping(value = "urlDecoration", method = RequestMethod.POST)
    ResponseDataEntity<String> urlDecoration(@RequestBody UrlDecorationParam param);

}
