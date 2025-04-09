package com.wallstreetcn.rpc.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.kronos.volley.Request;
import com.kronos.volley.toolbox.StringRequest;
import com.wallstreetcn.helper.utils.SharedPrefsUtil;
import com.wallstreetcn.helper.utils.file.CacheUtils;
import com.wallstreetcn.helper.utils.file.FileUtil;
import com.wallstreetcn.helper.utils.file.ZipHelper;
import com.wallstreetcn.helper.utils.router.RouterHelper;
import com.wallstreetcn.helper.utils.text.TextUtil;
import com.wallstreetcn.rpc.VolleyQueue;

import java.io.File;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by Leif Zhang on 2016/11/18.
 * Email leifzhanggithub@gmail.com
 */
public class ResourcesDownloadService extends IntentService {
    public ResourcesDownloadService() {
        super("ResourcesDownloadService");
    }

    public static void startService(Context context, String url) {
        Intent intent = new Intent(context, ResourcesDownloadService.class);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    private static final String DEFAULT_CACHE_DIR = CacheUtils.CACHE_RESOURCE;


    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        File cacheDir = new File(getCacheDir(), DEFAULT_CACHE_DIR);
        try {
            //String filePath = TextUtil.format("%s/%d.%s", cacheDir.getPath(), url.hashCode(), getSuffix(url));
            String filePath = getFilePath(this, url);
            Request request = new StringRequest(url);
            Response response = VolleyQueue.getInstance().sync(request);
            InputStream io = response.body().byteStream();
            FileUtil.writeFile(filePath, io);
            SharedPrefsUtil.save(url, true);
            if (TextUtils.equals(".zip", RouterHelper.getSuffix(url))) {
                handleZip(cacheDir, String.valueOf(url.hashCode()), filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void handleZip(File cacheDir, String url, String filePath) {
        File zipFile = new File(cacheDir, String.valueOf(url.hashCode()));
        ZipHelper.zipFile(filePath, zipFile.getPath());
    }


    public static String getFileDirectory(Context context, String url) {
        return TextUtil.format("%s/%d", context.getCacheDir() + File.separator + DEFAULT_CACHE_DIR, String.valueOf(url.hashCode()).hashCode());
    }

    public static String getFilePath(Context context, String url) {
        return TextUtil.format("%s/%d%s", context.getCacheDir() + File.separator + DEFAULT_CACHE_DIR, url.hashCode(),
                RouterHelper.getSuffix(url));
    }


}
