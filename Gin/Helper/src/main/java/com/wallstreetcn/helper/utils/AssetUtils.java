package com.wallstreetcn.helper.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.file.FileUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

public class AssetUtils {
    public static String replaceAppName(String  path) {
        String originPath = "file:///android_asset/" + path;
        String returnPath = originPath;
        try {
            int BUFFER_SIZE = 4096;
            AssetManager manager = UtilsContextManager.getInstance().getApplication().getAssets();
            InputStream inputStream = manager.open(path);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int count = -1;
            while ((count = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
            Context context = UtilsContextManager.getInstance().getApplication();
            String value = new String(outStream.toByteArray(), Charset.forName("UTF-8"));
            String replaceZh = context.getString(R.string.helper_main_app_name_zh);
            String replaceZhTw = context.getString(R.string.helper_main_app_name_zh_rtw);
            String appName = ResourceUtils.getAppName();
            value = value.replaceAll(replaceZh, appName);
            value = value.replaceAll(replaceZhTw, appName);
            String filePath = FileUtil.getCacheParentFile() + File.separator + path;
            if(new File(filePath).exists())
                FileUtil.deleteFile(filePath);
            byte[] bytesArr = value.getBytes();
            FileUtil.writeFile(filePath, bytesArr, 0, bytesArr.length);
            return "file://" + filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnPath;
    }
}
