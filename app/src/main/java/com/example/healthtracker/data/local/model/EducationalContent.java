package com.example.healthtracker.data.local.model;


public class EducationalContent {
    private String title;
    private String content;

    public EducationalContent(String title, String content) {
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
