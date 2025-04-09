package com.wallstreetcn.helper.utils;

import android.os.Bundle;
import android.text.TextUtils;

public class BundleUtil {
    public static boolean checkBundleEqual(Bundle bundle1, Bundle bundle2) {
        if (bundle1 == null || bundle2 == null)
            return false;
        else {
            boolean flag = true;
            Bundle newBundle = new Bundle();
            newBundle.putAll(bundle2);

            for (String key : bundle1.keySet()) {
                if (!newBundle.containsKey(key) || !TextUtils.equals(bundle1.getString(key), newBundle.getString(key))) {
                    flag = false;
                    return flag;
                }
                newBundle.remove(key);
            }

            if (newBundle.keySet().size() > 0) {
                flag = false;
            }

            return flag;
        }
    }
}
