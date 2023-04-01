package com.codeiy.config;

import com.codeiy.dto.HttpProperties;
import com.codeiy.monitor.LoggingInterceptor;
import com.codeiy.monitor.MetricInterceptor;
import com.codeiy.monitor.RetryInterceptor;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(HttpProperties.class)
@RequiredArgsConstructor
public class OkHttpConfig {

    private final HttpProperties httpProperties;
    private final MeterRegistry meterRegistry;

    @Bean
    public ConnectionPool connectionPool() {
        return new ConnectionPool(httpProperties.getConnectionPoolMaxIdleConnections(),
                httpProperties.getConnectionPoolKeepAliveDurationMinutes(), TimeUnit.MINUTES);
    }

    @Bean
    public OkHttpClient okHttpClient(ConnectionPool connectionPool) {
        return new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(httpProperties.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpProperties.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(httpProperties.getWriteTimeout(), TimeUnit.SECONDS)
                .addInterceptor(new MetricInterceptor(meterRegistry))
//                .addInterceptor(new RetryInterceptor(httpProperties.getRetryTimes()))
                .addInterceptor(new LoggingInterceptor())
                .build();
    }
}
