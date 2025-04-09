package com.wallstreetcn.helper.utils.image;

import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wallstreetcn.helper.utils.system.ScreenUtils;

/**
 * Created by yuweichen on 2016/10/28.
 */

public class ImageUtlFormatHelper {
    /**
     * ?imageView2/1/w/60/h/60
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static String formatImageFactory(String path, int width, int height) {
        try {
            Uri targetUri = Uri.parse(path);
            if(!checkWhiteList(targetUri.getHost()))
                return path;
            String scheme = targetUri.getScheme();
            String authority = targetUri.getAuthority();
            String targetPath = targetUri.getEncodedPath();
            if (TextUtils.isEmpty(targetPath)) {
                return "";
            }
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(scheme)
                    .authority(authority)
                    .appendEncodedPath(targetPath.substring(1) + "?imageView2/1/");
            if (width > 0) {
                builder.appendEncodedPath("w")
                        .appendEncodedPath(String.valueOf(width));
            }
            if (height > 0) {
                builder.appendEncodedPath("h")
                        .appendEncodedPath(String.valueOf(height));
            }
            builder.appendEncodedPath("format").appendEncodedPath("webp").appendEncodedPath("size-limit/15k!");
            return builder.build().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String[] whiteList = {"wallstcn.com", "wallstreetcn.com", "image.xuangubao.cn", "awtmt.com"};

    public static boolean checkWhiteList(String host) {
        for (String domain : whiteList) {
            if (host.contains(domain)) {
                return true;
            }
        }
        return false;
    }

    public static String formatImageWithThumbnail(String path, int width, int height) {
        try {
            if (TextUtils.isEmpty(path) || path.endsWith(".gif")) {
                return path;
            }
            path = path.trim();
            Uri targetUri = Uri.parse(path);
            String targetPath = targetUri.getEncodedPath();
            String host = targetUri.getHost();

            if (TextUtils.isEmpty(targetPath)) {
                return path;
            }
            if (!checkWhiteList(host)) {
                return path;
            }
            StringBuilder stringBuilder = new StringBuilder(path);
            if (path.contains("?")) {
                stringBuilder.append("|imageMogr2");
            } else {
                stringBuilder.append("?imageMogr2");
            }
            stringBuilder.append("/").append("auto-orient").append("/")
                    .append("thumbnail");
            String args = String.valueOf(width);
            args += height > 0 ? "x" + height : "";
            stringBuilder.append("/").append(args);
            stringBuilder.append("/");
            if (!path.contains("gif")) {
                stringBuilder.append("format").append("/").append("webp").append("/");
            }
            stringBuilder.append("size-limit/15k!");
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatImage(String path, ImageView imageView) {
        if (imageView == null) {
            return formatImageWithThumbnail(path, ScreenUtils.dip2px(300), 0);
        } else {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int width = params.width > 0 ? (int) (params.width * 1.5) : ScreenUtils.dip2px(300);
            return formatImageWithThumbnail(path, width, 0);
        }
    }


    /**
     * 利用七牛图床生成一个有blur效果的图片
     * 例:http://image.bao.wallstreetcn.com/Fu99NBLlEg3qY_u5tJIVywSiozTi?imageMogr2/thumbnail/640x480/blur/20x5
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static String formatImageBlur(String path, int width, int height) {
        return formatImageBlur(path, width, height, 20, 5);
    }


    /**
     * 利用七牛图床生成一个有blur效果的图片
     * 例:http://image.bao.wallstreetcn.com/Fu99NBLlEg3qY_u5tJIVywSiozTi?imageMogr2/thumbnail/640x480/blur/20x5
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static String formatImageBlur(String path, int width, int height, int radius, int sigma) {
        try {
            Uri targetUri = Uri.parse(path);
            if(!checkWhiteList(targetUri.getHost()) || path.endsWith(".gif"))
                return path;
            String scheme = targetUri.getScheme();
            String authority = targetUri.getAuthority();
            String targetPath = targetUri.getEncodedPath();
            if (TextUtils.isEmpty(targetPath)) {
                return "";
            }
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(scheme)
                    .authority(authority)
                    .appendEncodedPath(targetPath.substring(1) + "?imageMogr2")
                    .appendEncodedPath("auto-orient")
                    .appendEncodedPath("thumbnail");
            String args = String.valueOf(width);
            args += height > 0 ? "x" + height : "";
            builder.appendEncodedPath(args)
                    .appendEncodedPath("blur")
                    .appendEncodedPath(radius + "x" + sigma);
            if (!path.contains("gif")) {
                builder.appendEncodedPath("format").appendEncodedPath("webp");
            }
            builder.appendEncodedPath("size-limit/15k!");
            return builder.build().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatImageWithThumbnailJpeg(String path, int width, int height) {
        try {
            Uri targetUri = Uri.parse(path);
            if(!checkWhiteList(targetUri.getHost()) || path.endsWith(".gif"))
                return path;
            String scheme = targetUri.getScheme();
            String authority = targetUri.getAuthority();
            String targetPath = targetUri.getEncodedPath();
            if (TextUtils.isEmpty(targetPath)) {
                return "";
            }
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(scheme)
                    .authority(authority)
                    .appendEncodedPath(targetPath.substring(1) + "?imageMogr2")
                    .appendEncodedPath("auto-orient")
                    .appendEncodedPath("thumbnail");
            String args = String.valueOf(width);
            args += height > 0 ? "x" + height : "";
            builder.appendEncodedPath(args);
            builder.appendEncodedPath("format").appendEncodedPath("jpg").appendEncodedPath("size-limit/15k!");
            return builder.build().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean hasFormat(String image) {
        return image.contains("size-limit") || image.contains("imageView2");
    }
}
