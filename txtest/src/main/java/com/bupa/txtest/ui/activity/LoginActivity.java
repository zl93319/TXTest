package com.bupa.txtest.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.presenter.LoginPresenter;
import com.bupa.txtest.presenter.impl.LoginPresenterImpl;
import com.bupa.txtest.view.LoginView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建者:   Leon
 * 创建时间:  2016/11/9 10:11
 * 描述：    TODO
 */
public class LoginActivity extends BaseActivity implements LoginView,View.OnFocusChangeListener {

    public static final String TAG = "LoginActivity";
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 0;
    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.new_user)
    TextView mNewUser;


    private LoginPresenter mLoginPresenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        super.init();

        mLoginPresenter = new LoginPresenterImpl(this);
        mPassword.setOnEditorActionListener(mOnEditorActionListener);
    }

    @OnClick({R.id.login, R.id.new_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                //检查是否有读写磁盘的权限
                if (checkIfHasWriteExternalStorage()) {
                    login();
                } else {
                    applyPermission();//申请权限
                }
                break;
            case R.id.new_user:
                // 跳转找注册的activity
                startActivity(RegisterActivity.class);
                break;
        }
    }


    private void applyPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    /**
     *
     * @return true 表示有权限
     */
    private boolean checkIfHasWriteExternalStorage() {
        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
//        PermissionChecker.PERMISSION_GRANTED;
    }
    private void login() {
        // 隐藏软键盘
        hideKeyboard();
        // 获取屏幕的默认分辨率
        Display display = getWindowManager().getDefaultDisplay();
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        mLoginPresenter.login(userName, password);
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                login();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onUserNameError() {
        mUserName.setError(getString(R.string.error_user_name));
    }

    @Override
    public void onPasswordError() {
        mPassword.setError(getString(R.string.error_password));
    }

    @Override
    public void onStartLogin() {
        // 加载正在登录 对话框
        showProgressDialog(getString(R.string.logining));
    }

    /**
     * 登录成功
     */
    @Override
    public void onLoginSuccess() {
        // 隐藏对话框
        hideProgressDialog();
        // 提示成功
        toast(getString(R.string.success_login));
        startActivity(MainActivity.class);
    }

    /**
     * 登录失败
     */
    @Override
    public void onLoginFailed() {
        hideProgressDialog();
        toast(getString(R.string.error_login));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    login();
                } else {
                    toast(getString(R.string.denied));
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }
}