package com.codeiy.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "okhttp3")
@Data
public class HttpProperties {
    /**
     * 连接超时时间，单位：秒
     */
    private int connectTimeout = 10;
    /**
     * 读取超时时间，单位：秒
     */
    private int readTimeout = 30;
    /**
     * 写入超时时间，单位：秒
     */
    private int writeTimeout = 30;
    /**
     * 是否在连接失败时重试
     */
    private boolean retryOnConnectionFailure = true;

    /**
     * 连接池最大空闲连接数
     */
    private int connectionPoolMaxIdleConnections = 10;
    /**
     * 连接池保持存活时间，单位：分钟
     */
    private int connectionPoolKeepAliveDurationMinutes = 5;
    /**
     * 重试次数
     */
    private int retryTimes = 3;
}
