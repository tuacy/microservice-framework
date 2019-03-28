package com.tuacy.microservice.framework.user.manage.service.impl;

import com.tuacy.microservice.framework.user.manage.api.response.UserInfoEntity;
import com.tuacy.microservice.framework.user.manage.mapper.UserManageMapper;
import com.tuacy.microservice.framework.user.manage.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "user-service")
public class UserServiceImpl implements IUserService {


    private UserManageMapper userManageMapper;


    @Autowired
    public void setUserManageMapper(UserManageMapper userManageMapper) {
        this.userManageMapper = userManageMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoEntity getUserInfo() {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setName("tuacy");
        userInfoEntity.setPhone("1868888");
        userInfoEntity.setPassword("tuacy");
        userManageMapper.saveUserInfo(userInfoEntity);
        return userInfoEntity;
    }

}
