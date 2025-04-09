package com.wallstreetcn.helper.utils.data;


import java.lang.reflect.Method;

public class CrashReport {

    //避免导入包，利用反射处理
    public static void postCatchedException(Throwable throwable) {
        try {
            Class<?> clazz = Class.forName("com.tencent.bugly.crashreport.CrashReport");
            if (clazz != null) {
                Method postCatchedException = clazz.getMethod("postCatchedException", throwable.getClass());
                if (postCatchedException != null)
                    postCatchedException.invoke(null, throwable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
