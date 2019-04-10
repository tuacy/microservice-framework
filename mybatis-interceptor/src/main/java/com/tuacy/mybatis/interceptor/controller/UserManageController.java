package com.tuacy.mybatis.interceptor.controller;

import com.tuacy.microservice.framework.common.controller.BaseController;
import com.tuacy.microservice.framework.common.entity.response.ResponseListEntity;
import com.tuacy.microservice.framework.common.entity.response.ResponsePageEntity;
import com.tuacy.mybatis.interceptor.entity.param.GetAllUserParam;
import com.tuacy.mybatis.interceptor.entity.param.GetUserPageParam;
import com.tuacy.mybatis.interceptor.entity.vo.UserInfoVo;
import com.tuacy.mybatis.interceptor.interceptor.page.PageView;
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

    /**
     * 常规请求，不做分页
     */
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

    /**
     * 分页请求
     */
    @RequestMapping(value = "listPage", method = RequestMethod.POST)
    public ResponsePageEntity<UserInfoVo> getUserPage(@RequestBody GetUserPageParam param) {
        ResponsePageEntity<UserInfoVo> responseDataEntity = new ResponsePageEntity<>();
        try {
            PageView<UserInfoVo> pageView = new PageView<>(param.getPageIndex(), param.getPageSize());
            PageView<UserInfoVo> pageViewResult = userManageService.getUserListPage(pageView);
            responseDataEntity.setPageCount(pageViewResult.getTotalPage());
            responseDataEntity.setPageIndex(pageViewResult.getPageNo());
            responseDataEntity.setPageSize(pageViewResult.getPageSize());
            responseDataEntity.setTotalCount(pageViewResult.getTotalCount());
            responseDataEntity.setData(pageViewResult.getLists());
        } catch (Exception e) {
            responseDataEntity.setStatus(-1);
        }
        return responseDataEntity;
    }

    /**
     * 分页请求，并且子定义count查询
     */
    @RequestMapping(value = "listPageManualCount", method = RequestMethod.POST)
    public ResponsePageEntity<UserInfoVo> getUserPageManualCount(@RequestBody GetUserPageParam param) {
        ResponsePageEntity<UserInfoVo> responseDataEntity = new ResponsePageEntity<>();
        try {
            PageView<UserInfoVo> pageView = new PageView<>(param.getPageIndex(), param.getPageSize());
            PageView<UserInfoVo> pageViewResult = userManageService.getUserListPageManualCount(pageView);
            responseDataEntity.setPageCount(pageViewResult.getTotalPage());
            responseDataEntity.setPageIndex(pageViewResult.getPageNo());
            responseDataEntity.setPageSize(pageViewResult.getPageSize());
            responseDataEntity.setTotalCount(pageViewResult.getTotalCount());
            responseDataEntity.setData(pageViewResult.getLists());
        } catch (Exception e) {
            responseDataEntity.setStatus(-1);
        }
        return responseDataEntity;
    }

}
