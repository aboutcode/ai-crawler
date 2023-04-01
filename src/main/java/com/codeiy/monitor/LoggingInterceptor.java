package com.codeiy.monitor;

import java.io.IOException;
import java.nio.charset.Charset;

import com.codeiy.dto.HttpLog;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map;


/**
 * OkHttp的拦截器，用于记录HTTP请求和响应的日志
 */
public class LoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpLog httpLog = new HttpLog();
        httpLog.setUrl(request.url().toString());
        httpLog.setStartTime(System.nanoTime());
        httpLog.setHeaders(request.headers().toMultimap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0))));
        httpLog.setQueryParams(request.url().queryParameterNames().stream().collect(Collectors.toMap(Function.identity(), key -> request.url().queryParameterValues(key).get(0))));
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                httpLog.getHeaders().put("Content-Type", requestBody.contentType().toString());
            }
            if (requestBody.contentLength() != -1) {
                httpLog.getHeaders().put("Content-Length", String.valueOf(requestBody.contentLength()));
            }
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            httpLog.setRequestBody(buffer.readString(UTF8));
        }

        Response response = chain.proceed(request);

        httpLog.setEndTime(System.nanoTime());
//        httpLog.setResponseBody(response.body().string());
        System.out.println(httpLog);

        return response;
    }
}
