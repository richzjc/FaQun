package com.wallstreetcn.helper.utils.text;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Leif Zhang on 16/8/16.
 * Email leifzhanggithub@gmail.com
 */
public class CheckJsonUtils {
    public static boolean checkJsonObject(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
