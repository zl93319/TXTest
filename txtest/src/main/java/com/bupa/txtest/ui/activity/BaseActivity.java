package com.bupa.txtest.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * 作者: l on 2017/2/6 08:17
 * 邮箱: xjs250@163.com
 * 描述:
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    // 定义全局静态的 handler
    public Handler mHandler = new Handler();
    private InputMethodManager mInputMethodManager;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
    }

    /**
     * 获取布局xml的id.子类实现
     * @return
     */
    public abstract int getLayoutResId();

    /**
     * 启动一个Activity
     * @param activity
     */
    protected void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

    /**
     * 提交一个延迟任务
     * @param runnable
     * @param delay
     */
    protected void postDelay(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }
    protected void hideKeyboard() {
        //懒加载
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        }
        //getCurrentFocus获取当前有焦点的view
        //隐藏软键盘
        mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    // 显示进度框
    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }
    // 隐藏进度框
    protected void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
    // 打印toast
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
