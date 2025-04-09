package com.wallstreetcn.baseui.base;

import static com.wallstreetcn.baseui.manager.ResourceConfigManager.NIGHT_SP_KEY;
import static com.wallstreetcn.baseui.manager.ResourceConfigManager.TYPE_NIGHT;
import static com.wallstreetcn.baseui.manager.ResourceConfigManager.TYPE_NORMAL;
import static com.wallstreetcn.baseui.manager.ResourceConfigManager.TYPE_SYSTEM;
import static java.lang.Math.max;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.richzjc.anotation_api.manager.ParameterManager;
import com.richzjc.dialog_view.IAnimView;
import com.richzjc.dialoglib.base.BaseDialogFragment;
import com.richzjc.dialoglib.base.BaseLifecycleRegistry;
import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.callback.IBackPress;
import com.wallstreetcn.baseui.callback.IRegistGetAnimView;
import com.wallstreetcn.baseui.dialog.LoadingDialogFragment;
import com.wallstreetcn.baseui.internal.ViewQuery;
import com.wallstreetcn.baseui.manager.AppManager;
import com.wallstreetcn.baseui.manager.ResourceConfigManager;
import com.wallstreetcn.baseui.manager.ViewManager;
import com.wallstreetcn.baseui.proxy.BackPressProxy;
import com.wallstreetcn.baseui.skin.SkinActivity;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.SharedPrefsUtil;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.data.CrashReport;
import com.wallstreetcn.helper.utils.data.TraceUtils;
import com.wallstreetcn.helper.utils.text.TextUtil;
import com.yangl.swipeback.ui.SwipeBackLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by micker on 16/6/16.
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends SkinActivity implements
        ICreateViewInterface, View.OnClickListener, IBackPress, IRegistGetAnimView {

    public ViewQuery mViewQuery = new ViewQuery();
    protected IViewLifeCircleInterface mILifeCircle;
    protected T mPresenter;
    private SwipeBackLayout mSwipeBackLayout;
    protected ResourceConfigManager dayNightModeManager;
    private BaseDialogFragment dialogLoading;
    protected ViewManager viewManager;
    public boolean isImmersion;
    private List<IAnimView> animViews;
    private IBackPress backPress;
    public static int navigationbarheight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        replaceLifecycleRegistry();
        ParameterManager.getInstance().loadParameter(this);
        doBefore(savedInstanceState);
        dayNightModeManager = new ResourceConfigManager(this);
        super.onCreate(savedInstanceState);

        if (needCheckLightStatusBar()) {
            checkLightStatusBar();
        }
        doInitCreate();
        hideNavigationBar();
        forNavigation();
        setupWindowAnimations();
    }

    protected void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !needHideNavigation()) {
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null)
                insetsController.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        }

        View view = getRealContentView();
        if (view.getParent() instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view.getParent();
            group.setBackgroundColor(ResourceUtils.getColor(R.color.day_mode_background_color1_ffffff));
        }


        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                try {
                    navigationbarheight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
                    int softHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
                    View view = getRealContentView();
                    if (view.getParent() instanceof ViewGroup) {
                        if (needHideNavigation()) {
                            ((ViewGroup) view.getParent()).setPadding(0, 0, 0, max(navigationbarheight, softHeight));
                        }else{
                            ((ViewGroup) view.getParent()).setPadding(0, 0, 0, 0);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return insets;
            }
        });

    }

    public boolean needHideNavigation() {
        return true;
    }

    @Override
    protected void installSwipeLayout() {
        if (!TextUtils.equals("MainActivity", getClass().getSimpleName())) {
            super.installSwipeLayout();
        }
    }

    public View findViewById(int id) {
        return getWindow().getDecorView().findViewById(id);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        initNightMode(SharedPrefsUtil.getInt(NIGHT_SP_KEY, TYPE_SYSTEM), true);
        super.attachBaseContext(newBase);
    }

    protected void initNightMode(int mode, boolean recreate) {
        Lifecycle.State state = getLifecycle().getCurrentState();
        if (Lifecycle.State.RESUMED == state || recreate) {
            if (mode == TYPE_NIGHT) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (mode == TYPE_NORMAL) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        } else {
            getLifecycle().addObserver(new LifecycleObserver() {
                @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
                public void onResume() {
                    getLifecycle().removeObserver(this);
                    recreate();
                }
            });
        }

    }

    private void replaceLifecycleRegistry() {
        try {
            Class cls = Class.forName("androidx.activity.ComponentActivity");
            Field[] fields = cls.getDeclaredFields();
            Field field = null;
            for (Field f : fields) {
                if (TextUtils.equals(f.getName(), "mLifecycleRegistry")) {
                    field = f;
                    break;
                }
            }
            if (field != null) {
                field.setAccessible(true);
                field.set(this, new BaseLifecycleRegistry(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void recreate() {
        TLog.e("recreate", getClass().getSimpleName());
        try {
            dismissDialog();
            FragmentManager fm = getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    fm.beginTransaction().remove(fragment).commit();
                    fm.popBackStack();
                }
            }
            super.recreate();
        } catch (Exception e) {
            TLog.e("recreate", "BaseActivity: " + e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    public final void setStatusBarTranslucentCompat() {
        isImmersion = true;
        int DefaultLightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF);
        int DefaultDarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b);
        EdgeToEdge.enable(this, SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT), SystemBarStyle.auto(DefaultLightScrim, DefaultDarkScrim));
    }

    protected void setupWindowAnimations() {
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(500);
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    protected void doInitCreate() {
        AppManager.getAppManager().addActivity(this);
        viewManager = new ViewManager(this, doGetContentView());
        setContentView(getRealContentView());
        mSwipeBackLayout = getSwipeLayout();
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setSwipeGestureEnable(isSupportSwipeBack());
            mSwipeBackLayout.setSwipeSensitivity(0.8f);
            mSwipeBackLayout.setSwipeScrimColor(Color.argb(153, 0, 0, 0));
            mSwipeBackLayout.setSwipeSpeed(250);
            mSwipeBackLayout.needSwipeShadow(true);
        }

        doInitPresenter();
        __internal();
        doInitSubViews(getRealContentView());
        doInitData();
        doAfter();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        AppManager.getAppManager().addActivity(this);
    }

    protected View getRealContentView() {
        return viewManager.showContentView();
    }

    private void doInitPresenter() {
        mPresenter = doGetPresenter();
        if (mPresenter != null) {
            mPresenter.attachViewRef((V) this);
        }
    }

    protected T doGetPresenter() {
        return null;
    }

    private void __internal() {
        mViewQuery.setActivity(this).setClickListener(this);
        if (null != mILifeCircle)
            mILifeCircle.onCreate();
    }

    /**
     * 如果需要对Activity的生命周期做特殊处理，如统计，则在此设置mILifeCircle
     *
     * @param savedInstanceState
     */
    @Override
    public void doBefore(Bundle savedInstanceState) {

    }

    public abstract View doGetContentView();

    @Override
    public ViewGroup doViewGroupRoot() {
        return null;
    }

    @Override
    public void doInitSubViews(View view) {

    }

    @Override
    public void doInitData() {

    }

    @Override
    public void doAfter() {

    }

    @Override
    protected void onStart() {
        try {
            super.onStart();

            if (null != mILifeCircle) {
                mILifeCircle.onStart();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CrashReport.postCatchedException(e);
        }
    }


    @Override
    protected void onResume() {
        try {
            super.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TraceUtils.onPageStart(getLocalClassName());
        TraceUtils.onResume(this);
        if (null != mILifeCircle) {
            mILifeCircle.onResume();
        }
        if (mPresenter != null) {
            if (mPresenter.isViewRefAttached()) {
                mPresenter.attachViewRef((V) this);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        TraceUtils.onPageEnd(getLocalClassName());
        TraceUtils.onPause(this);
        if (null != mILifeCircle) {
            mILifeCircle.onStart();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (null != mILifeCircle) {
            mILifeCircle.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
        if (mPresenter != null)
            mPresenter.detachViewRef();
        if (null != mILifeCircle)
            mILifeCircle.onDestroy();
    }

    public void setmILifeCircle(IViewLifeCircleInterface mILifeCircle) {
        this.mILifeCircle = mILifeCircle;
    }

    @Override
    public void onClick(View view) {

    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (getApplicationInfo().targetSdkVersion
                >= Build.VERSION_CODES.ECLAIR) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                    && !event.isCanceled()) {
                if (backPress != null)
                    backPress.onBackPressed();
                else
                    onBackPressed();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        try {
            BaseFragment baseFragment = getFragment();
            if (baseFragment == null || !baseFragment.onBackPressed())
                super.onBackPressed();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public BaseFragment getFragment() {
        return null;
    }


    public final void toggleNightMode(int mode) {
        initNightMode(mode, false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void showDialog() {
        try {
            dismissDialog();
            dialogLoading = getDialogLoading();
            if (!dialogLoading.isAdded()) {
                dialogLoading.show(getSupportFragmentManager(), "loadingDialog");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (dialogLoading != null)
                dialogLoading.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BaseDialogFragment getDialogLoading() {
        return new LoadingDialogFragment();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void regist(IAnimView iAnimView) {
        if (animViews == null) {
            animViews = new ArrayList<>();
            backPress = new BackPressProxy().proxy(this, this);
        }
        if (!animViews.contains(iAnimView))
            animViews.add(iAnimView);
    }

    @Override
    public List<IAnimView> getAnimViews() {
        return animViews;
    }

    public boolean isSupportSwipeBack() {
        return true;
    }

    public void setSwipeBackEnable(boolean canSwipeBack) {
        if (mSwipeBackLayout != null)
            mSwipeBackLayout.setSwipeGestureEnable(canSwipeBack);
    }


}
