package com.bupa.txtest.ui.activity;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.presenter.impl.RegisterPresenterImpl;
import com.bupa.txtest.view.RegisterView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者: l on 2017/2/6 09:05
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class RegisterActivity extends BaseActivity implements RegisterView {
    private static final String TAG = "RegisterActivity";

    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.register)
    Button mRegister;
    private RegisterPresenterImpl mRegisterPresenter;

    @Override
    public int getLayoutResId() {
        // 加载注册界面的布局
        return R.layout.activity_register;
    }

    @Override
    public void init() {
        super.init();
        mRegisterPresenter = new RegisterPresenterImpl(this);
        //监听软键盘ACTION键事件
        mConfirmPassword.setOnEditorActionListener(mOnEditorActionListener);
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                register();
            }
            return false;
        }
    };

    private void register() {
        //隐藏软键盘
        hideKeyboard();
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        mRegisterPresenter.register(userName, password, confirmPassword);
    }


    @Override
    public void onUserNameError() {
        mUserName.setError(getString(R.string.error_user_name));
    }

    @Override
    public void onPasswordError() {
        mPassword.setError(getString(R.string.error_password));
    }

    @Override
    public void onConfirmPasswordError() {
        mConfirmPassword.setError(getString(R.string.error_confirm_password));
    }

    @Override
    public void onStartRegister() {
        showProgressDialog(getString(R.string.registering));
    }

    @Override
    public void onRegisterFailed() {
        hideProgressDialog();
        toast(getString(R.string.error_register_failed));
    }

    @Override
    public void onRegisterSuccess() {
        hideProgressDialog();
        toast(getString(R.string.success_register));
        startActivity(LoginActivity.class);
    }

    @Override
    public void onUserNameExist() {
        hideProgressDialog();
        toast(getString(R.string.user_name_exist));
    }


    @OnClick(R.id.register)
    public void onClick() {
        // 点击注册
        register();
    }
}
