package com.codeiy.monitor;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
public class MetricInterceptor implements Interceptor {
    private final MeterRegistry meterRegistry;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Timer.Sample sample = Timer.start(meterRegistry);
        Request request = chain.request();
        Response response = null;
        try {
            response = chain.proceed(request);
        } finally {
            sample.stop(Timer.builder("http.client.requests")
                    .tag("uri", request.url().uri().toString())
                    .tag("status", response == null ? "404" : String.valueOf(response.code()))
                    .tag("method", request.method())
                    .publishPercentiles(0.5, 0.95)
                    .publishPercentileHistogram()
                    .sla(Duration.ofMillis(500))
                    .register(meterRegistry));
        }
        return response;
    }
}
