package com.bupa.txtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.event.AddFriendEvent;
import com.bupa.txtest.model.AddFriendListItem;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者: l on 2017/2/6 20:59
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class AddFriendListItemView extends RelativeLayout {
    public static final String TAG = "AddFriendListItemView";

    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.timestamp)
    TextView mTimestamp;
    @BindView(R.id.add)
    Button mAdd;

    public AddFriendListItemView(Context context) {
        this(context, null);
    }

    public AddFriendListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_add_friend_list_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(AddFriendListItem addFriendListItem) {
        mUserName.setText(addFriendListItem.userName);
        mTimestamp.setText(addFriendListItem.timestamp);
        if (addFriendListItem.added) {
            // 如果是已添加的好友 设置按钮未激活
            mAdd.setEnabled(false);
            // 提示已添加
            mAdd.setText("已添加");
        } else {
            // 可以点击
            mAdd.setEnabled(true);
            // 提示添加
            mAdd.setText("添加");
        }
    }

    @OnClick(R.id.add)
    public void onClick() {
        //发送添加好友的请求
        String reason = getContext().getString(R.string.add_friend_reason);
        AddFriendEvent event = new AddFriendEvent(mUserName.getText().toString(), reason);
        EventBus.getDefault().post(event);
    }
}
