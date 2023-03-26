package com.codeiy.util.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 连接超时时间，单位：秒
     */
    private static final int CONNECT_TIMEOUT = 10;
    /**
     * 读取超时时间，单位：秒
     */
    private static final int READ_TIMEOUT = 30;
    /**
     * 写入超时时间，单位：秒
     */
    private static final int WRITE_TIMEOUT = 30;
    /**
     * 是否在连接失败时重试
     */
    private static final boolean RETRY_ON_CONNECTION_FAILURE = true;

    /**
     * 连接池最大空闲连接数
     */
    private static final int CONNECTION_POOL_MAX_IDLE_CONNECTIONS = 10;
    /**
     * 连接池保持存活时间，单位：分钟
     */
    private static final int CONNECTION_POOL_KEEP_ALIVE_DURATION_MINUTES = 5;
    /**
     * 重试次数
     */
    private static final int RETRY_TIMES = 3;

    private static final ConnectionPool connectionPool = new ConnectionPool(CONNECTION_POOL_MAX_IDLE_CONNECTIONS,
            CONNECTION_POOL_KEEP_ALIVE_DURATION_MINUTES, TimeUnit.MINUTES);

    private static final OkHttpClient client = new OkHttpClient.Builder().connectionPool(connectionPool)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).addInterceptor(new RetryInterceptor(RETRY_TIMES))
//            .addInterceptor(new LoggingInterceptor())
            .build();

    /**
     * 发送 GET 请求
     * 
     * @param url 请求地址
     * @return 响应结果
     * @throws IOException 请求失败时抛出异常
     */
    public static String get(String url) throws IOException {
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
    public static String post(String url, String json) throws IOException {
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
    private static String execute(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            logger.error("请求失败，url: {}, message: {}", request.url(), e.getMessage());
            throw e;
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

    public static <T> T get(String url, Class<T> responseType) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return JSONUtil.toBean(responseBody, responseType);
        } catch (IOException e) {
            logger.error("请求失败，url: {}, message: {}", request.url(), e.getMessage());
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
    public static <T> T post(String url, String json, Class<T> responseType) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(url).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            return JSONUtil.toBean(responseBody, responseType);
        } catch (IOException e) {
            logger.error("请求失败，url: {}, message: {}", request.url(), e.getMessage());
            throw e;
        }
    }

}
