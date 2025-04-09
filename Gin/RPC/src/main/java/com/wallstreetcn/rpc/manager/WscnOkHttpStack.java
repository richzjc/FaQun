package com.wallstreetcn.rpc.manager;

import android.text.TextUtils;
import android.util.Log;

import com.kronos.volley.Request;
import com.kronos.volley.VolleyError;
import com.kronos.volley.toolbox.FileUploadRequest;
import com.kronos.volley.toolbox.OkHttpStack;
import com.wallstreetcn.helper.utils.TLog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Leif Zhang on 2016/12/13.
 * Email leifzhanggithub@gmail.com
 */

public class WscnOkHttpStack extends OkHttpStack {

    public WscnOkHttpStack(OkHttpClient client) {
        super(client);
    }

    @Override
    protected RequestBody createFileRequestBody(FileUploadRequest r) throws VolleyError {
        String fileName = new String(r.getBody());
        File file = new File(fileName);
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", SevenBullConstants.QiniuKey)
                .addFormDataPart("token", SevenBullConstants.QiniuToken)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse(r.getMediaType()), new File(fileName)))
                .build();
    }


    private Map<String, String> getHeaders() {
        try {
            Field[] fields = getClass().getSuperclass().getDeclaredFields();
            Field headerField = null;
            for (Field field : fields) {
                if (TextUtils.equals(field.getName(), "mHeaders"))
                    headerField = field;
            }
            if (headerField != null) {
                headerField.setAccessible(true);
                return (Map<String, String>) headerField.get(this);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
