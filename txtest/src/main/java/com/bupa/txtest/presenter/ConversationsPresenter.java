package com.bupa.txtest.presenter;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * 作者: l on 2017/2/7 15:00
 * 邮箱: xjs250@163.com
 * 描述:
 */
public interface ConversationsPresenter {
    void loadConversations();

    List<EMConversation> getConversations();
}
