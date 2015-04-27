package com.macbook.zxb.squidbmysample.datas;

/**
 * Created by zxb on 15/4/27.
 */
public class LoginUser {
    private String username;

    private LoginUser(){

    }

    private static LoginUser INSTANCE = new LoginUser();

    public static LoginUser getInstance(){
        return INSTANCE;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
