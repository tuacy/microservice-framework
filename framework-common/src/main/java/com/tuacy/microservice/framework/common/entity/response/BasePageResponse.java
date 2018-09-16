package com.tuacy.microservice.framework.common.entity.response;

public class BasePageResponse<T> extends BaseResponse {

    private int pageIndex;
    private int pageCount;
    private int totalCount;

}
