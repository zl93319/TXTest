package com.bupa.txtest.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.presenter.impl.DynamicPresenterImpl;
import com.bupa.txtest.ui.activity.LoginActivity;
import com.bupa.txtest.view.DynamicView;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: l on 2017/2/6 14:39
 * 邮箱: xjs250@163.com
 * 描述: 动态
 */
public class DynamicFragment extends BaseFragment implements DynamicView {
    private static final String TAG = "DynamicFragment";
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.logout)
    Button mLogout;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.show)
    WebView mShow;
    private DynamicPresenterImpl mDynamicPresenter;
    private WebSettings mSettings;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void init() {
        mDynamicPresenter = new DynamicPresenterImpl(this);
        mTitle.setText(getString(R.string.dynamic));
        mName.setText(EMClient.getInstance().getCurrentUser());

        mLogout.setText("退出");
        initShow();

    }

    private void initShow() {
        setWebView();
        setWebClient();
        mShow.loadUrl("http://192.168.1.121:8080/Test/index1.html");
    }

    private void setWebClient() {
        mShow.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            // 进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        mShow.setWebViewClient(new WebViewClient() {
            // 页面加载中
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完毕
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // TODO: 2017/2/13 安卓调js  start

            }
        });
    }

    private void setWebView() {
        // 开启js
        mSettings = mShow.getSettings();
        // 支持脚本
        mSettings.setJavaScriptEnabled(true);

    }


    // 正在退出
    @Override
    public void onStartLogout() {
        showProgressDialog(getString(R.string.logouting));
    }

    // 退出成功
    @Override
    public void onLogoutSuccess() {
        hideProgressDialog();
        toast(getString(R.string.logout_success));
        //跳转到登录界面
        startActivity(LoginActivity.class);
    }

    // 退出失败
    @Override
    public void onLogoutFailed() {
        hideProgressDialog();
        toast(getString(R.string.logout_failed));
    }

    // 点击退出当前用户
    @OnClick(R.id.logout)
    public void onClick() {
        mDynamicPresenter.logout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
