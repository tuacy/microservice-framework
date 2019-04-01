package com.tuacy.mybatis.interceptor.mapper;

import com.tuacy.mybatis.interceptor.entity.vo.UserInfoVo;

import java.util.List;

/**
 * 用户管理mapper
 */
public interface UserManageMapper {

    List<UserInfoVo> getAllUserList();

}
