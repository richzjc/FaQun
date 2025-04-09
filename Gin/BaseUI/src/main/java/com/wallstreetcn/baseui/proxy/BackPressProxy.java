package com.wallstreetcn.baseui.proxy;

import android.view.View;

import com.richzjc.dialog_view.IAnimView;
import com.wallstreetcn.baseui.callback.IBackPress;
import com.wallstreetcn.baseui.callback.IRegistGetAnimView;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BackPressProxy {
    public IBackPress proxy(IRegistGetAnimView animViews, IBackPress backPress) {
        return (IBackPress) Proxy.newProxyInstance(backPress.getClass().getClassLoader(), new Class[]{IBackPress.class}, new BackPressInvocation(animViews, backPress));
    }

    private class BackPressInvocation implements InvocationHandler {

        private IBackPress backPress;
        private IRegistGetAnimView animViews;

        public BackPressInvocation(IRegistGetAnimView animViews, IBackPress backPress) {
            this.backPress = backPress;
            this.animViews = animViews;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            IAnimView animView = null;
            if (animViews.getAnimViews() != null && animViews.getAnimViews().size() > 0) {
                int length = animViews.getAnimViews().size();
                IAnimView view;
                for (int i = length - 1; i >= 0; i--) {
                    view = animViews.getAnimViews().get(i);
                    if(view instanceof View && ((View) view).getVisibility() == View.VISIBLE  && ((View) view).getParent() != null){
                        animView = view;
                    }
                }
            }
            if (animView != null) {
                animView.endAnim();
                return null;
            } else {
                return method.invoke(backPress, args);
            }
        }
    }
}
