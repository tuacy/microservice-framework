package com.tuacy.microservice.framework.common.constant;


import com.tuacy.microservice.framework.common.utils.StringUtil;

public enum ResponseResultType {

    SUCCESS(0, "SUCCESS"),                        //成功
    REQUEST_PARA_ERROR(1, "Parameter Error"),             //参数不正确
    JSON_PARA_EXCEPTION(2, "Json format error"),      //JSON 参数格式错误
    DB_OPERATE_ERROR(3, "Database operation failed"),         //数据库操作失败
    USER_NO_AUTH(4, "No authority"),              //没有权限
    SYS_SO_BUSY(5, "Server busy"),                     //服务器忙
    USER_NO_LOGIN(6, "User not login"),              //用户未登录
    OTHER_ERROR(7, "Other error"),              //其他错误（自定义提示内容）
    NEED_CONFIRM(8, "Need confirm");              // 当保存操作时需要进行再次确认

    private int value = 0;
    private String desc = null;
    private String myDesc = null;   //用户自定义显示内容

    ResponseResultType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * int到enum的转换函数
     *
     * @param value
     * @return
     */
    public static ResponseResultType valueOf(int value) {
        switch (value) {
            case 0:
                return SUCCESS;
            case 1:
                return REQUEST_PARA_ERROR;
            case 2:
                return JSON_PARA_EXCEPTION;
            case 3:
                return DB_OPERATE_ERROR;
            case 4:
                return USER_NO_AUTH;
            case 5:
                return SYS_SO_BUSY;
            case 6:
                return USER_NO_LOGIN;
            case 7:
                return OTHER_ERROR;
            default:
                return null;
        }

    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        String ret = StringUtil.validEmpty(myDesc) ? myDesc : desc;
        this.myDesc = null;     //清空自定义内容
        return ret;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setDesc(String desc) {
        this.myDesc = desc;
    }
}
