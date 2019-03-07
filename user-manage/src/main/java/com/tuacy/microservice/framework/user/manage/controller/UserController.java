package com.tuacy.microservice.framework.user.manage.controller;

import com.tuacy.microservice.framework.common.constant.ResponseResultType;
import com.tuacy.microservice.framework.common.controller.BaseController;
import com.tuacy.microservice.framework.common.entity.response.ResponseDataEntity;
import com.tuacy.microservice.framework.user.manage.annotation.ClassAnnotation;
import com.tuacy.microservice.framework.user.manage.annotation.LoggingAnnotation;
import com.tuacy.microservice.framework.user.manage.api.api.IUserControllerApi;
import com.tuacy.microservice.framework.user.manage.api.request.UserParam;
import com.tuacy.microservice.framework.user.manage.api.response.UserInfoEntity;
import com.tuacy.microservice.framework.user.manage.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@ClassAnnotation()
@RestController
@RequestMapping("/user/")
public class UserController extends BaseController implements IUserControllerApi {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @LoggingAnnotation()
    public ResponseDataEntity<UserInfoEntity> getUser(@RequestBody UserParam param) {
        ResponseDataEntity<UserInfoEntity> responseDataEntity = new ResponseDataEntity<>();
        try {
            responseDataEntity.setMsg(ResponseResultType.SUCCESS.getDesc());
            responseDataEntity.setStatus(ResponseResultType.SUCCESS.getValue());
            responseDataEntity.setData(userService.getUserInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseDataEntity;
    }
}
