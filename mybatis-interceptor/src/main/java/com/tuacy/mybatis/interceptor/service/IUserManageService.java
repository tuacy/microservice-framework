package com.tuacy.mybatis.interceptor.service;

import com.tuacy.mybatis.interceptor.entity.vo.UserInfoVo;
import com.tuacy.mybatis.interceptor.interceptor.page.PageView;

import java.util.List;

public interface IUserManageService {

    /**
     * 获取所有的用户列表信息
     */
    List<UserInfoVo> getAllUserList();


    /**
     * 分页获取所有的用户列表信息
     */
    PageView<UserInfoVo> getUserListPage(PageView<UserInfoVo> pageView);


    /**
     * 分页获取所有的用户列表信息 -- 自定义count查询
     */
    PageView<UserInfoVo> getUserListPageManualCount(PageView<UserInfoVo> pageView);
}
