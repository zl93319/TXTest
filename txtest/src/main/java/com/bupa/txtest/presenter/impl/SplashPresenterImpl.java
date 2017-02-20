package com.bupa.txtest.presenter.impl;

import com.bupa.txtest.presenter.SplashPresenter;
import com.bupa.txtest.ui.activity.SplashActivity;
import com.bupa.txtest.view.SplashView;
import com.hyphenate.chat.EMClient;

/**
 * 作者: l on 2017/2/6 11:10
 * 邮箱: xjs250@163.com
 * 描述: // TODO: 2017/2/6
 */
public class SplashPresenterImpl implements SplashPresenter {
    private static final String TAG = "SplashPresenterImpl";
    private SplashView mSplashView;

    //维持一view的引用
    public SplashPresenterImpl(SplashActivity splashActivity) {
        mSplashView = splashActivity;
    }

    /**
     * 检测登录状态
     */
    @Override
    public void checkLoginStatus() {
        if (isLoggedIn()) {
            mSplashView.onLoggedIn();
        } else {
            mSplashView.onNotLogin();
        }
    }

    /**
     * 判断是否登录到环信服务器
     * @return
     */
    private boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected();
    }
}
