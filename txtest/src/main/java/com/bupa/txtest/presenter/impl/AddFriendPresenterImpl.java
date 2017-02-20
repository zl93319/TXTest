package com.bupa.txtest.presenter.impl;

import com.bupa.txtest.database.DatabaseManager;
import com.bupa.txtest.event.AddFriendEvent;
import com.bupa.txtest.model.AddFriendListItem;
import com.bupa.txtest.model.User;
import com.bupa.txtest.presenter.AddFriendPresenter;
import com.bupa.txtest.utils.ThreadUtils;
import com.bupa.txtest.view.AddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 作者: l on 2017/2/6 22:00
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class AddFriendPresenterImpl implements AddFriendPresenter {
    public static final String TAG = "AddFriendPresenterImpl";

    private AddFriendView mAddFriendView;

    private List<AddFriendListItem> mAddFriendListItems;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        mAddFriendView = addFriendView;
        mAddFriendListItems = new ArrayList<>();
        EventBus.getDefault().register(this);
    }

    @Override
    public void search(String keyword) {
        BmobQuery<User> query = new BmobQuery<User>();
        //设置查询条件 用户名以keyword开头并且不包含当前用户的
        query.addWhereStartsWith("username", keyword).addWhereNotEqualTo("username", EMClient.getInstance().getCurrentUser());

        //通知View层开始搜索
        mAddFriendView.onStartSearch();

        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mAddFriendView.onSearchEmpty();
                    } else {
                        //查询数据库，将现有的好友查询出来
                        List<String> contacts = DatabaseManager.getInstance().getContacts();

                        //将User转换成ui刷新要用的数据模块AddFriendListItem
                        for (int i = 0; i < list.size(); i++) {
                            User user = list.get(i);
                            AddFriendListItem item = new AddFriendListItem();
                            item.userName = user.getUsername();
                            item.timestamp = user.getCreatedAt();

//                            item.added = false;//默认都没有添加
                            item.added = contacts.contains(user.getUsername());

                            mAddFriendListItems.add(item);
                        }

                        mAddFriendView.onSearchSuccess();
                    }
                } else {
                    mAddFriendView.onSearchFailed();
                }
            }
        });
    }

    @Override
    public List<AddFriendListItem> getFriends() {
        return mAddFriendListItems;
    }

    /**
     * 监听什么事件，就声明什么参数类型
     * 方法要是public
     *
     * ThreadMode.BACKGROUND 如果发送者是在子线程发送的，那么该方法也在同一个子线程处理事件
     * ，如果发送者是在主线程，那么该方法在一个线程池中处理事件（EventBus内部维护一个线程池）
     *
     * ThreadMode.POSTING 发送者在哪个线程发送的，该方法也在哪个线程中处理事件
     *
     * ThreadMode.MAIN 不管发送者在哪个线程发送的，该方法都会在主线程中处理事件
     *
     * ThreadMode.ASYNC 不管发送者在哪个线程发送的，该方法都在EventBus线程池中执行
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void handleAddFriendEvent(AddFriendEvent event) {
        //参数为要添加的好友的username和添加理由
        try {
            EMClient.getInstance().contactManager().addContact(event.userName, event.reason);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAddFriendView.sendAddFriendRequestSuccess();
                }
            });
        } catch (HyphenateException e) {
            e.printStackTrace();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAddFriendView.sendAddFriendRequestFailed();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
