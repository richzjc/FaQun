package com.wallstreetcn.global.manager;

import android.util.SparseArray;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewPoolInstance {
    private static RecyclerViewPoolInstance instance;
    private SparseArray<RecyclerView.RecycledViewPool> poolArray;
    public static final int NEWS_MAIN = 10;
    public static final int LIVE_MAIN = 11;
    public static final int IMAGE_HOLDER = 12;
    public static final int NEWS_RECYCLER = 13;


    private RecyclerViewPoolInstance() {
        poolArray = new SparseArray<>();
    }

    public static RecyclerViewPoolInstance getInstance() {
        if (instance == null) {
            instance = new RecyclerViewPoolInstance();
        }
        return instance;
    }

    public void attachRecyclerView(RecyclerView recyclerView) {
        attachRecyclerView(NEWS_MAIN, recyclerView);
    }

    public void attachRecyclerView(int key, RecyclerView recyclerView) {
        RecyclerView.RecycledViewPool pool;
        if (poolArray.indexOfKey(key) > 0) {
            pool = poolArray.get(key);
        } else {
            pool = new RecyclerView.RecycledViewPool();
            poolArray.append(key, pool);
        }
        recyclerView.setRecycledViewPool(pool);
    }

}
