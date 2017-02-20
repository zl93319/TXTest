package com.bupa.txtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bupa.txtest.widget.ReceiveMessageItemView;
import com.bupa.txtest.widget.SendMessageItemView;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.List;

/**
 * 作者: l on 2017/2/7 00:17
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    public static final String TAG = "MessageListAdapter";

    private Context mContext;

    private List<EMMessage> mMessages;

    private static final int ITEM_TYPE_SEND = 0;

    private static final int ITEM_TYPE_RECEIVE = 1;

    public MessageListAdapter(Context context, List<EMMessage> messages) {
        mContext = context;
        mMessages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_SEND) {
            return new SendMessageItemViewHolder(new SendMessageItemView(mContext));
        } else {
            return new ReceivedMessageItemViewHolder(new ReceiveMessageItemView(mContext));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        boolean showTimestamp = false;
        //如果是第一个位置的消息，或者两个消息之间的时间隔得很远
        if (position == 0 || isShowTimestamp(position)) {
            showTimestamp = true;
        }
        if (holder instanceof SendMessageItemViewHolder) {
            ((SendMessageItemViewHolder) holder).mSendMessageItemView.bindView(mMessages.get(position), showTimestamp);
        } else {
            ((ReceivedMessageItemViewHolder)holder).mReceiveMessageItemView.bindView(mMessages.get(position), showTimestamp);
        }

    }

    /**
     *
     * @param position
     * @return 如果true显示时间戳，false就不显示
     */
    public boolean isShowTimestamp (int position) {
        //拿到当前消息时间
        long currentItemTimestamp = mMessages.get(position).getMsgTime();
        //拿到上一消息时间
        long preItemTimestamp = mMessages.get(position - 1).getMsgTime();
        return !DateUtils.isCloseEnough(currentItemTimestamp, preItemTimestamp);
    }

    @Override
    public int getItemViewType(int position) {
        return (mMessages.get(position).direct() == EMMessage.Direct.SEND) ? ITEM_TYPE_SEND : ITEM_TYPE_RECEIVE;
    }

    @Override
    public int getItemCount() {
        if (mMessages == null) {
            return 0;
        }
        return mMessages.size();
    }

    public void addNewMessage(EMMessage emMessage) {
        mMessages.add(emMessage);
        notifyDataSetChanged();
    }

    public class SendMessageItemViewHolder extends RecyclerView.ViewHolder {

        private SendMessageItemView mSendMessageItemView;

        public SendMessageItemViewHolder(SendMessageItemView itemView) {
            super(itemView);
            mSendMessageItemView = itemView;
        }
    }

    public class ReceivedMessageItemViewHolder extends RecyclerView.ViewHolder {

        private ReceiveMessageItemView mReceiveMessageItemView;

        public ReceivedMessageItemViewHolder(ReceiveMessageItemView itemView) {
            super(itemView);
            mReceiveMessageItemView = itemView;
        }
    }
}
