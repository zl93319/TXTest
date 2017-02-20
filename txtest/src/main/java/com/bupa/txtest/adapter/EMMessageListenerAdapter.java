package com.bupa.txtest.adapter;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * 作者: l on 2017/2/7 14:47
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class EMMessageListenerAdapter implements EMMessageListener {
    public static final String TAG = "EMMessageListenerAdapter";

    @Override
    public void onMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
