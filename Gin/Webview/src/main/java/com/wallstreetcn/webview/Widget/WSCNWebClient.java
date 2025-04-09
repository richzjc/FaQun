package com.wallstreetcn.webview.Widget;


import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.WriterCallback;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.kronos.volley.toolbox.StringRequest;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.image.ImageWebCacheHelper;
import com.wallstreetcn.rpc.VolleyQueue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import androidx.annotation.Nullable;

/**
 * Created by zhangyang on 16/7/14.
 */
public class WSCNWebClient extends WSCNSslHandlerWebViewClient {

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String param = "";
        Uri uri = request.getUrl();
        if (uri != null) {
            param = request.getUrl().toString();
        }
        if (!TextUtils.isEmpty(param)) {
            if(param.startsWith(ImageWebCacheHelper.TAG)) {
                param = param.replaceFirst(ImageWebCacheHelper.TAG, "");
                byte[] bytes = getCacheFile(param);
                if (bytes == null) {
                    if (Util.getIsNoImage() && !Util.isConnectWIFI()) {
                        bytes = "NotLoad".getBytes();
                    } else /*if (param.contains(ImageWebCacheHelper.TAG_FORCE))*/ {
                        try {
                            bytes = VolleyQueue.getInstance().sync(new StringRequest(param)).body().bytes();
                            addImageCache(param, bytes);
                        } catch (Exception e) {

                        }
                    }
                }
                if (bytes == null)
                    bytes = "LoadError".getBytes();
                ByteArrayInputStream data = new ByteArrayInputStream(bytes);
                WebResourceResponse webResourceResponse = new WebResourceResponse("text/html", "utf-8", data);
                return webResourceResponse;
            } else if(param.startsWith("file://")) {
                TLog.e("webview", param);
                String path = uri.getPath();
                try {
                    if (path.endsWith("html"))
                        return new WebResourceResponse("text/html", "utf-8", new FileInputStream(path));
                    else if (path.endsWith("css"))
                        return new WebResourceResponse("text/css", "utf-8", new FileInputStream(path));
                    else if (path.endsWith("png"))
                        return new WebResourceResponse("image/png", "utf-8", new FileInputStream(path));
                    else if (path.endsWith("js"))
                        return new WebResourceResponse("text/javascript", "utf-8", new FileInputStream(path));
                    else if (path.endsWith("gif"))
                        return new WebResourceResponse("image/gif", "utf-8", new FileInputStream(path));
                    else if(path.endsWith("jpg") || path.endsWith("jpeg"))
                        return new WebResourceResponse("image/jpeg", "utf-8", new FileInputStream(path));
                    else
                        return new WebResourceResponse("text/html", "utf-8", new FileInputStream(path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.shouldInterceptRequest(view, request);
    }

    private static byte[] getCacheFile(String param) {
        ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(param));
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, imageRequest.getSourceUri());
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        if (resource == null) {
            resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
        }
        try {
            return resource == null ? null : resource.read();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addImageCache(String param, final byte[] bytes) {
        if (bytes == null)
            return;
        ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(param));
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, imageRequest.getSourceUri());
        try {
            ImagePipelineFactory.getInstance().getMainFileCache().insert(cacheKey, new WriterCallback() {
                @Override
                public void write(OutputStream os) throws IOException {
                    try {
                        os.write(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        os.close();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
