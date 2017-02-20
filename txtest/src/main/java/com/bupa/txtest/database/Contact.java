package com.bupa.txtest.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者: l on 2017/2/6 23:19
 * 邮箱: xj250@163.com
 * 描述:
 */
@Entity
public class Contact {
    @Id
    public Long id;
    public String userName;
    @Generated(hash = 2041396140)
    public Contact(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
    @Generated(hash = 672515148)
    public Contact() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
