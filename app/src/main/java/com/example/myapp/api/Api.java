package com.example.myapp.api;

import android.text.TextUtils;
import android.util.Log;


import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author brynn
 * @since 2022/06/24 20:52
 */
public class Api {

    public volatile static Api api = null;
    private static String requestUrl;
    private static HashMap<String, Object> mParams;
    private static OkHttpClient client;

    private Api() {}

    public static Api config(String url, HashMap<String, Object> params) {
        requestUrl = ApiConfig.BASE_URl + url;
        mParams = params;
        client = new OkHttpClient.Builder().build();
        if (api == null) {
            synchronized (Api.class) {
                if (api == null) {
                    api = new Api();
                }
            }
        }
        return api;
    }

    // 拼接请求url，url+param
    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                if (TextUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }

    // 发送post请求
    public void postRequest(TtitCallback callback) {
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        //第二步创建Request
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .build();
        // 第三步创建请求执行对象call
        Call call = client.newCall(request);
        //第四步执行请求
        System.out.println(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }

    // 发送get请求
    public void getRequest(TtitCallback callback) {
        String url = getAppendUrl(requestUrl, mParams);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                callback.onSuccess(result);
            }
        });
    }
}
