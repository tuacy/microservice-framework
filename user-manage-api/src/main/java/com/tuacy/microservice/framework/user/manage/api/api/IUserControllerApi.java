package com.tuacy.microservice.framework.user.manage.api.api;

import com.tuacy.microservice.framework.common.entity.response.ResponseDataEntity;
import com.tuacy.microservice.framework.user.manage.api.request.UserParam;
import com.tuacy.microservice.framework.user.manage.api.response.UserInfoEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/user/")
public interface IUserControllerApi {

    @RequestMapping(value = "get", method = RequestMethod.POST)
    ResponseDataEntity<UserInfoEntity> getUser(@RequestBody UserParam param);
}
