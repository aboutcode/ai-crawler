package com.codeiy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class HttpLog {
    private String url;
    /**
     * 请求头
     */
    private Map<String, String> headers = new HashMap<>();
    /**
     * 查询参数
     */
    private Map<String, String> queryParams = new HashMap<>();
    /**
     * 请求体
     */
    private String requestBody;
    /**
     * 响应体
     */
    private String responseBody;
    /**
     * 请求开始时间
     */
    private long startTime;
    /**
     * 请求结束时间
     */
    private long endTime;
}