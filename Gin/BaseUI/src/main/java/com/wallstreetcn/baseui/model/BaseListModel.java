package com.wallstreetcn.baseui.model;

import android.text.TextUtils;

import com.wallstreetcn.baseui.adapter.IDifference;

import java.util.List;

/**
 * Created by zhangyang on 16/6/24.
 */
public abstract class BaseListModel<T extends IDifference> {
    public String next_cursor;
    private boolean isTouchEnd = false;
    private int limit = 20;
    private int counter = 0;
    private boolean isCache;

    public int getCounter() {
        return counter;
    }

    public void updateCounter() {
        counter++;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isTouchEnd() {
        return isTouchEnd;
    }

    public void setTouchEnd(boolean touchEnd) {
        isTouchEnd = touchEnd;
    }

    public String getNextCursor() {
        return TextUtils.isEmpty(next_cursor) ? "" : next_cursor;
    }

    public void clear() {
        next_cursor = "";
        counter = 0;
    }

    public void addList(List<T> list) {
        if(getResults() == null) {
            setResults(list);
        } else {
            getResults().addAll(list);
        }
    }


    public void setupDataMap() {
        clear();
    }

    public abstract List<T> getResults();

    public abstract void setResults(List<T> results);

    public void checkIsEnd(BaseListModel<T> model) {
        if (model == null) return;
        List<T> list = model.getResults();
        if (list == null || list.isEmpty()) {
            setTouchEnd(true);
        } else if (TextUtils.isEmpty(model.next_cursor)) {
            setTouchEnd(true);
        } else {
            setTouchEnd(false);
        }
    }

}
