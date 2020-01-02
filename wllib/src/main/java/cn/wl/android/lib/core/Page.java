package cn.wl.android.lib.core;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 分页数据保存对象
 */
public class Page<T> implements IDataEmpty, IPageSource {

    private int pages;
    private int total;
    private int current;
    private boolean searchCount;

    private List<T> records = new ArrayList<>();

    /**
     * 创建一个空数据的分页数据
     *
     * @param <T>
     * @return
     */
    public static <T> Page<T> empty() {
        return new Page<>();
    }

    /**
     * 添加测试数据
     *
     * @param data
     * @param current
     * @param pages
     * @param <T>
     * @return
     */
    public static <T> Page<T> test(@NonNull List<T> data, int current, int pages) {
        Page<T> page = new Page<>();

        page.pages = pages;
        page.records = data;
        page.current = current;

        return page;
    }

    @Override
    public boolean isDataEmpty() {
        return records == null || records.isEmpty();
    }

    @Override
    public boolean hasData() {
        // 当前页数(1开始) * 页码 小于最大条数时
        return current < pages;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pages=" + pages +
                ", total=" + total +
                ", current=" + current +
                ", searchCount=" + searchCount +
                ", records=" + records +
                '}';
    }

}
