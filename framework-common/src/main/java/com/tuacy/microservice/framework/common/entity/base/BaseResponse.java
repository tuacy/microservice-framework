package com.tuacy.microservice.framework.common.entity.base;

import com.tuacy.microservice.framework.common.constant.ResponseResultType;

import java.util.ArrayList;
import java.util.List;

public class BaseResponse<T> implements BaseEntity {

    private ResponseResultType resultType;
    private List<T> data;

    public BaseResponse() {
        this.resultType = ResponseResultType.SUCCESS;
    }

    public BaseResponse(ResponseResultType resultType) {
        this.resultType = resultType;
    }

    public BaseResponse(ResponseResultType resultType, T data) {
        this.resultType = resultType;
        this.data = new ArrayList<>();
        this.data.add(data);
    }

    /**
     * 自定义错误信息
     *
     * @param errMsg
     */
    public void setCustomErrMsg(String errMsg) {
        this.resultType = ResponseResultType.OTHER_ERROR;
        this.resultType.setDesc(errMsg);
    }

    public ResponseResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResponseResultType resultType) {
        this.resultType = resultType;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
