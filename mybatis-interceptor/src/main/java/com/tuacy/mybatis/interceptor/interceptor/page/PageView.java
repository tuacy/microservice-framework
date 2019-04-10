package com.tuacy.mybatis.interceptor.interceptor.page;

import java.util.List;

public class PageView<T> {

    /**
     * 默认每一页的数据大小
     */
    private static final int PAGE_SIZE = 10;

    /**
     * 当前需要查询的页码 ,从0开始
     */
    private int pageNo;
    /**
     * 每页显示几条记录
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private long totalCount;
    /**
     * 是否需要进行count查询，因为在有些情况，不需要每次都进行count查询
     */
    private boolean doCount;
    /**
     * 总页数
     */
    private long totalPage;

    /**
     * 最终查询出来的数据
     */
    private List<T> lists;

    public PageView() {
        this(0);
    }

    /**
     * 使用构造函数，，强制必需输入 当前页
     *
     * @param pageNo 　当前页
     */
    public PageView(int pageNo) {
        this(pageNo, PAGE_SIZE, true);
    }

    /**
     * @param pageSize 每一页的大小
     * @param pageNo   当前查询第几页，从1开始
     */
    public PageView(int pageNo, int pageSize) {
        this(pageNo, pageSize, true);
    }

    /**
     * @param pageSize 每一页的大小
     * @param pageNo   当前查询第几页，从1开始
     * @param doCount  是否需要进行count查询
     */
    public PageView(int pageNo, int pageSize, boolean doCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.doCount = doCount;
    }

    /**
     * 查询结果方法 把　记录数　结果集合　放入到　PageView对象
     *
     * @param rowCount 总记录数
     * @param records  结果集合
     */

    public void setQueryResult(long rowCount, List<T> records) {
        setLists(records);
    }


    public List<T> getLists() {
        return lists;
    }

    public void setLists(List<T> lists) {
        this.lists = lists;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        setTotalPage(this.totalCount % this.pageSize == 0 ? this.totalCount / this.pageSize : this.totalCount / this.pageSize + 1);
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isDoCount() {
        return doCount;
    }

    public void setDoCount(boolean doCount) {
        this.doCount = doCount;
    }
}
