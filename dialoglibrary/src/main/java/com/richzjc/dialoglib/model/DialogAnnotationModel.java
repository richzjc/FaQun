package com.richzjc.dialoglib.model;


import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;

import com.richzjc.dialoglib.R;
import com.richzjc.dialoglib.anotation.DialogAnnotation;
import com.richzjc.dialoglib.base.BaseDialogFragment;
import com.richzjc.dialoglib.util.DialogReflectUtil;
import com.richzjc.dialoglib.util.ScreenUtil;

public class DialogAnnotationModel {

    private DialogAnnotation annotation;
    private BaseDialogFragment dialogFragment;

    public DialogAnnotationModel(BaseDialogFragment dialogFragment){
        annotation = dialogFragment.getClass().getAnnotation(DialogAnnotation.class);
        this.dialogFragment = dialogFragment;
    }

    public int getStyle(){
        if(annotation != null)
            return DialogReflectUtil.getId("style", annotation.styleName(), annotation.nameSpace());
        return dialogFragment.getStyle();
    }

    public int getGravity(){
        if(annotation != null)
            return annotation.gravity();
        return dialogFragment.getGravity();
    }

    public boolean isCancelable(){
        if(annotation != null)
            return annotation.cancelable();
        return true;
    }

    public int getSoftInputMode(){
        if(annotation != null)
            return annotation.softInputMode();
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
    }

    public int getDialogWidth(){
        if(annotation != null) {
            if(annotation.dialogWidth() == WindowManager.LayoutParams.MATCH_PARENT  &&  annotation.horMargin() > 0){
                return ScreenUtil.getScreenWidth(dialogFragment.getContext()) - ScreenUtil.dip2px(annotation.horMargin(), dialogFragment.getContext());
            }else {
                return annotation.dialogWidth();
            }
        }
        return dialogFragment.getDialogWidth();
    }

    public int getDialogHeight(){
        if(annotation != null)
            return annotation.dialogHeight();
        return dialogFragment.getDialogHeight();
    }

    public int getWindowAnimations(){
        if(annotation != null && !TextUtils.isEmpty(annotation.windowAnimationsName()))
            return DialogReflectUtil.getId("anim", annotation.windowAnimationsName(), annotation.nameSpace());
        else{
           return getAnimationId();
        }
    }

    private int getAnimationId(){
        int gravity = getGravity();
        if(gravity == Gravity.BOTTOM)
            return R.style.anim_menu_bottombar;
        else if(gravity == Gravity.CENTER)
            return R.style.anim_menu_center_scale;
        else return R.style.anim_menu_bottombar;
    }
}
