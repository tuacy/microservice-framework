package com.tuacy.mybatis.interceptor.service.impl;

import com.tuacy.mybatis.interceptor.mapper.AlarmManageMapper;
import com.tuacy.mybatis.interceptor.service.IAlarmManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "alarm-manage")
public class AlarmManageServiceImpl implements IAlarmManageService {

    private AlarmManageMapper alarmManageMapper;

    @Autowired
    public void setAlarmManageMapper(AlarmManageMapper alarmManageMapper) {
        this.alarmManageMapper = alarmManageMapper;
    }


}
