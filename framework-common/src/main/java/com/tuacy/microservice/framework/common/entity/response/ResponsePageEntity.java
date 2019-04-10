package com.tuacy.microservice.framework.common.entity.response;

/**
 * http请求返回数据 page info + list
 */
public class ResponsePageEntity<T> extends ResponseListEntity<T> {
    private int pageIndex;
    private int pageSize;
    private long pageCount;
    private long totalCount;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
