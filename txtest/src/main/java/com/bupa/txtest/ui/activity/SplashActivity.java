package com.bupa.txtest.ui.activity;

import com.bupa.txtest.R;
import com.bupa.txtest.presenter.impl.SplashPresenterImpl;
import com.bupa.txtest.view.SplashView;

/**
 * 作者: l on 2017/2/6 08:25
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class SplashActivity extends BaseActivity implements SplashView{
    private static final String TAG = "SplashActivity";
    private static final int DEFAULT_DELAY = 2000;
    private SplashPresenterImpl mSplashPresenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        super.init();
        mSplashPresenter = new SplashPresenterImpl(this);
        mSplashPresenter.checkLoginStatus();//检查登录状态
    }
    /**
     * 如果已经登录，则跳转到主界面
     */
    @Override
    public void onLoggedIn() {
        startActivity(MainActivity.class);
    }

    /**
     * 没有登录 ,则跳转到登录界面
     */
    @Override
    public void onNotLogin() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                startActivity(LoginActivity.class);
            }
        }, DEFAULT_DELAY);
    }
}
