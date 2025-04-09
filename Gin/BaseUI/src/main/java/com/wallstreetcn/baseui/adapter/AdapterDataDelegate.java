package com.wallstreetcn.baseui.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import com.wallstreetcn.baseui.model.SkeletonEntity;
import com.wallstreetcn.helper.utils.TLog;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Leif Zhang on 2017/8/23.
 * Email leifzhanggithub@gmail.com
 */

class AdapterDataDelegate<D> {

    private final String TAG = "AdapterDataDelegate";
    private List<Object> mData;
    private List<D> mDataCursor;
    private List<SkeletonEntity> skeletonList;
    private BaseRecycleAdapter<D, ?> mAdapter;
    private int skeletonCount = 10;
    private boolean isNeedSkeleton = false;
    private boolean diffDetectMoves = false;


    AdapterDataDelegate(BaseRecycleAdapter<D, ?> adapter) {
        this.mAdapter = adapter;
    }

    public void setData(List<D> data) {
        mDataCursor = data;
        copyData();
        mAdapter.notifyDataSetChanged();
    }

    public void setDataNew(List<D> data) {
        TLog.i(TAG + "  notifyDataSetChanged: data.size() = " + (data == null ? 0 : data.size()));
        boolean isNeedNotifyDataSetChange;
        if (mDataCursor != null && data != null) {
            mDataCursor = data;
            isNeedNotifyDataSetChange = AdapterDiffUtil.calculateDiff(new DiffCallBack(mData, mDataCursor));
            if (isNeedNotifyDataSetChange)
                copyData();
        } else {
            mDataCursor = data;
            copyData();
            isNeedNotifyDataSetChange = true;
        }

        if (isNeedNotifyDataSetChange) {
            mAdapter.notifyDataSetChanged();
        } else {
            notifyItemChanged();
        }
    }


    public void onlySetData(List<D> data) {
        mDataCursor = data;
        copyData();
    }

