package com.pixelcat.core.http;

import com.pixelcat.core.exception.PixelCatException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 *
 * 官方建议OkHttpClient应该单例化
 */
@Slf4j
public class OkHttpUtil {

    private OkHttpClient client;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpUtil(){
        client = new OkHttpClient().newBuilder()
                .connectTimeout(30L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .build();
    }

    private static class Inner{
        private static OkHttpUtil instance = new OkHttpUtil();
    }

    public static OkHttpUtil getInstance(){
        return Inner.instance;
    }

    public void get(String url, HttpResultHandler handler) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@Nullable Call call, @Nullable IOException e) {
                log.error("Http Get Request Failed. url:{},Cause:{}", url, e);
                throw new PixelCatException("Http Get Request Failed.", e);
            }

            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
                handler.deal(response.body().string());
            }
        });
    }

    public void post(String url, String bodyJson, HttpResultHandler handler) {
        RequestBody body = RequestBody.create(JSON, bodyJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@Nullable Call call, @Nullable IOException e) {
                log.error("Http Post Request Failed. url:{},body:{},Cause:{}", url, bodyJson, e);
                throw new PixelCatException("Http Post Request Failed.", e);
            }

            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
                handler.deal(response.body().string());
            }
        });
    }

}
