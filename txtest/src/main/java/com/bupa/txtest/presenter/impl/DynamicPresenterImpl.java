package com.bupa.txtest.presenter.impl;

import com.bupa.txtest.adapter.EMCallBackAdapter;
import com.bupa.txtest.presenter.DynamicPresenter;
import com.bupa.txtest.utils.ThreadUtils;
import com.bupa.txtest.view.DynamicView;
import com.hyphenate.chat.EMClient;

/**
 * 作者: l on 2017/2/6 15:58
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class DynamicPresenterImpl implements DynamicPresenter {
    private static final String TAG = "DynamicPresenterImpl";
    public DynamicView mDynamicView;
    public DynamicPresenterImpl(DynamicView dynamicFragment) {
            mDynamicView = dynamicFragment;
    }

    @Override
    public void logout() {
        mDynamicView.onStartLogout();
        EMClient.getInstance().logout(true, mEMCallBackAdapter);
    }
    private EMCallBackAdapter mEMCallBackAdapter = new EMCallBackAdapter() {
        //在子线程回调
        @Override
        public void onSuccess() {
            // 退出成功
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDynamicView.onLogoutSuccess();
                }
            });
        }

        @Override
        public void onError(int i, String s) {
            // 退出失败
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDynamicView.onLogoutFailed();
                }
            });
        }
    };
}
