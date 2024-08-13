package com.example.btlptudddn14.models;

import java.io.Serializable;

public class ListTypeTarget implements Serializable {
    private String imgpath;
    private String title;

    public ListTypeTarget(String imgpath, String title ) {
        this.imgpath = imgpath;
        this.title = title;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
