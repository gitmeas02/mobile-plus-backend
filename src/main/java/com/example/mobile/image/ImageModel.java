package com.example.mobile.image;

import java.util.List;

public class ImageModel {
    private Long id;
    private String fileName;
    private String title;
    private String url;  // URL to download the image
      // images associated with a project
    public ImageModel() {}

    public ImageModel(Long id, String fileName, String title, String url) {
        this.id = id;
        this.fileName = fileName;
        this.title = title;
        this.url = url;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}