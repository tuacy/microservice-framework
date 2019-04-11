package com.tuacy.mybatis.interceptor.service.impl;

import com.tuacy.mybatis.interceptor.entity.param.AlarmInfoInsetParam;
import com.tuacy.mybatis.interceptor.mapper.AlarmManageMapper;
import com.tuacy.mybatis.interceptor.service.IAlarmManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "alarm-manage")
public class AlarmManageServiceImpl implements IAlarmManageService {

    private AlarmManageMapper alarmManageMapper;

    @Autowired
    public void setAlarmManageMapper(AlarmManageMapper alarmManageMapper) {
        this.alarmManageMapper = alarmManageMapper;
    }

    /**
     * 插入一条告警
     * @param param 告警信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAlarm(AlarmInfoInsetParam param) {
        alarmManageMapper.insertAlarm(param, param.getAlarmOccurTime());
    }

}
