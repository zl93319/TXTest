package com.bupa.txtest.model;

import cn.bmob.v3.BmobUser;

/**
 * 作者: l on 2017/2/6 10:03
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class User extends BmobUser {
    public static final String TAG = "User";

    public String gender;

    public String age;
    public  String mAvatar;
    public User(String userName, String password) {
        setUsername(userName);
        setPassword(password);
    }
}
