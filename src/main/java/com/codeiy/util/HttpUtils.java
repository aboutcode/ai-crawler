package com.codeiy.util;

import cn.hutool.json.JSONUtil;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpUtils {
    private final OkHttpClient okHttpClient;
    private final MeterRegistry meterRegistry;

    public Call newCall(String url) {
        Request request = new Request.Builder().url(url).build();
        return okHttpClient.newCall(request);
    }

    /**
     * 发送 GET 请求
     *
     * @param url 请求地址
     * @return 响应结果
     * @throws IOException 请求失败时抛出异常
     */
    public String get(String url) {
        Request request = new Request.Builder().url(url).build();
        return execute(request);
    }

    /**
     * 发送 POST 请求
     *
     * @param url  请求地址
     * @param json 请求体，JSON 格式
     * @return 响应结果
     * @throws IOException 请求失败时抛出异常
     */
    public String post(String url, String json) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(url).post(body).build();

        return execute(request);
    }

    /**
     * 执行请求
     *
     * @param request 请求对象
     * @return 响应结果
     * @throws IOException 请求失败时抛出异常
     */
    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            String message = String.format("请求失败，url: %s, message: %s", request.url(), e.getMessage());
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * 发送 GET 请求，并将响应结果转换为指定类型的对象
     *
     * @param url          请求地址
     * @param responseType 响应结果类型
     * @param <T>          响应结果类型
     * @return 响应结果
     * @throws IOException 请求失败时抛出异常
     */

    public <T> T get(String url, Class<T> responseType) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return JSONUtil.toBean(responseBody, responseType);
        } catch (IOException e) {
            log.error("请求失败，url: {}, message: {}", request.url(), e.getMessage());
            throw e;
        }
    }

    /**
     * 发送 POST 请求，并将响应结果转换为指定类型的对象
     *
     * @param url          请求地址
     * @param json         请求体，JSON 格式
     * @param responseType 响应结果类型
     * @param <T>          响应结果类型
     * @return 响应结果
     * @throws IOException 请求失败时抛出异常
     */
    public <T> T post(String url, String json, Class<T> responseType) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return JSONUtil.toBean(responseBody, responseType);
        } catch (IOException e) {
            log.error("请求失败，url: {}, message: {}", request.url(), e.getMessage());
            throw e;
        }
    }

}
