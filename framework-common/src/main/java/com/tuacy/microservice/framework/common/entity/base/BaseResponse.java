package com.tuacy.microservice.framework.common.entity.base;

import com.tuacy.microservice.framework.common.constant.ResultType;

import java.util.ArrayList;
import java.util.List;

public class BaseResponse<T> implements BaseEntity {

    private ResultType resultType;
    private List<T> data;

    public BaseResponse() {
        this.resultType = ResultType.SUCCESS;
    }

    public BaseResponse(ResultType resultType) {
        this.resultType = resultType;
    }

    public BaseResponse(ResultType resultType, T data) {
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
        this.resultType = ResultType.OTHER_ERROR;
        this.resultType.setDesc(errMsg);
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
