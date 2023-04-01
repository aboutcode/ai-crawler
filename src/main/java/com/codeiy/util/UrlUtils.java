package com.codeiy.util;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public class UrlUtils {
    /**
     * 获取基本URL（协议+域名+端口号），例如：https://www.example.com:80
     *
     * @param url URL字符串
     * @return 基本URL
     */
    public static String getBaseUrl(String url) {
        String baseUrl = "";
        try {
            URI uri = new URI(url);
            baseUrl = uri.getScheme() + "://" + uri.getHost();
            if (uri.getPort() != -1) {
                baseUrl += ":" + uri.getPort();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return baseUrl;
    }

    /**
     * 将相对 URL 转为绝对 URL
     *
     * @param baseUrl 基础 URL
     * @param url     相对 URL
     * @return 绝对 URL
     */
    public static String fixUrl(String baseUrl, String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }

        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = url;
        }

        try {
            URL base = new URL(baseUrl);
            URL absolute = new URL(base, url);
            return absolute.toExternalForm();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断是否为有效的 URL
     *
     * @param url 待判断的 URL
     * @return 是否为有效的 URL
     */
    public static boolean isValid(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
