package com.codeiy.event;

import org.springframework.context.ApplicationEvent;

public class CrawlerErrorEvent extends ApplicationEvent {
    private Exception exception;

    public CrawlerErrorEvent(Object source, Exception exception) {
        super(source);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

}
