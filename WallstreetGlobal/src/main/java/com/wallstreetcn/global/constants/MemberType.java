package com.wallstreetcn.global.constants;

import android.text.TextUtils;

import com.wallstreetcn.global.R;
import com.wallstreetcn.helper.utils.ResourceUtils;

public class MemberType {

    public final static String BLUE = "blue";//3
    public final static String GOLD = "gold";//3
    public final static String PLATINUM = "platinum";//30

    public final static String MASTER_COURSE = "master_course";//30
    public final static String DEHYDRATION = "dehydration";//30
    public final static String CENTURION = "centurion";//40
    public final static String CLUB = "platinum,master_course,dehydration";//300

    public static int getVipWeight(String type) {
        if (TextUtils.isEmpty(type))
            return 0;
        if (TextUtils.equals(BLUE, type)) {
            return 3;
        } else if (TextUtils.equals(GOLD, type))
            return 3;
        else if (CLUB.contains(type))
            return 30;
        return 0;
    }

    public static String getVipText(String vip_type) {
        if (TextUtils.equals(vip_type, "master_course")) {
            return ResourceUtils.getResStringFromId(R.string.premium_master_course_club);
        } else if (TextUtils.equals(vip_type, "gold")) {
            return ResourceUtils.getResStringFromId(R.string.premium_dehydration_gold);
        } else if (TextUtils.equals(vip_type, "platinum")) {
            return ResourceUtils.getResStringFromId(R.string.premium_platinum_club);
        } else if (TextUtils.equals(vip_type, "black")) {
            return ResourceUtils.getResStringFromId(R.string.premium_black_for_free);
        } else if (TextUtils.equals(vip_type, "dehydration")) {
            return ResourceUtils.getResStringFromId(com.wallstreetcn.global.R.string.premium_dehydration_club);
        } else return "";
    }
}
