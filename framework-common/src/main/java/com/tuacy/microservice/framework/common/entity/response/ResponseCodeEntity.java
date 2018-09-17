package com.tuacy.microservice.framework.common.entity.response;


/**
 * http请求返回数据，status + code
 */
public class ResponseCodeEntity extends ResponseBaseEntity {

    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
