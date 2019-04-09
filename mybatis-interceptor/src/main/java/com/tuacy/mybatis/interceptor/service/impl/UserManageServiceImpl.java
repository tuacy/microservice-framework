package com.tuacy.mybatis.interceptor.service.impl;

import com.tuacy.mybatis.interceptor.entity.vo.UserInfoVo;
import com.tuacy.mybatis.interceptor.interceptor.page.PageView;
import com.tuacy.mybatis.interceptor.mapper.UserManageMapper;
import com.tuacy.mybatis.interceptor.service.IUserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "user-manage")
public class UserManageServiceImpl implements IUserManageService {

    private UserManageMapper userManageMapper;

    @Autowired
    public void setUserManageMapper(UserManageMapper userManageMapper) {
        this.userManageMapper = userManageMapper;
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    @Override
    public List<UserInfoVo> getAllUserList() {
        PageView pageView = new PageView();
        List<UserInfoVo> retList = userManageMapper.getAllUserListPage(pageView);
        pageView.setLists(retList);
        return retList;
    }
}
