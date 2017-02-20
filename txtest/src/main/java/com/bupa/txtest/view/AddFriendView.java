package com.bupa.txtest.view;

/**
 * 作者: l on 2017/2/6 22:01
 * 邮箱: xjs250@163.com
 * 描述:
 */
public interface AddFriendView {
    void onSearchSuccess();

    void onSearchFailed();

    void onSearchEmpty();

    void onStartSearch();

    void sendAddFriendRequestSuccess();

    void sendAddFriendRequestFailed();
}
