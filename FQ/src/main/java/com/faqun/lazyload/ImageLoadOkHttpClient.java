package com.faqun.lazyload;

import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.image.ImageUtlFormatHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageLoadOkHttpClient {
    private OkHttpClient okHttpClient;

    public ImageLoadOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
//                .dns(new WscnDns())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 5, TimeUnit.MINUTES));
        okHttpClient = client.build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    static class ImageRetryInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            int code = response.code();
            if (code != 200 && checkUrl(request)) {
                Request.Builder builder = request.newBuilder();
                HttpUrl url = request.url();
                HttpUrl.Builder newUrlBuilder = new HttpUrl.Builder();
                HttpUrl newUrl = newUrlBuilder.scheme(url.scheme()).host(url.host()).encodedPath(url.encodedPath()).build();
                builder.url(ImageUtlFormatHelper.formatImageWithThumbnailJpeg(newUrl.toString(), 500, 0));
                response.close();
                response = chain.proceed(builder.build());
                TLog.i("ImageRetryInterceptor", "result:" + response.isSuccessful());

            }
            return response;
        }


        private boolean checkUrl(Request request) {
            return ImageUtlFormatHelper.checkWhiteList(request.url().host());
        }
    }
}
