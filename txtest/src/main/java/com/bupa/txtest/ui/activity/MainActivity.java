package com.bupa.txtest.ui.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bupa.txtest.R;
import com.bupa.txtest.adapter.EMMessageListenerAdapter;
import com.bupa.txtest.factory.FragmentFactory;
import com.bupa.txtest.utils.ThreadUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.bottom_bar)
    BottomBar mBottomBar;
    private FragmentManager mFragmentManager;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }


    @Override
    protected void init() {
        super.init();
        mFragmentManager = getSupportFragmentManager();
        mBottomBar.setOnTabSelectListener(mOnTabSelectListener);
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
//        updateBadge();
    }

    private void updateBadge() {
        BottomBarTab tabWithId = mBottomBar.getTabWithId(R.id.conversations);
        //获取未读消息的总数
        int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        tabWithId.setBadgeCount(unreadMsgsCount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
    }

    private OnTabSelectListener mOnTabSelectListener = new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, FragmentFactory.getInstance().getFragment(tabId));
            fragmentTransaction.commit();
        }
    };

    private EMMessageListenerAdapter mEMMessageListener = new EMMessageListenerAdapter() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateBadge();//更新未读消息总数
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }
}
