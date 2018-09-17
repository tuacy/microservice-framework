package com.tuacy.microservice.framework.common.entity.response;

/**
 * http请求返回数据 page info + list
 */
public class ResponsePageEntity<T> extends ResponseListEntity<T> {
    private int pageIndex;
    private int pageCount;
    private int totalCount;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
