package com.richzjc.dialoglib.anotation;

import android.view.Gravity;
import android.view.WindowManager;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对于styleName,在使用的时候最好是传，R.style.name这种格式，如果只传name值 反射拼接的全限定名为 packageName.R$style
 * 对于layoutName,在使用的时候最好是传，R.layout.name这种格式，如果只传name值 反射拼接的全限定名为 packageName.R$layout
 * 对于windowAnimationsName,在使用的时候最好是传，R.anim.name这种格式，如果只传name值 反射拼接的全限定名为 packageName.R$anim
 * 总之写在R.xxx.xxx,代码里面会去解析中间的一部份值
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogAnnotation {
    String styleName() default "BaseDefaultDialog";
    int gravity() default Gravity.CENTER;
    boolean cancelable() default true;
    int softInputMode() default WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
    int dialogWidth() default WindowManager.LayoutParams.WRAP_CONTENT;
    int dialogHeight() default WindowManager.LayoutParams.WRAP_CONTENT;
    int horMargin() default 0;
    String windowAnimationsName() default "";
    String nameSpace() default "";
}
