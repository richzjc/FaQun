package com.wallstreetcn.global.activity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.interfaces.GestureView;
import com.wallstreetcn.global.utils.GestureSettingsSetupListener;

/**
 * Created by Leif Zhang on 16/8/15.
 * Email leifzhanggithub@gmail.com
 */
public class GestureSetup implements GestureSettingsSetupListener {
    private static final float OVERSCROLL = 32f;
    private boolean isPanEnabled = true;
    private boolean isZoomEnabled = true;
    private boolean isRotationEnabled = false;
    private boolean isRestrictRotation = false;
    private boolean isOverscrollXEnabled = false;
    private boolean isOverscrollYEnabled = false;
    private boolean isOverzoomEnabled = true;
    private boolean isFitViewport = true;
    private Settings.Fit fitMethod = Settings.Fit.INSIDE;
    private int gravity = Gravity.CENTER;

    @Override
    public void onSetupGestureView(GestureView view) {
        Context context = ((View) view).getContext();
        float overscrollX = isOverscrollXEnabled ? OVERSCROLL : 0f;
        float overscrollY = isOverscrollYEnabled ? OVERSCROLL : 0f;
        float overzoom = isOverzoomEnabled ? Settings.OVERZOOM_FACTOR : 1f;

        view.getController().getSettings()
                .setPanEnabled(isPanEnabled)
                .setZoomEnabled(isZoomEnabled)
                .setDoubleTapEnabled(isZoomEnabled)
                .setOverscrollDistance(context, overscrollX, overscrollY)
                .setOverzoomFactor(overzoom)
                .setRotationEnabled(isRotationEnabled)
                .setRestrictRotation(isRestrictRotation)
                .setFillViewport(isFitViewport)
                .setFitMethod(fitMethod)
                .setGravity(gravity);
    }
}
