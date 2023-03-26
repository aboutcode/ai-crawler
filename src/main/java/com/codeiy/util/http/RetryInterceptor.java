package com.codeiy.util.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
/**
 * 重试拦截器，用于OkHttp的重试机制
 */
public class RetryInterceptor implements Interceptor {

    /**
     * 最大重试次数
     */
    private int maxRetry;
    /**
     * 已经重试次数
     */
    private int retryNum = 0;

    public RetryInterceptor(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        boolean responseOK = false;
        while (!responseOK && retryNum < maxRetry) {
            try {
                response = chain.proceed(request);
                responseOK = response.isSuccessful();
            } catch (Exception e) {
                retryNum++;
                if (retryNum >= maxRetry) {
                    throw e;
                }
            }
        }
        return response;
    }
}
