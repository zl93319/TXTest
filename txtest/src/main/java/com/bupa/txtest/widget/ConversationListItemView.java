package com.bupa.txtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者: l on 2017/2/7 14:51
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ConversationListItemView extends RelativeLayout {
    public static final String TAG = "ConversationListItemView";
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.last_message)
    TextView mLastMessage;
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.unread_count)
    TextView mUnreadCount;


    public ConversationListItemView(Context context) {
        this(context, null);
    }

    public ConversationListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_conversation_list_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(EMConversation emConversation) {
        mUserName.setText(emConversation.getUserName());

        //刷新最后一条消息的内容
        EMMessage lastMessage = emConversation.getLastMessage();
        EMMessageBody body = lastMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            mLastMessage.setText(((EMTextMessageBody) body).getMessage());
        } else {
            mLastMessage.setText(getContext().getString(R.string.no_text_message));
        }

        //刷新时间戳
        long msgTime = lastMessage.getMsgTime();
        String timestampString = DateUtils.getTimestampString(new Date(msgTime));
        mTimestamp.setText(timestampString);

        //未读消息个数
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if (unreadMsgCount > 0) {
            mUnreadCount.setVisibility(VISIBLE);
            mUnreadCount.setText(String.valueOf(unreadMsgCount));
        } else {
            mUnreadCount.setVisibility(GONE);
        }

    }
}
