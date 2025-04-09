package com.wallstreetcn.helper.utils.image;

import android.text.TextUtils;

import com.wallstreetcn.helper.utils.UriUtil;

public class ImageWebCacheHelper {

    public final static String TAG = "wscnimg";

    public static String TAG_NORMAL = "wscnimg=0";
    public static String TAG_FORCE  = "wscnimg=1";

    public static String getTagImageFormat(String image) {
        if (TextUtils.isEmpty(image))
            return image;
        return UriUtil.addParamsToUrl(image, TAG, "0");
    }

    public static String getCleanImageUrl(String image) {
        if (TextUtils.isEmpty(image))
            return image;
        image = image.replace(TAG_NORMAL, "");
        image = image.replace(TAG_FORCE, "");
        return image;
    }

    public static String getResizeImageUrl(String url, int width, int height) {
        url = getCleanImageUrl(url);
        return ImageUtlFormatHelper.formatImageWithThumbnailJpeg(url, width, height);
    }

}
