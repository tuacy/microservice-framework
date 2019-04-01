package com.tuacy.mybatis.interceptor.controller;

import com.tuacy.microservice.framework.common.controller.BaseController;
import com.tuacy.microservice.framework.common.entity.response.ResponseListEntity;
import com.tuacy.mybatis.interceptor.entity.param.GetAllUserParam;
import com.tuacy.mybatis.interceptor.entity.vo.UserInfoVo;
import com.tuacy.mybatis.interceptor.service.IUserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserManageController extends BaseController {

    private IUserManageService userManageService;

    @Autowired
    public void setUserManageService(IUserManageService userManageService) {
        this.userManageService = userManageService;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseListEntity<UserInfoVo> getAllUser(@RequestBody GetAllUserParam param) {
        ResponseListEntity<UserInfoVo> responseDataEntity = new ResponseListEntity<>();
        try {
            responseDataEntity.setData(userManageService.getAllUserList());
        } catch (Exception e) {
            responseDataEntity.setStatus(-1);
        }
        return responseDataEntity;
    }

}
