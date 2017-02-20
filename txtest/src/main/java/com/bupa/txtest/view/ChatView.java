package com.bupa.txtest.view;

/**
 * 作者: l on 2017/2/7 00:07
 * 邮箱: xjs250@163.com
 * 描述:
 */
public interface ChatView {
    // 发送消息
    void onSendMessageSuccess();
    // 发送消息失败
    void onSendMessageFailed();
    // 发送消息中
    void onStartSendMessage();
    // 加载更多的消息
    void onMessagesLoaded();
    // 加载中
    void onMoreMessagesLoaded(int size);
    // 没有更多的消息
    void onNoMoreData();
}
