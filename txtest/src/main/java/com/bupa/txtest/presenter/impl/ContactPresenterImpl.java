package com.bupa.txtest.presenter.impl;

import com.bupa.txtest.database.Contact;
import com.bupa.txtest.database.DatabaseManager;
import com.bupa.txtest.model.ContactListItem;
import com.bupa.txtest.presenter.ContactPresenter;
import com.bupa.txtest.utils.ThreadUtils;
import com.bupa.txtest.view.ContactView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: l on 2017/2/6 15:38
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ContactPresenterImpl implements ContactPresenter {
    public static final String TAG = "ContactPresenterImpl";

    public ContactView mContactView;

    private List<ContactListItem> mContactListItems;

    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
        mContactListItems = new ArrayList<>();
    }
    // 添加联系人
    @Override
    public void loadContacts() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //将本地联系人清除
                    DatabaseManager.getInstance().deleteAllContacts();


                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //将联系人列表数据转换List<ContactListItem>
                    for (int i = 0; i < usernames.size(); i++) {
                        String userName = usernames.get(i);
                        ContactListItem item = new ContactListItem();
                        item.userName = userName;

                        //如果是首字符相同，那么从第二个元素开始就不要显示首字符
                        if (isInSameGroup(i, item)) {
                            item.showFirstLetter = false;
                        } else {
                            item.showFirstLetter = true;
                        }
                        //添加到数据集合
                        mContactListItems.add(item);

                        //将用户名 转换 Contact，存入数据库
                        Contact contact = new Contact();
                        contact.setUserName(userName);
                        DatabaseManager.getInstance().save(contact);

                    }

                    //加载成功，通知View层
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onLoadContactsSuccess();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onLoadContactsFailed();
                        }
                    });
                }
            }
        });
    }

    private boolean isInSameGroup(int i, ContactListItem item) {
        return i > 0  && item.getFirstLetter().equals(mContactListItems.get(i - 1).getFirstLetter());
    }
    // 获取总的联系人
    @Override
    public List<ContactListItem> getContacts() {
        return mContactListItems;
    }
    // 更新联系人列表
    @Override
    public void refreshContacts() {
        // 先清空老数据
        mContactListItems.clear();
        // 重新加载联系人
        loadContacts();
    }

    /**
     * 实现删除好友
     * @param userName
     */
    @Override
    public void deleteFriend(final String userName) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(userName);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendFailed();
                        }
                    });
                }
            }
        });
    }
}

