package com.bupa.txtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者: l on 2017/2/7 14:39
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ReceiveMessageItemView extends RelativeLayout {

    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.message)
    TextView mMessage;
    @BindView(R.id.avatar1)
    ImageView mAvatar;


    public ReceiveMessageItemView(Context context) {
        this(context, null);
    }

    public ReceiveMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_receive_message_item, this);
        ButterKnife.bind(this, this);

        if (EMClient.getInstance().getCurrentUser().toString().contains("bu")||EMClient.getInstance().getCurrentUser().toString().contains("zha")) {

            mAvatar.setImageDrawable(getResources().getDrawable(R.drawable.tx1));
        } else {
            mAvatar.setImageDrawable(getResources().getDrawable(R.drawable.zl));
        }


    }


    public void bindView(EMMessage emMessage, boolean showTimestamp) {
        updateMessage(emMessage);
        updateTimestamp(emMessage, showTimestamp);
    }

    private void updateTimestamp(EMMessage emMessage, boolean showTimestamp) {
        if (showTimestamp) {
            mTimestamp.setVisibility(VISIBLE);
            //更新时间戳
            long msgTime = emMessage.getMsgTime();
            String timestampString = DateUtils.getTimestampString(new Date(msgTime));
            mTimestamp.setText(timestampString);
        } else {
            mTimestamp.setVisibility(GONE);
        }

    }

    private void updateMessage(EMMessage emMessage) {
        //刷新文本
        if (emMessage.getBody() instanceof EMTextMessageBody) {
            mMessage.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
        } else {
            mMessage.setText(getContext().getString(R.string.no_text_message));
        }
    }
}
