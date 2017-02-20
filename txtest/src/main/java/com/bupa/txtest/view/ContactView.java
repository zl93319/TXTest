package com.bupa.txtest.view;

/**
 * 作者: l on 2017/2/6 15:25
 * 邮箱: xjs250@163.com
 * 描述:
 */
public interface ContactView {
    void onLoadContactsSuccess();

    void onLoadContactsFailed();

    void onDeleteFriendSuccess();

    void onDeleteFriendFailed();
}
