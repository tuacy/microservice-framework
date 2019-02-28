package com.tuacy.microservice.framework.user.manage.controller;

import com.tuacy.microservice.framework.common.constant.ResponseResultType;
import com.tuacy.microservice.framework.common.controller.BaseController;
import com.tuacy.microservice.framework.common.entity.response.ResponseDataEntity;
import com.tuacy.microservice.framework.user.manage.entity.UserInfoEntity;
import com.tuacy.microservice.framework.user.manage.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController extends BaseController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDataEntity<UserInfoEntity> getUser() {
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
