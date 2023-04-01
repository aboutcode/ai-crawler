package com.codeiy.event;

import org.springframework.context.ApplicationEvent;


public class SaveDataEvent extends ApplicationEvent {
    private String category;

    public SaveDataEvent(Object source, String category) {
        super(source);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
