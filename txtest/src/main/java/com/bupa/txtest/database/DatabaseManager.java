package com.bupa.txtest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bupa.txtest.app.Constant;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: l on 2017/2/6 23:21
 * 邮箱: xjs250@163.com
 * 描述:
 */
public class DatabaseManager {
    public static final String TAG = "DatabaseManager";

    public static DatabaseManager sDatabaseManager;
    private DaoSession mDaoSession;
    private Object mContacts;

    private DatabaseManager() {}

    public static DatabaseManager getInstance() {
        if (sDatabaseManager == null) {
            synchronized (DatabaseManager.class) {
                if (sDatabaseManager == null) {
                    sDatabaseManager = new DatabaseManager();
                }
            }
        }
        return sDatabaseManager;
    }

    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, Constant.DATABASE.DATABASE_NAME, null);
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }

    /**
     * 保存联系人
     * @param contact
     */
    public void save(Contact contact) {
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.save(contact);
    }

    /**
     *
     * @return 返回联系人List集合，方便数据比较
     */
    public List<String> getContacts() {
        List<String> contacts = new ArrayList<String>();
        //查询所有的联系人的数据
        ContactDao contactDao = mDaoSession.getContactDao();
        QueryBuilder<Contact> contactQueryBuilder = contactDao.queryBuilder();
        List<Contact> list = contactQueryBuilder.list();
        //转换成字符串集合
        for (int i = 0; i < list.size(); i++) {
            contacts.add(list.get(i).getUserName());
        }
        return contacts;
    }

    /**
     * 删除所有的联系人
     */
    public void deleteAllContacts() {
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.deleteAll();
    }
}
