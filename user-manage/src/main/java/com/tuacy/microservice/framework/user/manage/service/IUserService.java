package com.tuacy.microservice.framework.user.manage.service;

import com.tuacy.microservice.framework.user.manage.api.response.UserInfoEntity;

public interface IUserService {

    /**
     * 获取用户信息
     *
     * @return 用户信息实体类
     */
    UserInfoEntity getUserInfo();

}
