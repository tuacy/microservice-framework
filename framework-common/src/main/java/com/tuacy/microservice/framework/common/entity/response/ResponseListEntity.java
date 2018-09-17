package com.tuacy.microservice.framework.common.entity.response;

import java.util.List;

public class ResponseListEntity<T> extends ResponseCodeEntity {

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
