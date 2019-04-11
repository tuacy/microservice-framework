package com.tuacy.mybatis.interceptor.service;

import com.tuacy.mybatis.interceptor.entity.param.AlarmInfoInsetParam;

public interface IAlarmManageService {

    /**
     * 插入一条告警
     * @param param 告警信息
     */
   void insertAlarm(AlarmInfoInsetParam param);

}
