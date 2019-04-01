package com.tuacy.mybatis.interceptor.service;

import com.tuacy.mybatis.interceptor.entity.vo.UserInfoVo;

import java.util.List;

public interface IUserManageService {

    /**
     * 获取所有的用户列表信息
     */
    List<UserInfoVo> getAllUserList();

}
