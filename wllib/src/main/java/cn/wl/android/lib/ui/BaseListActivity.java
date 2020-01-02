package cn.wl.android.lib.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import cn.wl.android.lib.R;
import cn.wl.android.lib.core.ErrorBean;
import cn.wl.android.lib.core.Page;
import cn.wl.android.lib.core.PageParam;
import cn.wl.android.lib.data.core.DefResult;
import cn.wl.android.lib.miss.EmptyMiss;
import cn.wl.android.lib.miss.NetMiss;
import cn.wl.android.lib.ui.common.LoadView;
import cn.wl.android.lib.ui.holder.StatusCodePool;
import cn.wl.android.lib.utils.Toasts;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by JustBlue on 2019-08-29.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 分页Activity基类
 */
public abstract class BaseListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private BaseQuickAdapter mAdapter;

    protected RecyclerView rvContent;
    protected SwipeRefreshLayout refLayout;

    private PageParam mPageParam;

    @Override
    protected void internalViewCreated() {
        super.internalViewCreated();

        rvContent = findViewById(R.id.rv_content);
        refLayout = findViewById(R.id.refresh_layout);

        if (rvContent != null) {
            initRecyclerView(rvContent);
        }
        if (refLayout != null) {
            initRefreshLayout(refLayout);
        }
    }

    /**
     * 初始化刷新控件
     *
     * @param refLayout
     */
    protected void initRefreshLayout(SwipeRefreshLayout refLayout) {
        refLayout.setOnRefreshListener(this);
    }

    /**
     * 初始化
     *
     * @param recyclerView
     */
    protected void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(getAdapter());
    }

    /**
     * 获取RecyclerView的Adapter
     *
     * @return
     */
    protected BaseQuickAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = createAdapter();
            mAdapter.setLoadMoreView(new LoadView(() ->
                    loadData(true)));
            mAdapter.setOnLoadMoreListener(this);
        }
        return mAdapter;
    }

    /**
     * 创建适配器
     *
     * @return
     */
    protected abstract BaseQuickAdapter createAdapter();

    /**
     * 获取{@link androidx.recyclerview.widget.RecyclerView.LayoutManager}
     *
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public final void loadData() {
        loadData(false);
    }

    /**
     * 判断当前adapter是非为空
     *
     * @return
     */
    protected boolean isEmptyAdapter() {
        BaseQuickAdapter adapter = this.mAdapter;
        if (adapter != null) {
            List data = adapter.getData();
            return data.isEmpty();
        }

        return true;
    }

    /**
     * 加载数据
     *
     * @param loadMore 是否为分页加载更多
     */
    protected void loadData(boolean loadMore) {
        if (!loadMore) {
            if (mPageParam != null) {
                mPageParam.resetPage();
            }

            if (isEmptyAdapter()) {
                showLoading();
            }
        }
    }

    /**
     * 绑定集合数据
     *
     * @param source
     * @param doNext
     * @param <T>
     */
    protected <T> void bindListSub(@NonNull Observable<List<T>> source,
                                   @NonNull Consumer<List<T>> doNext) {
        bindListSub(source, doNext, null, null);
    }

    /**
     * 绑定集合数据
     *
     * @param source
     * @param doNext
     * @param doFail
     * @param <T>
     */
    protected <T> void bindListSub(@NonNull Observable<List<T>> source,
                                   @NonNull Consumer<List<T>> doNext,
                                   @Nullable Consumer<ErrorBean> doFail) {
        bindListSub(source, doNext, doFail, null);
    }

    /**
     * 绑定集合数据
     *
     * @param source
     * @param doNext
     * @param doFail
     * @param doDone
     * @param <T>
     */
    protected <T> void bindListSub(@NonNull Observable<List<T>> source,
                                   @NonNull Consumer<List<T>> doNext,
                                   @Nullable Consumer<ErrorBean> doFail,
                                   @Nullable Consumer<Boolean> doDone) {
        source.compose(bindDestroy())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefResult<List<T>>() {
                    @Override
                    protected void doNext(List<T> data) throws Exception {
                        showContent();

                        doNext.accept(data);

                        tryCompleteStatus(false);
                    }

                    @Override
                    protected void doError(ErrorBean bean) throws Exception {
                        bean.setLoadMore(false);
                        bean.setMode(ErrorBean.MODE_LIST);

                        if (doFail != null) {
                            doFail.accept(bean);
                        } else {
                            dispatchDataMiss(bean);
                        }
                    }

                    @Override
                    protected void doFinally(boolean fromMiss) throws Exception {
                        if (doDone != null) {
                            doDone.accept(fromMiss);
                        } else {
                            hideLoadingAlert();
                        }
                    }
                });
    }

    /**
     * 绑定分页数据
     *
     * @param loadMore
     * @param pageSource
     * @param doNext
     * @param <T>
     */
    protected <T> void bindPageSub(final boolean loadMore,
                                   @NonNull Observable<Page<T>> pageSource,
                                   @NonNull Consumer<List<T>> doNext) {
        bindPageSub(loadMore, pageSource, doNext, null, null);
    }

    /**
     * 绑定分页数据
     *
     * @param loadMore
     * @param pageSource
     * @param doNext
     * @param doFail
     * @param <T>
     */
    protected <T> void bindPageSub(final boolean loadMore,
                                   @NonNull Observable<Page<T>> pageSource,
                                   @NonNull Consumer<List<T>> doNext,
                                   @Nullable Consumer<ErrorBean> doFail) {
        bindPageSub(loadMore, pageSource, doNext, doFail, null);
    }

    /**
     * 绑定分页数据
     *
     * @param loadMore
     * @param pageSource
     * @param doNext
     * @param doFail
     * @param doDone
     * @param <T>
     */
    protected <T> void bindPageSub(final boolean loadMore,
                                   @NonNull Observable<Page<T>> pageSource,
                                   @NonNull Consumer<List<T>> doNext,
                                   @Nullable Consumer<ErrorBean> doFail,
                                   @Nullable Consumer<Boolean> doDone) {
        pageSource.compose(bindDestroy())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefResult<Page<T>>() {
                    @Override
                    protected void doNext(Page<T> data) throws Exception {
                        showContent();

                        // 驱动下一页数据
                        tryNextPageParam(data);

                        // 回传处理数据绑定
                        doNext.accept(data.getRecords());

                        tryCompleteStatus(data.hasData());
                    }

                    @Override
                    protected void doError(ErrorBean bean) throws Exception {
                        bean.setLoadMore(loadMore);
                        bean.setMode(ErrorBean.MODE_MORE);

                        if (doFail != null) {
                            doFail.accept(bean);
                        } else {
                            dispatchDataMiss(bean);
                        }
                    }

                    @Override
                    protected void doFinally(boolean fromMiss) throws Exception {
                        if (doDone != null) {
                            doDone.accept(fromMiss);
                        } else {
                            hideLoadingAlert();
                        }
                    }
                });
    }

    /**
     * 获取当前分页参数
     *
     * @return
     */
    protected PageParam getPageParam() {
        if (mPageParam == null) {
            mPageParam = createPageParam();
        }

        return mPageParam;
    }

    /**
     * 创建默认的分页参数
     *
     * @return
     */
    protected PageParam createPageParam() {
        return PageParam.create();
    }

    /**
     * 尝试更新分页数据, 并驱动分页数据指向下一页
     *
     * @param dataSource 当前分页实体类
     */
    private void tryNextPageParam(@NonNull Page dataSource) {
        PageParam param = this.mPageParam;
        if (param != null) {
            param.nextPage(dataSource.getCurrent());
        }
    }

    @Override
    public boolean onInterceptDataMiss(ErrorBean error) {
        int mode = error.getMode();

        if (mode == ErrorBean.MODE_LIST) {
            Throwable throwable = error.getError();
            if (throwable instanceof NetMiss) {
                if (!isEmptyAdapter()) {
                    Toasts.show(error.getMsg());
                    return true;
                }
            } else {
                mAdapter.setNewData(null);
            }
        } else if (mode == ErrorBean.MODE_MORE) {
            boolean loadMore = error.isLoadMore();
            Throwable throwable = error.getError();

            if (throwable instanceof EmptyMiss) {
                if (!loadMore || isEmptyAdapter()) {
                    mAdapter.setNewData(null);
                    showEmptyData(error);
                } else {
                    tryCompleteStatus(false);
                }
                return true;
            } else if (throwable instanceof NetMiss) {
                if (!isEmptyAdapter()) {
                    Toasts.show(error.getMsg());
                    return true;
                }
            } else {
                if (!isEmptyAdapter()) {
                    tryFailureStatus();
                    return true;
                }
            }
        }

        return super.onInterceptDataMiss(error);
    }

    /**
     * 尝试更新完成状态
     *
     * @param hasData
     */
    protected void tryCompleteStatus(boolean hasData) {
        if (refLayout != null && refLayout.isRefreshing()) {
            refLayout.setRefreshing(false);
        }

        mAdapter.loadMoreComplete();
        if (!hasData) {
            mAdapter.loadMoreEnd();
        }
    }

    /**
     * 尝试设置失败状态
     */
    protected void tryFailureStatus() {
        if (refLayout != null && refLayout.isRefreshing()) {
            refLayout.setRefreshing(false);
        }

        if (mAdapter.isLoading()) {
            mAdapter.loadMoreFail();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(true);
    }

}
