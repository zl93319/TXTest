package com.bupa.txtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bupa.txtest.app.Constant;
import com.bupa.txtest.ui.activity.ChatActivity;
import com.bupa.txtest.widget.ConversationListItemView;
import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * 作者: l on 2017/2/7 15:04
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ConversationsAdapter  extends RecyclerView.Adapter<ConversationsAdapter.ConversationListItemViewHolder> {
    public static final String TAG = "ConversationsAdapter";

    private Context mContext;
    private List<EMConversation> mEMConversations;

    public ConversationsAdapter(Context context, List<EMConversation> emConversations) {
        mContext = context;
        mEMConversations = emConversations;
    }

    @Override
    public ConversationListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        return new ConversationListItemViewHolder(new ConversationListItemView(mContext));
    }

    @Override
    public void onBindViewHolder(ConversationListItemViewHolder holder, final int position) {
        holder.mConversationListItemView.bindView(mEMConversations.get(position));
        holder.mConversationListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到聊天界面
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(Constant.Extra.USER_NAME, mEMConversations.get(position).getUserName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEMConversations.size();
    }

    public class ConversationListItemViewHolder extends RecyclerView.ViewHolder {

        public ConversationListItemView mConversationListItemView;

        public ConversationListItemViewHolder(ConversationListItemView itemView) {
            super(itemView);
            mConversationListItemView = itemView;
        }
    }
}
