package com.bupa.txtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bupa.txtest.R;
import com.bupa.txtest.model.ContactListItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者: l on 2017/2/6 15:32
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ContactListItemView extends RelativeLayout {
    public static final String TAG = "ContactListItemView";
    @BindView(R.id.first_letter)
    TextView mFirstLetter;
    @BindView(R.id.user_name)
    TextView mUserName;


    public ContactListItemView(Context context) {
        this(context, null);
    }

    public ContactListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
//        View.inflate()
        LayoutInflater.from(getContext()).inflate(R.layout.view_list_contact, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(ContactListItem item) {
        mUserName.setText(item.userName);
        if (item.showFirstLetter) {
            mFirstLetter.setVisibility(VISIBLE);
            mFirstLetter.setText(item.getFirstLetter());
        } else {
            mFirstLetter.setVisibility(GONE);
        }
    }
}

