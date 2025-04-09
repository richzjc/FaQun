package com.richzjc.dialoglib.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;

import com.richzjc.dialoglib.callback.ICreateViewInterface;
import com.richzjc.dialoglib.model.DialogAnnotationModel;

import java.lang.reflect.Field;

/**
 * Created by Spark on 2016/7/4 16:28.
 */
public abstract class BaseDialogFragment extends DialogFragment implements ICreateViewInterface {

    private DialogAnnotationModel model;
    private Field mCalledField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new DialogAnnotationModel(this);
        setStyle(DialogFragment.STYLE_NORMAL, model.getStyle());

        Field[] fields = Fragment.class.getDeclaredFields();
        for (Field field : fields) {
            if (TextUtils.equals(field.getName(), "mCalled")) {
                mCalledField = field;
                mCalledField.setAccessible(true);
                return;
            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setGravity(model.getGravity());
        dialog.getWindow().setSoftInputMode(model.getSoftInputMode());

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.windowAnimations = model.getWindowAnimations();
        window.setAttributes(lp);

        dialog.setCancelable(model.isCancelable());
        dialog.setCanceledOnTouchOutside(model.isCancelable());
        return dialog;
    }

    public int getGravity() {
        return Gravity.CENTER;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {
        try {
            if (mCalledField != null) {
                mCalledField.set(this, true);
            }

            Activity activity = getActivity();
            if (activity != null && activity instanceof ComponentActivity) {
                final LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) ((ComponentActivity) activity).getLifecycle();
                Lifecycle.State state = lifecycleRegistry.getCurrentState();
                if (state == Lifecycle.State.DESTROYED || activity.isDestroyed() || activity.isFinishing())
                    return;

                else if (state == Lifecycle.State.RESUMED) {
                    realStart();
                } else if (lifecycleRegistry instanceof BaseLifecycleRegistry) {
                    if (((BaseLifecycleRegistry) lifecycleRegistry).smallerEvent(Lifecycle.Event.ON_DESTROY))
                        realStart();
                } else {
                    realStart();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void realStart() {
        super.onStart();
        getDialog().getWindow().setLayout(model.getDialogWidth(), model.getDialogHeight());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = doGetContentView(container, inflater);
        doBefore(savedInstanceState);
        doInitSubViews(containerView);
        setClickListener();
        return containerView;
    }

    public abstract View doGetContentView(ViewGroup container, LayoutInflater inflater);

    public abstract void destroyViewBinding();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doInitData();
        doAfter();
    }

    public void setClickListener(){

    }


    public int getStyle() {
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       destroyViewBinding();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }


    @Override
    public void doBefore(Bundle savedInstanceState) {

    }

    @Override
    public ViewGroup doViewGroupRoot() {
        return null;
    }

    @Override
    public void doInitSubViews(View view) {

    }

    @Override
    public abstract void doInitData();

    @Override
    public void doAfter() {

    }

    public WindowManager.LayoutParams getLayoutParams() {
        return getDialog().getWindow().getAttributes();
    }

    public int getDialogWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public int getDialogHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void show(final FragmentManager manager, final String tag) {
        try {
            Context context = getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (!activity.isDestroyed() && !activity.isFinishing())
                    super.show(manager, tag);
            } else {
                super.show(manager, tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}