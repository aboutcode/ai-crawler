package com.codeiy.event;

import org.springframework.context.ApplicationEvent;

public class CrawlerRequestEvent extends ApplicationEvent {
    private String url;

    public CrawlerRequestEvent(Object source, String url) {
        super(source);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
