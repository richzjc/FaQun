package com.wallstreetcn.helper.utils.socket;

import android.text.TextUtils;

import com.wallstreetcn.helper.utils.TLog;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import okhttp3.WebSocket;

public class SocketUtil {

    public static void startSocketProxy(WebSocket websocket, OnReadPingCallback pingCallback) {
        try {
            Class cls = Class.forName("okhttp3.internal.ws.RealWebSocket");
            Field[] fields = cls.getDeclaredFields();
            Field radarField = null;
            for (Field field : fields) {
                if (TextUtils.equals(field.getName(), "reader")) {
                    radarField = field;
                    break;
                }
            }

            if (radarField == null)
                return;

            radarField.setAccessible(true);

            Object radarInstance = radarField.get(websocket);


            Class radarClass = Class.forName("okhttp3.internal.ws.WebSocketReader");
            Field[] radarFields = radarClass.getDeclaredFields();
            Field callbackField = null;
            for (Field field : radarFields) {
                if (TextUtils.equals(field.getName(), "frameCallback")) {
                    callbackField = field;
                    break;
                }
            }

            if (callbackField == null)
                return;

            callbackField.setAccessible(true);
            Class[] clsArr = new Class[1];
            clsArr[0] = Class.forName("okhttp3.internal.ws.WebSocketReader$FrameCallback");
            Object callback = Proxy.newProxyInstance(websocket.getClass().getClassLoader(), clsArr, (proxy, method, args) -> {
                TLog.i("live", "methodName = " + method.getName() + "; args = " + args);
                        if (TextUtils.equals(method.getName(), "onReadPing")) {
                            pingCallback.onReadping();
                        }
                        return method.invoke(websocket, args);
                    }
            );

            callbackField.set(radarInstance, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnReadPingCallback {
        void onReadping();
    }
}
