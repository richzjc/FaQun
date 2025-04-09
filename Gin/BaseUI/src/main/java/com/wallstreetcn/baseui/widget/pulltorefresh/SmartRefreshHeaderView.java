package com.wallstreetcn.baseui.widget.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.wallstreetcn.baseui.R;
import com.wallstreetcn.helper.utils.image.ImageUtlFormatHelper;
import com.wallstreetcn.helper.utils.system.ScreenUtils;
import com.wallstreetcn.imageloader.ImageLoadManager;
import com.wallstreetcn.imageloader.WscnImageView;

/**
 * Created by ichongliang on 05/03/2018.
 */

public class SmartRefreshHeaderView extends RelativeLayout implements RefreshHeader {

    protected SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;
    public float dragRate = 1f;

    public SmartRefreshHeaderView(Context context) {
        super(context);
        init(context, null);
    }

    public SmartRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SmartRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    //	protected final ImageView mHeaderImage;
    public SimpleDraweeView mIvRefresh;
    private TextView mHeaderText;
    private WscnImageView adImageView;
    private View loadViewParent;
    public int maxDragHeight = ScreenUtils.dip2px(150f);

    private void init(Context context, AttributeSet attrs) {
        addAdIV();
        addLoadView();
        mHeaderText = findViewById(R.id.pull_to_refresh_text);
        mIvRefresh = findViewById(R.id.iv_refresh);
        adImageView.getHierarchy().setPlaceholderImage(new ColorDrawable(Color.TRANSPARENT));

//        ImageLoadManager.loadImage(R.drawable.base_ptr_refresh, mIvRefresh, 0);

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.base_ptr_refresh))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(false)
                .build();
        mIvRefresh.setController(controller);

        setImageLayoutParams(PTRHeaderConfig.scale);
        loadImage(PTRHeaderConfig.REFRESH_ICON);
        setPullStr(PTRHeaderConfig.REFRESH_PULL_TEXT);
        setLoadingStr(PTRHeaderConfig.REFRESH_REFRESHING_TEXT);
    }

    private void addLoadView() {
        loadViewParent = LayoutInflater.from(getContext()).inflate(R.layout.base_refresh_header_vertical, this, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(loadViewParent, params);
    }

    private void addAdIV() {
        adImageView = new WscnImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(adImageView, params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int loadViewParentHeight = loadViewParent.getMeasuredHeight();
        int totalHeight = getHeight();
        if (totalHeight > maxDragHeight) {
            dragRate = (maxDragHeight * 1.0f) / totalHeight;
            int gap = (maxDragHeight - loadViewParentHeight) / 2;
            if (gap > 0) {
                loadViewParent.layout(0, totalHeight - gap - loadViewParentHeight, getWidth(), totalHeight - gap);
            } else {
                loadViewParent.layout(0, totalHeight - loadViewParentHeight, getWidth(), totalHeight);
            }
        } else {
            dragRate = 1f;
            int gap = (totalHeight - loadViewParentHeight) / 2;
            if (gap > 0) {
                loadViewParent.layout(0, totalHeight - gap - loadViewParentHeight, getWidth(), totalHeight - gap);
            } else {
                loadViewParent.layout(0, totalHeight - loadViewParentHeight, getWidth(), totalHeight);
            }
        }
    }


    public void setImageLayoutParams(float scale) {
        if (scale < 0.3) {
            return;
        }
        //  scale = scale > 0.3f ? 0.3f : scale;
        ViewGroup.LayoutParams params = adImageView.getLayoutParams();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        params.width = dm.widthPixels;
        params.height = (int) (dm.widthPixels / scale);
        adImageView.setLayoutParams(params);
    }


    public void loadImage(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        String imgUrl = ImageUtlFormatHelper.formatImageWithThumbnail(imageUrl, 640, 0);
        ImageLoadManager.loadImage(imgUrl, adImageView, 0);
    }

    private String pullStr;
    private String loadingStr;

    public void setPullStr(String pullStr) {
        this.pullStr = pullStr;
    }

    public void setLoadingStr(String loadingStr) {
        this.loadingStr = loadingStr;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPrimaryColors(int... colors) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
        //Log.i("tag@@", "SmartRefreshHeaderView onInitialized~ height:" + height + "   extendHeight:" + extendHeight);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int extendHeight) {
        if (Math.abs(offset) > 0) {
            DraweeController controller = mIvRefresh.getController();
            if (controller != null) {
                Animatable animatable = controller.getAnimatable();
                if (animatable != null && !animatable.isRunning()) {
                    animatable.start();
                    //Log.w("tag@@", "SmartRefreshHeaderView onMoving~   animatable.start();   height:" + height + "   extendHeight:" + extendHeight + "  percent:" + percent + "  offset:" + offset);
                }
            }
        } else {
            DraweeController controller = mIvRefresh.getController();
            if (controller != null) {
                Animatable animatable = controller.getAnimatable();
                if (animatable != null && animatable.isRunning()) {
                    animatable.stop();
                    //Log.w("tag@@", "SmartRefreshHeaderView onMoving~   animatable.stop();   height:" + height + "   extendHeight:" + extendHeight + "  percent:" + percent + "  offset:" + offset);
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {
        //Log.i("tag@@", "SmartRefreshHeaderView onReleased~ height:" + height + "   extendHeight:" + extendHeight);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {
        //Log.i("tag@@", "smartRefreshHeaderView onStartAnimator ~~");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        //Log.i("tag@@", "smartRefreshHeaderView onFinish ~~");
        return 0;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
//        Log.d("tag@@", "oldState: " + oldState + "   newState: " + newState);
//        if (newState.isFinishing
//                || newState == RefreshState.PullDownCanceled
//                || newState == RefreshState.PullUpCanceled) {
//            DraweeController controller = mIvRefresh.getController();
//            if (controller != null) {
//                Animatable animatable = controller.getAnimatable();
//                if (animatable != null && animatable.isRunning()) {
//                    animatable.stop();
//                }
//            }
//        } else if (newState.isDragging || newState.isOpening) {
//            DraweeController controller = mIvRefresh.getController();
//            if (controller != null) {
//                Animatable animatable = controller.getAnimatable();
//                if (animatable != null && !animatable.isRunning()) {
//                    animatable.start();
//                }
//            }
//        }
    }


}
