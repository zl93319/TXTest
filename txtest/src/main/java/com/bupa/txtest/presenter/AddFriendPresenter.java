package com.bupa.txtest.presenter;

import com.bupa.txtest.model.AddFriendListItem;

import java.util.List;

/**
 * 作者: l on 2017/2/6 22:04
 * 邮箱: xjs250@163.com
 * 描述:
 */
public interface AddFriendPresenter {
    void search(String keyword);

    List<AddFriendListItem> getFriends();

    void onDestroy();
}
