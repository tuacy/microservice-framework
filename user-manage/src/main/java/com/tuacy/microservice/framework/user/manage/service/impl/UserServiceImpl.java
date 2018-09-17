package com.tuacy.microservice.framework.user.manage.service.impl;

import com.tuacy.microservice.framework.user.manage.entity.UserInfoEntity;
import com.tuacy.microservice.framework.user.manage.service.IUserService;
import org.springframework.stereotype.Service;

@Service(value = "user-service")
public class UserServiceImpl implements IUserService {


    @Override
    public UserInfoEntity getUserInfo() {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUserId(1L);
        userInfoEntity.setRoleId(1L);
        userInfoEntity.setUserName("tuacy");
        return userInfoEntity;
    }
}
