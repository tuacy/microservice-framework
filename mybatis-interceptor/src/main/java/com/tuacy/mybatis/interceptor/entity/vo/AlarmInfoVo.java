package com.tuacy.mybatis.interceptor.entity.vo;

public class AlarmInfoVo {

    private long alarmEventId;
    private String alarmEventName;
    private int alarmLevel;
    private String alarmOccurTime;

    public long getAlarmEventId() {
        return alarmEventId;
    }

    public void setAlarmEventId(long alarmEventId) {
        this.alarmEventId = alarmEventId;
    }

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
