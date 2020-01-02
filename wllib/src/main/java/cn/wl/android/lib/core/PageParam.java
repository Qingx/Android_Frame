package cn.wl.android.lib.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JustBlue on 2019-09-16.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 分页参数
 */
public class PageParam {

    public static final int INIT_PAGE = 1;
    public static final int PAGE_NUM = 10;

    private int page = INIT_PAGE;       // 分页从1开始
    private int pageSize = PAGE_NUM;    // 分页每页条数

    private List<OrderBy> orderBy;      // 排序规则

    /**
     * 创建一个默认的分页参数
     *
     * @return
     */
    public static PageParam create() {
        return new PageParam();
    }

    public void timeDesc(String fileName) {
        orderBy = new ArrayList<>();
        OrderBy orderBy = new OrderBy();
        orderBy.asc = false;
        orderBy.setColumnsName(fileName);
        this.orderBy.add(orderBy);
    }

    /**
     * 创建自定义分页属性
     *
     * @param page
     * @param pageSize
     * @return
     */
    public static PageParam create(int page, int pageSize) {
        PageParam param = create();
        param.page = page;
        param.pageSize = pageSize;

        return param;
    }

    /**
     * 创建带排序规则的分页参数
     *
     * @param orderBy
     * @return
     */
    public static PageParam create(List<OrderBy> orderBy) {
        PageParam param = create();
        param.orderBy = orderBy;

        return param;
    }

    private PageParam() {
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    /**
     * 驱动分页到下一页
     *
     * @param page
     * @return
     */
    public int nextPage(int page) {
        this.page = ++page;

        return this.page;
    }

    public void resetPage() {
        this.page = 1;
    }

    @Override
    public String toString() {
        return "PageParam{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", orderBy=" + orderBy +
                '}';
    }

    public static class OrderBy {

        private boolean asc;
        private String columnsName;

        public boolean isAsc() {
            return asc;
        }

        public void setAsc(boolean asc) {
            this.asc = asc;
        }

        public String getColumnsName() {
            return columnsName;
        }

        public void setColumnsName(String columnsName) {
            this.columnsName = columnsName;
        }

        @Override
        public String toString() {
            return "OrderByBean{" +
                    "asc=" + asc +
                    ", columnsName='" + columnsName + '\'' +
                    '}';
        }

    }
}
