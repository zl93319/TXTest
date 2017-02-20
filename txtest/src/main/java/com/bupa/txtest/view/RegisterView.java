package com.bupa.txtest.view;

/**
 * 作者: l on 2017/2/6 09:51
 * 邮箱: xjs250@163.com
 * 描述:
 */
public interface RegisterView  {
    // 用户名错误
    void onUserNameError();
    // 用户密码错误
    void onPasswordError();
    // 重复密码错误
    void onConfirmPasswordError();
    // 开始注册
    void onStartRegister();
    // 注册失败
    void onRegisterFailed();
    // 注册成功
    void onRegisterSuccess();
    // 用户名已存在
    void onUserNameExist();
}
