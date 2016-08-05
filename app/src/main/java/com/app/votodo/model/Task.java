package com.app.votodo.model;

/**
 * Created by anuj on 04/08/16.
 */
public class Task {
    String title;
    String path;

    public Task(){

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }
}
