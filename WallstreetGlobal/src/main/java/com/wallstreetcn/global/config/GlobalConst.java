package com.wallstreetcn.global.config;

import android.view.View;

import com.wallstreetcn.helper.utils.system.EquipmentUtils;

import java.util.List;
import java.util.Map;

public class GlobalConst {
    public static Map<String, Map<String, List<String>>> publishSwitch;

    public static boolean checkHideComment(){
        String versionName = EquipmentUtils.getVersionName();
        String channel = EquipmentUtils.getChannel();
        if(publishSwitch != null && publishSwitch.get(versionName) != null){
            List<String> switchs = publishSwitch.get(versionName).get(channel);
            if(switchs != null){
                return switchs.contains("comment");
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public static void checkHideComment(View view){
        String versionName = EquipmentUtils.getVersionName();
        String channel = EquipmentUtils.getChannel();
        if(publishSwitch != null && publishSwitch.get(versionName) != null){
            List<String> switchs = publishSwitch.get(versionName).get(channel);
            if(view != null && switchs != null && switchs.contains("comment")){
                view.setVisibility(View.GONE);
            }
        }
    }

    public static void checkHideComment(View view, int visibility){
        String versionName = EquipmentUtils.getVersionName();
        String channel = EquipmentUtils.getChannel();
        if(publishSwitch != null && publishSwitch.get(versionName) != null){
            List<String> switchs = publishSwitch.get(versionName).get(channel);
            if(view != null && switchs != null && switchs.contains("comment")){
                view.setVisibility(visibility);
            }
        }
    }
}
