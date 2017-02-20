package com.bupa.txtest.model;

/**
 * 作者: l on 2017/2/6 15:31
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class ContactListItem {
    public static final String TAG = "ContactListItem";

    public String avatar;

    public String userName;

    public boolean showFirstLetter;//是否显示首字符

    public String getFirstLetter() {
        return String.valueOf(userName.charAt(0)).toUpperCase();
    }
}
