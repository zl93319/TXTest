package com.bupa.txtest.presenter.impl;

import com.bupa.txtest.adapter.EMCallBackAdapter;
import com.bupa.txtest.presenter.ChatPresenter;
import com.bupa.txtest.utils.ThreadUtils;
import com.bupa.txtest.view.ChatView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;



/**
 * 作者: l on 2017/2/7 00:09
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ChatPresenterImpl implements ChatPresenter{
    private static final String TAG = "ChatPresenterImpl";
    private final ChatView mChatView;
    private final ArrayList<EMMessage> mMessages;
    private boolean hasMoreData = true;
    private static final int PAGE_SIZE = 20;

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
        // 获取所有的消息
        mMessages = new ArrayList<>();
    }

    @Override
    public void sendMessage(final String userName,final String msg) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage(msg, userName);
                message.setMessageStatusCallback(mEMCallBackAdapter);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                // 将消息添加到 集合中
                mMessages.add(message);
                //通知View层更新消息列表
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 开始发送
                        mChatView.onStartSendMessage();
                    }
                });
            }
        });
    }

    @Override
    public List<EMMessage> getMessages() {
        return mMessages;
    }

    @Override
    public void loadMessages(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                //如果没有聊天记录 conversation会返回一个null
                if (conversation != null) {
                    //标记会话所有消息已读
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();
                    List<EMMessage> allMessages = conversation.getAllMessages();
                    mMessages.addAll(allMessages);
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onMessagesLoaded();
                    }
                });
            }


        });
    }

    @Override
    public void loadMoreMessages(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if (hasMoreData) {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                    //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                    //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                    String startMsgId = mMessages.get(0).getMsgId();
                    final List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, PAGE_SIZE);
                    //将新加载的数据添加到成员数据集合里面
                    mMessages.addAll(0, messages);
                    hasMoreData = (messages.size() == PAGE_SIZE);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.onMoreMessagesLoaded(messages.size());
                        }
                    });
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.onNoMoreData();
                        }
                    });
                }

            }
        });
    }

    @Override
    public void markMessageRead(String userName) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
        //指定会话消息未读数清零
        conversation.markAllMessagesAsRead();
    }

    private EMCallBackAdapter mEMCallBackAdapter = new EMCallBackAdapter() {
        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageSuccess();
                }
            });
        }

        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageFailed();
                }
            });
        }
    };
}
