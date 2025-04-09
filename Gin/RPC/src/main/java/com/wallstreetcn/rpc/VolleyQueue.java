package com.wallstreetcn.rpc;

import android.text.TextUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.kronos.volley.Network;
import com.kronos.volley.Request;
import com.kronos.volley.RequestQueue;
import com.kronos.volley.toolbox.BasicNetwork;
import com.kronos.volley.toolbox.DiskBasedCache;
import com.kronos.volley.toolbox.NetResponse;
import com.kronos.volley.toolbox.StringRequest;
import com.wallstreetcn.helper.utils.SharedPrefsUtil;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.system.EquipmentUtils;
import com.wallstreetcn.rpc.exception.BaseErrorCodeFactory;
import com.wallstreetcn.rpc.manager.WscnOkHttpStack;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhangyang on 16/5/3.
 */
public class VolleyQueue {

    private RequestQueue requestQueue;
    private Function0<Unit> token;
    private boolean tokenSwitch = true;

    public void addRequest(Request request) {
        try {
            if (token != null && tokenSwitch)
                token.invoke();
            start();
            removeRequest(request);
            requestQueue.add(request);
            ResponseTimeUtilKt.checkResponseTime(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUserAgent() {
        String userAgent = SharedPrefsUtil.getString("WebViewUa", "");
        return userAgent;
    }

    private static VolleyQueue instance;

    public static VolleyQueue getInstance() {
        if (instance == null) {
            synchronized (VolleyQueue.class) {
                // 第二次检查
                if (instance == null) {
                    instance = new VolleyQueue();
                }
            }
        }
        return instance;
    }

    private VolleyQueue() {
        requestQueue = newRequestQueue();
        header = new HashMap<>();
    }

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "volley";
    private WscnOkHttpStack stack;
    private OkHttpClient okClient;

    private RequestQueue newRequestQueue() {
        File cacheDir = new File(UtilsContextManager.getInstance().getApplication()
                .getCacheDir(), DEFAULT_CACHE_DIR);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
//                .dns(new WscnDns())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 5, TimeUnit.MINUTES));


        client.addNetworkInterceptor(chain -> {
            okhttp3.Request.Builder builder = chain.request().newBuilder();
            if (!TextUtils.isEmpty(getUserAgent())) {
                builder.removeHeader("User-Agent").addHeader("User-Agent", getUserAgent());
            }
            return chain.proceed(builder.build());
        });
        if (EquipmentUtils.getVersionName().contains("-debug")) {
            client.addNetworkInterceptor(new StethoInterceptor());
        }
        okClient = client.build();
        stack = new WscnOkHttpStack(okClient);
        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        queue.start();
        return queue;
    }

    private HashMap<String, String> header;

    public void addHeader(Map<String, String> map) {
        header.putAll(map);
        requestQueue.addHeader(map);
    }

    @NonNull
    public OkHttpClient getOkClient() {
        return okClient;
    }

    public Response sync(Request request) throws Exception {
        return stack.performRequest(request, new HashMap<>());
    }

    private boolean isRestart = false;

    public void start() {
        if (isRestart) {
            requestQueue.start();
            isRestart = false;
        }
    }

    public void stop() {
        requestQueue.stop();
        isRestart = true;
    }

    private BaseErrorCodeFactory factory;

    public BaseErrorCodeFactory getFactory() {
        return factory;
    }

    public void setFactory(BaseErrorCodeFactory factory) {
        this.factory = factory;
    }

    public void removeRequest(final String url) {
        requestQueue.cancelAll(request -> TextUtils.equals(request.getUrl(), url));
    }

    /**
     * 去重，取消重复的已发的请求，去除监听器，待新的请求返回后正确回掉请求
     * 但是代码有问题，判断逻辑有bug,url判断，并不能代表一定相同
     *
     * @param newRequest
     */
    private void removeRequest(final Request<?> newRequest) {
        List<Request<?>> requestList = new ArrayList<>();
        requestQueue.cancelAll(request -> {
            if (TextUtils.equals(request.getUrl(), newRequest.getUrl())) {
                requestList.add(request);
                return true;
            }
            return false;
        });

        if (!requestList.isEmpty()) {
            requestList.add(newRequest);
            ResponseListListener listener = getRequest(requestList);
            newRequest.setErrorListener(listener);
            if (newRequest instanceof StringRequest) {
                ((StringRequest) newRequest).setRequestListener(listener);
            }
        }
    }

    private ResponseListListener getRequest(List<Request<?>> requestList) {
        List<com.kronos.volley.Response.Listener<NetResponse>> list = new ArrayList<>();
        List<com.kronos.volley.Response.ErrorListener> errorListenerList = new ArrayList<>();
        for (Request<?> request : requestList) {
            if (request instanceof RpcStringRequest) {
                RpcStringRequest stringRequest = (RpcStringRequest) request;
                list.add(stringRequest.requestListener);
                errorListenerList.add(stringRequest.getErrorListener());
            } else if (request instanceof RpcJsonRequest) {
                RpcJsonRequest jsonRequest = (RpcJsonRequest) request;
                list.add(jsonRequest.requestListener);
                errorListenerList.add(jsonRequest.getErrorListener());
            }
        }
        return new ResponseListListener(list, errorListenerList);
    }

    public Map<String, String> getRequestHeader() {
        return header;
    }


    /**
     * 下面的这个方法是为了解决线上用户一直反馈登录的问题
     *
     * @param token
     */
    public void setGetToken(@NotNull Function0<Unit> token) {
        this.token = token;
    }
}
