package com.bupa.txtest.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * 作者: l on 2017/2/7 00:08
 * 邮箱: xjs250@163.com
 * 描述:
 */
public interface ChatPresenter {
    // 发送消息的数据
    void sendMessage(String userName, String message);
    // 获取所有的消息
    List<EMMessage> getMessages();
    // 加载消息
    void loadMessages(String userName);
    // 加载更多的消息
    void loadMoreMessages(String userName);
    // 标记消息的数目
    void markMessageRead(String userName);
}
