package com.bupa.txtest.utils;

/**
 * 创建者:   Leon
 * 创建时间:  2016/11/9 15:40
 * 描述：    TODO
 */
public class StringUtils {
    public static final String TAG = "StringUtils";

    private static final String REGEX_USER_NAME = "^[a-zA-Z]\\w{2,19}$";

    private static final String REGEX_PASSWORD = "^[0-9]{3,20}$";


    public static boolean isValidUserName(String userName) {
        return userName.matches(REGEX_USER_NAME);
    }


    public static boolean isValidPassword(String password) {
        return password.matches(REGEX_PASSWORD);
    }
}
