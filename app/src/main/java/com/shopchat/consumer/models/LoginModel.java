package com.shopchat.consumer.models;

/**
 * Created by Sudipta on 9/25/2015.
 */
public class LoginModel {

    private String cookieName;
    private String cookieValue;

    public LoginModel(String cookieName, String cookieValue) {
        this.cookieName = cookieName;
        this.cookieValue = cookieValue;
    }

    public LoginModel(){

    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }
}
