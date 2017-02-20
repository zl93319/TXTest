package com.bupa.txtest.presenter.impl;

import com.bupa.txtest.adapter.EMCallBackAdapter;
import com.bupa.txtest.presenter.LoginPresenter;
import com.bupa.txtest.ui.activity.LoginActivity;
import com.bupa.txtest.utils.StringUtils;
import com.bupa.txtest.utils.ThreadUtils;
import com.bupa.txtest.view.LoginView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * 作者: l on 2017/2/6 08:47
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class LoginPresenterImpl implements LoginPresenter {
    private static final String TAG = "LoginPresenterImpl";
    public LoginView mLoginView;
    public LoginPresenterImpl(LoginActivity loginActivity) {
        mLoginView = loginActivity;
    }

    @Override
    public void login(String userName, String password) {
        if (StringUtils.isValidUserName(userName)) {
            //检查密码
            if (StringUtils.isValidPassword(password)) {
                //开始登录
                mLoginView.onStartLogin();
                //登录到环信服务器
                loginEaseMob(userName, password);
            } else {
                mLoginView.onPasswordError();
            }

        } else {
            mLoginView.onUserNameError();
        }
    }
    private void loginEaseMob(String userName, String password) {
        EMClient.getInstance().login(userName, password, mEMCallBack);
    }

    private EMCallBack mEMCallBack = new EMCallBackAdapter(){
        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoginView.onLoginSuccess();

                }
            });
        }

        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoginView.onLoginFailed();
                }
            });
        }
    };



}
