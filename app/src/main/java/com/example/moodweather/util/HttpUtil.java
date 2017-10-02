package com.example.moodweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * http工具类
 */
public class HttpUtil {
    /**
     * 发送http请求
     */
    public static void sendOkHttpRequest (String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
