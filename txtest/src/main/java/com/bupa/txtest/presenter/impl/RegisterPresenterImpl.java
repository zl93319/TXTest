package com.bupa.txtest.presenter.impl;

import com.bupa.txtest.app.Constant;
import com.bupa.txtest.model.User;
import com.bupa.txtest.presenter.RegisterPresenter;
import com.bupa.txtest.view.RegisterView;
import com.bupa.txtest.utils.StringUtils;
import com.bupa.txtest.utils.ThreadUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 作者: l on 2017/2/6 09:59
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class RegisterPresenterImpl implements RegisterPresenter {
    private static final String TAG = "RegisterPresenterImpl";
    private final RegisterView mRegisterView;

    public RegisterPresenterImpl(RegisterView registerView) {
        mRegisterView = registerView;
    }

    public void register(String userName, String password, String confirmPassword) {
        if (StringUtils.isValidUserName(userName)) {
            //检查密码
            if (StringUtils.isValidPassword(password)) {
                //检查确认密码
                if (password.equals(confirmPassword)) {
                    //开始注册
                    //通知view层开始注册
                    mRegisterView.onStartRegister();
                    registerBmob(userName, password);


                } else {
                    //通知view层确认密码错误
                    mRegisterView.onConfirmPasswordError();
                }
            } else {
                //通知view层密码错误
                mRegisterView.onPasswordError();
            }
        } else {
            //通知view层用户名错误
            mRegisterView.onUserNameError();
        }
    }

    /**
     * 注册到Bmob
     */
    private void registerBmob(final String userName, final String password) {
        User user = new User(userName, password);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    //没有异常表示注册成功

                    startRegisterEaseMob(userName, password);
                } else {
                    //有异常注册失败
                    if (e.getErrorCode() == Constant.ErrorCode.USER_NAME_ALREADY_EXIST) {
                        mRegisterView.onUserNameExist();
                    } else {
                        mRegisterView.onRegisterFailed();
                    }
                }
            }


        });
    }

    /**
     * 注册到环信服务器
     *
     * @param userName
     * @param password
     */
    private void startRegisterEaseMob(final String userName, final String password) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //注册失败会抛出HyphenateException
                try {
                    EMClient.getInstance().createAccount(userName, password);//同步方法
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterFailed();
                        }
                    });
                }
            }
        });
    }
}