    private void copyData() {
        try {
            if (mDataCursor != null && !mDataCursor.isEmpty()) {
                if (mDataCursor.get(0) instanceof Parcelable) {
                    mData = new ArrayList<>();
                } else {
                    mData = new ArrayList<>();
                    mData.addAll(mDataCursor);
                    return;
                }
            } else {
                mData = new ArrayList<>();
                return;
            }
            for (D entity : mDataCursor) {
                Parcel parcel = Parcel.obtain();
                if (entity instanceof Parcelable) {
                    ((Parcelable) entity).writeToParcel(parcel, 0);
                    parcel.setDataPosition(0);
                    Constructor<?> constructor = entity.getClass().getDeclaredConstructor(Parcel.class);
                    constructor.setAccessible(true);
                    D dateEntity = (D) constructor.newInstance(parcel);
                    mData.add(dateEntity);
                    parcel.recycle();
                } else {
                    mData.add(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addData(int pos) {
        try {
            if (mDataCursor.get(pos) instanceof Parcelable) {
                Parcelable parcelable = (Parcelable) mDataCursor.get(pos);
                Parcel parcel = Parcel.obtain();
                parcelable.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);
                Constructor<?> constructor = parcelable.getClass().getDeclaredConstructor(Parcel.class);
                constructor.setAccessible(true);
                D dateEntity = (D) constructor.newInstance(parcel);
                mData.add(pos, dateEntity);
                parcel.recycle();
            } else {
                mData.add(pos, mDataCursor.get(pos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeData(int pos) {
        try {
            if (mDataCursor.get(pos) instanceof Parcelable) {
                Parcelable parcelable = (Parcelable) mDataCursor.get(pos);
                Parcel parcel = Parcel.obtain();
                parcelable.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);
                Constructor<?> constructor = parcelable.getClass().getDeclaredConstructor(Parcel.class);
                constructor.setAccessible(true);
                D dateEntity = (D) constructor.newInstance(parcel);
                mData.remove(pos);
                mData.add(pos, dateEntity);
                parcel.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeData(int pos) {
        mData.remove(pos);
    }

    private int notifyCount;

    public void notifyItemChanged() {
        try {
            if (notifyCount == 0 && mData != null) {
                notifyCount++;
                diffUtils();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void diffUtils() {
        TLog.i(TAG + "  diffUtils：" + notifyCount);
        if (mDataCursor == null) {
            mDataCursor = new ArrayList<>();
        }
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mData, mDataCursor), diffDetectMoves);
        diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                TLog.i(TAG + "  onInserted : start = " + (position + mAdapter.getHeadViewSize()) + " ; count = " + count + "; itemCount = " + mAdapter.getItemCount() + "  " + mAdapter.getClass().getSimpleName());
                for (int i = 0; i < count; i++) {
                    if (notifyCount > 1)
                        break;
                    addData(position + i);
                }
                if (notifyCount <= 1)
                    mAdapter.notifyItemRangeInserted(position + mAdapter.getHeadViewSize(), count);
            }

            @Override
            public void onRemoved(int position, int count) {
                for (int i = 0; i < count; i++) {
                    if (notifyCount > 1)
                        break;
                    removeData(position);
                }
                TLog.i(TAG + "  onRemove : start = " + (position + mAdapter.getHeadViewSize()) + " ; count = " + count + "; itemCount = " + mAdapter.getItemCount() + "  " + mAdapter.getClass().getSimpleName());
                if (notifyCount <= 1)
                    mAdapter.notifyItemRangeRemoved(position + mAdapter.getHeadViewSize(), count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                copyData();
                if (notifyCount <= 1) {
                    TLog.i(TAG + "  onMoved : start = " + (fromPosition + mAdapter.getHeadViewSize()) + " ; toPosition = " + (toPosition + mAdapter.getHeadViewSize()) + "; itemCount = " + mAdapter.getItemCount() + "  " + mAdapter.getClass().getSimpleName());
                    mAdapter.notifyItemMoved(fromPosition + mAdapter.getHeadViewSize(), toPosition + mAdapter.getHeadViewSize());
                    mAdapter.notifyItemRangeChanged(Math.min(fromPosition, toPosition) + mAdapter.getHeadViewSize(), Math.abs(toPosition - fromPosition) + 1);
                }
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                for (int i = 0; i < count; i++) {
                    if (notifyCount > 1)
                        break;
                    changeData(position + i);
                }

                if (notifyCount <= 1) {
                    TLog.i(TAG + "  onChanged : start = " + (position + mAdapter.getHeadViewSize()) + " ; count = " + count + "; itemCount = " + mAdapter.getItemCount() + "  " + mAdapter.getClass().getSimpleName());
                    mAdapter.notifyItemRangeChanged(position + mAdapter.getHeadViewSize(), count, payload);
                }
            }

        });
        TLog.i(TAG + "  notifyCount --:" + notifyCount);
        notifyCount--;
        if (notifyCount > 0)
            diffUtils();
    }

    public void onItemMove(int lastPos, int nowPos) {
        copyData();
        mAdapter.notifyItemMoved(lastPos, nowPos);
        mAdapter.notifyItemChanged(nowPos);
        mAdapter.notifyItemChanged(lastPos);
    }


    public D getData(int pos) {
        return isEmpty() || mDataCursor.size() <= pos || pos < 0 ? null : mDataCursor.get(pos);
    }


    public boolean isEmpty() {
        return mDataCursor == null;
    }

    public boolean isListEmpty() {
        return mDataCursor != null && mDataCursor.isEmpty();
    }

    public int getListSize() {
        if (isNeedSkeleton) {
            if (skeletonList == null) {
                skeletonList = SkeletonEntity.skeletonList(skeletonCount);
            }
            return isEmpty() ? skeletonList.size() : mDataCursor.size();
        } else {
            return isEmpty() ? 0 : mDataCursor.size();
        }
    }

    public SkeletonEntity getSkeletonEntity(int pos) {
        return skeletonList != null && skeletonList.size() > pos ? skeletonList.get(pos) : null;
    }

    public void setNeedSkeleton(boolean needSkeleton) {
        isNeedSkeleton = needSkeleton;
    }

    public void setSkeletonCount(int skeletonCount) {
        this.skeletonCount = skeletonCount;
    }

    public List<D> getData() {
        return mDataCursor;
    }

    public void clear() {
        mData = null;
        mDataCursor = null;
    }

    public void removeItem(D item) {
        mDataCursor.remove(item);
        notifyItemChanged();
    }

    public void diffDetectMoves(boolean diffDetectMoves) {
        this.diffDetectMoves = diffDetectMoves;
    }

}
