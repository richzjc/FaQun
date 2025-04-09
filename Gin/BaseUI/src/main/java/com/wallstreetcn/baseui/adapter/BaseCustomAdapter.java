package com.wallstreetcn.baseui.adapter;

import android.content.Context;
import android.os.Parcel;
import android.text.TextUtils;

import com.wallstreetcn.helper.utils.TLog;

import android.view.ViewGroup;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.adapter.holder.DefaultRecycleHolder;
import com.wallstreetcn.baseui.base.BaseRecyclerViewCallBack;
import com.wallstreetcn.baseui.callback.ICustomModel;
import com.wallstreetcn.baseui.model.BaseCustomListModel;
import com.wallstreetcn.baseui.model.BaseCustomModel;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCustomAdapter extends BaseRecycleAdapter<Object, BaseRecycleViewHolder<Object>> implements StickyRecyclerHeadersAdapter<BaseRecycleViewHolder<Object>> {

    private List<BaseCustomModel> originDatas;
    private List<Object> realDatas;
    private Map<Integer, Class> viewHolderMaps = new HashMap<>();
    private LifecycleOwner owner;
    private RecyclerView recyclerView;
    private boolean isAddAnimation = true;
    private LifecycleObserver observer = new LifecycleObserver() {
        @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
        public void onResume() {
            realSetAdapterData();
        }
    };

    public BaseCustomAdapter(LifecycleOwner owner) {
        this.owner = owner;
    }

    public void setCustomData(List<BaseCustomModel> origin) {
        this.originDatas = origin;
        if (origin == null) {
            super.setData(null);
        } else {
            realDatas = new ArrayList<>();
            for (BaseCustomModel model : origin) {
                model.realRequestData();
                checkRule(model);
                TLog.i("uniqueId", model.getUniqueId());
                if (model.getValue() != null && model.getValue().size() > 0) {
                    for (Object o : model.getValue()) {
                        if (o instanceof ICustomModel)
                            ((ICustomModel) o).bindCustomModel(model);
                        realDatas.add(o);
                    }
                } else if (model.getValue() == null) {
                    realDatas.add(model);
                }

                addModelObserver(model);
            }
            setAdapterData();
        }
    }

    private void checkRule(BaseCustomModel model) {
        if (TextUtils.isEmpty(model.getUniqueId())) {
            throw new IllegalArgumentException("model.uniqueId must not be null and empty string. because diffUtil need use it");
        }

        try {
            model.getClass().getDeclaredConstructor(Parcel.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("model must have a constructor with parcel");

        }
    }

    public void addModelObserver(BaseCustomModel model) {
        model.observe(owner, iCustomModels -> {
            if (iCustomModels != null) {
                for (Object iCustomModel : iCustomModels) {
                    if (iCustomModel instanceof ICustomModel)
                        ((ICustomModel) iCustomModel).bindCustomModel(model);
                }
            }

            if (recyclerView != null && (recyclerView.getContext() instanceof LifecycleOwner)) {
                Lifecycle lifecycle = ((LifecycleOwner) recyclerView.getContext()).getLifecycle();
                if (lifecycle instanceof LifecycleRegistry) {
                    Lifecycle.State state = lifecycle.getCurrentState();
                    if (state != Lifecycle.State.RESUMED) {
                        lifecycle.addObserver(observer);
                    } else {
                        realSetAdapterData();
                    }
                } else {
                    realSetAdapterData();
                }
            } else {
                realSetAdapterData();
            }
        });
    }

    public void realSetAdapterData() {
        realDatas.clear();
        for (BaseCustomModel tempModel : originDatas) {
            if (tempModel.getValue() != null && tempModel.getValue().size() > 0) {
                realDatas.addAll(tempModel.getValue());
            } else if (tempModel.getValue() == null) {
                realDatas.add(tempModel);
            }
        }

        if (originDatas.get(originDatas.size() - 1) instanceof BaseCustomListModel)
            updateFooter(((BaseCustomListModel) originDatas.get(originDatas.size() - 1)).isTouchEnd());
        else
            updateFooter(true);

        setAdapterData();
    }

    @Override
    public void binderItemHolder(BaseRecycleViewHolder<Object> holder, int position) {
        if (holder != null) {
            holder.itemView.setTag(com.wallstreetcn.baseui.R.id.holder_position, position);
            holder.itemView.setTag(com.wallstreetcn.baseui.R.id.adapter_click_listener, adapterItemClickListener);
            holder.doBindData(get(position));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        int endIndex = getHeadViewSize() + getListItemCount();
        if (position >= getHeadViewSize() && position < endIndex) {
            Object obj = get(getListItemPosition(position));
            if (obj instanceof ICustomModel && originDatas != null) {
                for (BaseCustomModel originData : originDatas) {
                    if (originData.getPullToRefresh()
                            && originData.getValue() != null
                            && originData.getValue().contains(obj)) {
                        originData.realRequestData();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        removeAnimation(parent);
        this.recyclerView = (RecyclerView) parent;
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public BaseRecycleViewHolder<Object> createListItemView(ViewGroup parent, int viewType) {
        BaseRecycleViewHolder holder;
        try {
            if (viewHolderMaps.containsKey(viewType)) {
                holder = createHolder(viewHolderMaps.get(viewType), parent.getContext());
            } else {
                holder = new DefaultRecycleHolder(parent.getContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder = new DefaultRecycleHolder(parent.getContext());
        }
        return holder;
    }

    @Override
    public int getListType(int listItemPosition) {
        Object obj = get(listItemPosition);
        int viewType = 0;
        if (obj instanceof ICustomModel && ((ICustomModel) obj).getHolderClass() != null) {
            viewType = ((ICustomModel) obj).getHolderClass().getName().hashCode();
            viewHolderMaps.put(viewType, ((ICustomModel) obj).getHolderClass());
        } else {
            viewType = DefaultRecycleHolder.class.getName().hashCode();
        }
        return viewType;
    }


    @Override
    public long getHeaderId(int i) {
        Object obj = getItemAtPosition(i);
        long viewType;
        if (obj instanceof ICustomModel && ((ICustomModel) obj).getStickyHolderClass() != null) {
            viewType = ((ICustomModel) obj).getHeaderId();
        } else {
            viewType = -1;
        }
        return viewType;
    }

    @Override
    public BaseRecycleViewHolder<Object> onCreateHeaderViewHolder(ViewGroup parent, int i) {
        Object model = getItemAtPosition(i);
        BaseRecycleViewHolder holder = null;
        try {
            if (model instanceof ICustomModel && ((ICustomModel) model).getStickyHolderClass() != null) {
                holder = createHolder(((ICustomModel) model).getStickyHolderClass(), parent.getContext());
            } else {
                holder = new DefaultRecycleHolder(parent.getContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder = new DefaultRecycleHolder(parent.getContext());
        }
        return holder;
    }

    private BaseRecycleViewHolder createHolder(Class cls, Context context) {
        BaseRecycleViewHolder holder = null;
        try {
            Constructor constructor = cls.getConstructor(Context.class);
            constructor.setAccessible(true);
            holder = (BaseRecycleViewHolder) constructor.newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    @Override
    public void onBindHeaderViewHolder(BaseRecycleViewHolder<Object> holder, int i) {
        if (holder != null)
            holder.doBindData(getItemAtPosition(i));
    }

    public void refresh() {
        if (originDatas != null && recyclerView != null) {
            for (BaseCustomModel originData : originDatas) {
                originData.clear();
                originData.setPullToRefresh(true);
            }
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int first = manager.findFirstVisibleItemPosition() - getHeadViewSize();
            int last = manager.findLastVisibleItemPosition() - getHeadViewSize();
            int index = 0;
            List<BaseCustomModel> tempList = new ArrayList<>();
            for (BaseCustomModel originData : originDatas) {
                if (originData.getValue() != null && originData.getValue().size() == 0) {
                    tempList.add(originData);
                } else if (originData.getValue() != null && originData.getValue().size() > 0) {
                    int endIndex = index + originData.getValue().size();
                    boolean flag = (index > last) || (endIndex < first);
                    if (!flag) {
                        tempList.add(originData);
                        index = endIndex;
                    }
                } else {
                    int endIndex = index + 1;
                    if ((endIndex - 1) >= first && (endIndex - 1) <= last) {
                        tempList.add(originData);
                        index = endIndex;
                    }
                }
            }

            for (BaseCustomModel baseCustomModel : tempList) {
                baseCustomModel.realRequestData();
            }
        } else {
            setAdapterData();
        }
    }

    public void loadMore() {
        if (originDatas != null
                && originDatas.size() > 0
                && (originDatas.get(originDatas.size() - 1) instanceof BaseCustomListModel)) {
            originDatas.get(originDatas.size() - 1).realRequestData();
        } else {
            updateFooter(true);
        }
    }

    public void setAdapterData() {
        if (owner instanceof BaseRecyclerViewCallBack) {
            ((BaseRecyclerViewCallBack) owner).setData(realDatas, false);
        }
    }

    private void updateFooter(boolean isEnd) {
        if (owner instanceof BaseRecyclerViewCallBack) {
            ((BaseRecyclerViewCallBack) owner).isListFinish(isEnd);
        }
    }

    private void removeAnimation(ViewGroup group) {
        if (isAddAnimation && group instanceof RecyclerView) {
            isAddAnimation = false;
            ((RecyclerView) group).getItemAnimator().setAddDuration(0);
            ((RecyclerView) group).getItemAnimator().setChangeDuration(0);
            ((RecyclerView) group).getItemAnimator().setMoveDuration(0);
            ((RecyclerView) group).getItemAnimator().setRemoveDuration(0);
            ((SimpleItemAnimator) ((RecyclerView) group).getItemAnimator()).setSupportsChangeAnimations(false);
        }
    }
}
