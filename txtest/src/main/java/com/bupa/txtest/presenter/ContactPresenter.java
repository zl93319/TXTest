package com.bupa.txtest.presenter;

import com.bupa.txtest.model.ContactListItem;

import java.util.List;

/**
 * 作者: l on 2017/2/6 15:39
 * 邮箱: xjs250@163.com
 * 描述:
 */
public interface ContactPresenter {
    // 加载联系人
    void loadContacts();
    // 获取联系人
    List<ContactListItem> getContacts();
    // 刷新联系人
    void refreshContacts();
    // 删除好友
    void deleteFriend(String userName);
}
