package com.wallstreetcn.baseui.base;

import android.os.Bundle;
import com.wallstreetcn.helper.utils.TLog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.wallstreetcn.baseui.adapter.BaseCustomAdapter;
import com.wallstreetcn.baseui.adapter.BaseRecycleAdapter;
import com.wallstreetcn.baseui.model.BaseCustomModel;

import java.util.List;

public abstract class BaseCustomRvSwipeActivity extends BaseSwipeRecyclerViewActivity {

    private Lifecycle.Event state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = Lifecycle.Event.ON_CREATE;
    }

    @Override
    protected void onStart() {
        super.onStart();
        state = Lifecycle.Event.ON_START;
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = Lifecycle.Event.ON_RESUME;
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = Lifecycle.Event.ON_PAUSE;
    }

    @Override
    protected void onStop() {
        super.onStop();
        state = Lifecycle.Event.ON_STOP;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        state = Lifecycle.Event.ON_DESTROY;
    }

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        ptrRecyclerView.setCanRefresh(isCanPullToRefresh());
        recycleView.setIsEndless(isCanLoadMore());
        setListener();
    }

    private void setListener() {
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                TLog.i("state", "" + newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING && (state == Lifecycle.Event.ON_RESUME)) {
                    ((LifecycleRegistry)getLifecycle()).handleLifecycleEvent(Lifecycle.Event.ON_STOP);
                } else if(state == Lifecycle.Event.ON_RESUME){
                    ((LifecycleRegistry)getLifecycle()).handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Nullable
    @Override
    public BaseRecycleAdapter doInitAdapter() {
        return new BaseCustomAdapter(this);
    }

    @Override
    public void doInitData() {
        super.doInitData();
        addDecor();

        ((BaseCustomAdapter) adapter).setCustomData(getAdapterData());
    }

    protected void addDecor() {
        if (isNeedStickyHeader()) {
            StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration((StickyRecyclerHeadersAdapter) adapter);
            recycleView.addItemDecoration(headersDecor);
        }
    }

    @Override
    public void onLoadMore(int page) {
        ((BaseCustomAdapter) adapter).loadMore();
    }

    @Override
    public void onRefresh() {
        ((BaseCustomAdapter) adapter).refresh();
    }

    protected abstract boolean isNeedStickyHeader();

    protected abstract boolean isCanPullToRefresh();

    protected abstract boolean isCanLoadMore();

    protected abstract List<BaseCustomModel> getAdapterData();
}
