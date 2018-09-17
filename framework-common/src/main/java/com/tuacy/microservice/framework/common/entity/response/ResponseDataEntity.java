package com.tuacy.microservice.framework.common.entity.response;

public class ResponseDataEntity<T> extends ResponseCodeEntity {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
