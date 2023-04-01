package com.codeiy.event;

import org.springframework.context.ApplicationEvent;


public class CrawlerParsedEvent extends ApplicationEvent {
    private String title;
    private String content;

    public CrawlerParsedEvent(Object source, String title, String content) {
        super(source);
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
