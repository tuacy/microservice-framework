package com.tuacy.microservice.framework.user.manage.service.impl;

import com.tuacy.microservice.framework.user.manage.entity.UserInfoEntity;
import com.tuacy.microservice.framework.user.manage.mapper.UserManageMapper;
import com.tuacy.microservice.framework.user.manage.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "user-service")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    private UserManageMapper userManageMapper;

    @Autowired
    public void setUserManageMapper(UserManageMapper userManageMapper) {
        this.userManageMapper = userManageMapper;
    }

    @Override
    public UserInfoEntity getUserInfo() {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setName("tuacy");
        userInfoEntity.setPhone("1868888");
        userInfoEntity.setPassword("tuacy");
        List<String> nameList = userManageMapper.selectName();
        return userInfoEntity;
    }
}
