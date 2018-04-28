package com.example.yuanping.feihuaapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;

import com.example.yuanping.feihuaapp.App;
import com.example.yuanping.feihuaapp.Constant;

/**
 * Created by yuanping on 2/17/18.
 * 消息存储数据库
 */

public class MessageDatabaseHelper extends SQLiteOpenHelper {
    private static final String dbName = "Message.db";
    private static final int version = 1;
    private static MessageDatabaseHelper INSTANCE;
    //好友,暂时只缓存了用户的objectId,因为考虑到用户的信息可能会改变
    private static final String CREATE_FRIENDS = "create table  " + Constant.DB_FRIENDS_TABLE
            + "if not exists (" +
            "id integer primary key autoincrement," +
            "objectId text";
    //待添加好友,暂时只缓存了用户的objectId,因为考虑到用户的信息可能会改变
    private static final String CREATE_ADDREQUEST = "create table " + Constant.DB_ADD_REQUEST_TABLE
            + " if not exists (" +
            "id integer primary key autoincrement," +
            "objectId text";

    private MessageDatabaseHelper(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FRIENDS);
        db.execSQL(CREATE_ADDREQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static MessageDatabaseHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageDatabaseHelper(App.context);
        }
        return INSTANCE;
    }
}
