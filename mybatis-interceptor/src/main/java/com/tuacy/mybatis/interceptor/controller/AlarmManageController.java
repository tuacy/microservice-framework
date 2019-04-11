package com.tuacy.mybatis.interceptor.controller;

import com.tuacy.microservice.framework.common.controller.BaseController;
import com.tuacy.microservice.framework.common.entity.response.ResponseListEntity;
import com.tuacy.mybatis.interceptor.entity.param.AlarmInfoInsetParam;
import com.tuacy.mybatis.interceptor.entity.vo.UserInfoVo;
import com.tuacy.mybatis.interceptor.service.IAlarmManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alarm/")
public class AlarmManageController extends BaseController {

    private IAlarmManageService alarmManageService;

    @Autowired
    public void setAlarmManageService(IAlarmManageService alarmManageService) {
        this.alarmManageService = alarmManageService;
    }


    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public ResponseListEntity<UserInfoVo> insertAlarm(@RequestBody AlarmInfoInsetParam param) {
        ResponseListEntity<UserInfoVo> responseDataEntity = new ResponseListEntity<>();
        alarmManageService.insertAlarm(param);
        return responseDataEntity;
    }

}
