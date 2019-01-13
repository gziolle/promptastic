package com.gziolle.promptastic.data.model;

public class Script {

    private String title;
    private String content;

    public Script(String title, String content) {
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
