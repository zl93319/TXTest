package com.bupa.txtest.view;

/**
 * 作者: l on 2017/2/6 08:45
 * 邮箱: 2293809059@qq.com
 * 描述: 登录的ui
 */
public interface LoginView {

    void onUserNameError();

    void onPasswordError();

    void onStartLogin();

    void onLoginSuccess();

    void onLoginFailed();
}
