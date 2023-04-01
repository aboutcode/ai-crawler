package com.codeiy.util;

import cn.hutool.core.util.URLUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class RobotsTxt {

    private static final Map<String, RobotsTxt> cache = new HashMap<>();

    private final String userAgent;
    private final boolean allowAll;
    private final boolean disallowAll;
    private final Map<String, Boolean> allowMap;
    private final Map<String, Boolean> disallowMap;

    private RobotsTxt(String userAgent, boolean allowAll, boolean disallowAll, Map<String, Boolean> allowMap, Map<String, Boolean> disallowMap) {
        this.userAgent = userAgent;
        this.allowAll = allowAll;
        this.disallowAll = disallowAll;
        this.allowMap = allowMap;
        this.disallowMap = disallowMap;
    }

    public static RobotsTxt get(String url, String userAgent) throws IOException {
        URL robotUrl = new URL(UrlUtils.getBaseUrl(url) + "/robots.txt");
        String key = robotUrl.toString() + "_" + userAgent;

        RobotsTxt robotsTxt = cache.get(key);
        if (robotsTxt != null) {
            return robotsTxt;
        }

        URLConnection conn = robotUrl.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        boolean allowAll = false;
        boolean disallowAll = false;
        Map<String, Boolean> allowMap = new HashMap<>();
        Map<String, Boolean> disallowMap = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("User-agent:")) {
                if (line.contains(userAgent) || line.contains("*")) {
                    allowAll = false;
                    disallowAll = false;
                } else {
                    allowAll = true;
                    disallowAll = true;
                }
            } else if (line.startsWith("Allow:")) {
                String allowPath = line.substring(line.indexOf(':') + 1).trim();
                allowMap.put(allowPath, true);
            } else if (line.startsWith("Disallow:")) {
                String disallowPath = line.substring(line.indexOf(':') + 1).trim();
                disallowMap.put(disallowPath, true);
            } else if (line.startsWith("Crawl-delay:")) {
                // ignore for now
            } else if (line.startsWith("#")) {
                // ignore comments
            } else {
                // unknown command
            }
        }

        reader.close();

        if (allowAll) {
            allowMap.clear();
            allowMap.put("/", true);
        }

        if (disallowAll) {
            disallowMap.clear();
        }

        robotsTxt = new RobotsTxt(userAgent, allowAll, disallowAll, allowMap, disallowMap);
        cache.put(key, robotsTxt);

        return robotsTxt;
    }

    public boolean isAllowed(String url) {
        String path = URLUtil.getPath(url);

        if (allowAll) {
            return true;
        } else if (disallowAll) {
            return false;
        } else if (disallowMap.containsKey("/") || disallowMap.containsKey(path)) {
            return false;
        } else if (allowMap.containsKey("/") || allowMap.containsKey(path)) {
            return true;
        } else {
            return true;
        }
    }
}
