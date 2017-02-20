package com.bupa.txtest.factory;

import android.support.v4.app.Fragment;

import com.bupa.txtest.R;
import com.bupa.txtest.ui.fragment.ContactFragment;
import com.bupa.txtest.ui.fragment.ConversationFragment;
import com.bupa.txtest.ui.fragment.DynamicFragment;

/**
 * 作者: l on 2017/2/6 14:38
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class FragmentFactory {
    public static final String TAG = "FragmentFactory";

    private static FragmentFactory sFragmentFactory;

    private ConversationFragment mConversationFragment;
    private ContactFragment mContactFragment;
    private DynamicFragment mDynamicFragment;

    public static FragmentFactory getInstance() {
        if (sFragmentFactory == null) {
            synchronized (FragmentFactory.class) {
                if (sFragmentFactory == null) {
                    sFragmentFactory = new FragmentFactory();
                }
            }
        }
        return sFragmentFactory;
    }

    public Fragment getFragment(int tabId) {
        switch (tabId) {
            case R.id.conversations:
                return getConversationFragment();
            case R.id.contacts:
                return getContactFragment();
            case R.id.dynamic:
                return getDynamicFragment();
        }
        return null;
    }

    /**
     * 消息
     * @return
     */
    private ConversationFragment getConversationFragment() {
        if (mConversationFragment == null) {
            mConversationFragment = new ConversationFragment();
        }
        return mConversationFragment;
    }

    /**
     * 联系人
     * @return
     */
    private ContactFragment getContactFragment() {
        if (mContactFragment == null) {
            mContactFragment = new ContactFragment();
        }
        return mContactFragment;
    }

    /**
     * 动态
     * @return
     */
    private DynamicFragment getDynamicFragment() {
        if (mDynamicFragment == null) {
            mDynamicFragment = new DynamicFragment();
        }
        return mDynamicFragment;
    }
}
