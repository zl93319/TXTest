package com.bupa.txtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bupa.txtest.model.AddFriendListItem;
import com.bupa.txtest.widget.AddFriendListItemView;

import java.util.List;

/**
 * 作者: l on 2017/2/6 22:15
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendListItemViewHolder> {


    private Context mContext;

    private List<AddFriendListItem> mAddFriendListItems;

    public AddFriendAdapter(Context context, List<AddFriendListItem> addFriendListItems) {
        mContext = context;
        mAddFriendListItems = addFriendListItems;
    }

    @Override
    public AddFriendListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddFriendListItemViewHolder(new AddFriendListItemView(mContext));
    }

    @Override
    public void onBindViewHolder(AddFriendListItemViewHolder holder, int position) {
        //获取holder hold住的view，将数据传给它进行渲染
        holder.mAddFriendListItemView.bindView(mAddFriendListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mAddFriendListItems.size();
    }

    public class AddFriendListItemViewHolder extends RecyclerView.ViewHolder {

        public AddFriendListItemView mAddFriendListItemView;

        public AddFriendListItemViewHolder(AddFriendListItemView itemView) {
            super(itemView);
            mAddFriendListItemView =  itemView;
        }
    }
}