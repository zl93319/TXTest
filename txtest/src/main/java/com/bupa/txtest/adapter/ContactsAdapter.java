package com.bupa.txtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bupa.txtest.model.ContactListItem;
import com.bupa.txtest.widget.ContactListItemView;

import java.util.List;

/**
 * 作者: l on 2017/2/6 15:26
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactListItemViewHolder>{
    public static final String TAG = "ContactsAdapter";
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private List<ContactListItem> mContactListItems;

    public ContactsAdapter(Context context, List<ContactListItem> contactListItems) {
        mContext = context;
        mContactListItems = contactListItems;
    }

    /**
     * 创建ViewHolder, ViewHolder应该hold住条目的view
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ContactListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactListItemViewHolder(new ContactListItemView(mContext));
    }

    /**
     * 获取position对应位置的数据，来刷新holder hold住的item
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ContactListItemViewHolder holder, int position) {
        final ContactListItem item = mContactListItems.get(position);
        holder.mContactListItemView.bindView(item);

        holder.mContactListItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: ");
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLongClick(item.userName);
                }
                return true;
            }
        });

        holder.mContactListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(item.userName);
                }
            }
        });
    }

    /**
     * 返回item个数 跟ListView getCount是一样
     * @return
     */
    @Override
    public int getItemCount() {
        return mContactListItems.size();
    }

    public class ContactListItemViewHolder extends RecyclerView.ViewHolder {

        public ContactListItemView mContactListItemView;

        public ContactListItemViewHolder(ContactListItemView itemView) {
            super(itemView);
            mContactListItemView = itemView;
        }
    }

    public interface OnItemClickListener {
        void onLongClick(String userName);

        void onItemClick(String userName);
    }
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mOnItemClickListener = clickListener;
    }
}
