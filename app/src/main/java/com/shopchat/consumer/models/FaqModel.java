package com.shopchat.consumer.models;

/**
 * Created by Sudipta on 9/17/2015.
 */
public class FaqModel {

    private String content;

    public FaqModel(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
