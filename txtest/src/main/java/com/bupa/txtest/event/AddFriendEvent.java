package com.bupa.txtest.event;

/**
 * 作者: l on 2017/2/6 21:43
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class AddFriendEvent {
    public static final String TAG = "AddFriendEvent";

    public String userName;

    public String reason;

    public AddFriendEvent(String userName, String reason) {
        this.userName = userName;
        this.reason = reason;
    }
}
