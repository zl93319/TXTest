package com.bupa.txtest.presenter.impl;

import com.bupa.txtest.presenter.ConversationsPresenter;
import com.bupa.txtest.utils.ThreadUtils;
import com.bupa.txtest.view.ConversationsView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者: l on 2017/2/7 14:59
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ConversationsPresenterImpl implements ConversationsPresenter {
    public static final String TAG = "ConversationsPresenterImpl";

    private ConversationsView mConversationsView;

    private List<EMConversation> mEMConversations;

    public ConversationsPresenterImpl(ConversationsView conversationsView) {
        mConversationsView = conversationsView;
        mEMConversations = new ArrayList<EMConversation>();
    }

    @Override
    public void loadConversations() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //先清空会话列表
                mEMConversations.clear();

                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                mEMConversations.addAll(conversations.values());

                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConversationsView.onConversationsLoaded();
                    }
                });
            }
        });
    }

    @Override
    public List<EMConversation> getConversations() {
        return mEMConversations;
    }
}
