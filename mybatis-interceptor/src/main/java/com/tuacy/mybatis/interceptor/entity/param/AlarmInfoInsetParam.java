package com.tuacy.mybatis.interceptor.entity.param;

public class AlarmInfoInsetParam {

    private String alarmEventName;
    private int alarmLevel;
    private String alarmOccurTime;

    public String getAlarmEventName() {
        return alarmEventName;
    }

    public void setAlarmEventName(String alarmEventName) {
        this.alarmEventName = alarmEventName;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmOccurTime() {
        return alarmOccurTime;
    }

    public void setAlarmOccurTime(String alarmOccurTime) {
        this.alarmOccurTime = alarmOccurTime;
    }
}
