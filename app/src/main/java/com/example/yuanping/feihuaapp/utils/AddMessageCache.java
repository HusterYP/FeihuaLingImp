package com.example.yuanping.feihuaapp.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.example.yuanping.feihuaapp.App;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.db.MessageDatabaseHelper;
import com.example.yuanping.feihuaapp.event.AddEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanping on 2/12/18.
 * 待处理的好友添加消息缓存
 */

public class AddMessageCache {
    //好友添加消息缓存
    private Map<String, String> addMsgCache;
    private static AddMessageCache messageCache;

    private AddMessageCache() {
        addMsgCache = new HashMap<>();
    }

    public static synchronized AddMessageCache getInstance() {
        if (null == messageCache) {
            messageCache = new AddMessageCache();
        }
        return messageCache;
    }

    //同步数据库中的数据到内存,其初始化在Leading界面
    public void init() {
        SQLiteDatabase database = MessageDatabaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = database.query(Constant.DB_ADD_REQUEST_TABLE,
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                addMsgCache.put(cursor.getString(cursor.getColumnIndex(Constant.DB_OBJECT_ID)),
                        cursor.getString(cursor.getColumnIndex(Constant.DB_OBJECT_ID)));
            } while (cursor.moveToNext());
        }
    }

    public boolean hasNewFriends() {
        return addMsgCache.size() > 0;
    }

    public List<String> getAddMsgCache() {
        if (addMsgCache != null && addMsgCache.size() != 0) {
            List list = new ArrayList();
            Iterator iterator = addMsgCache.entrySet().iterator();
            while (iterator.hasNext()) {
                list.add(((Map.Entry) iterator.next()).getValue());
            }
            return list;
        }
        return null;
    }

    public void cacheAll(List<AVIMMessage> list) {
        for (AVIMMessage msg : list) {
            if (!isContains(msg)) {
                addMsgCache.put(msg.getFrom(), msg.getFrom());
                syncToDB(msg);
                EventBus.getDefault().post(new AddEvent(msg));
            }
        }
    }

    public void clearAll() {
        if (addMsgCache != null && addMsgCache.size() != 0) {
            addMsgCache.clear();
            deleteAllDB();
        }
    }

    public void cacheSingle(AVIMMessage msg) {
        if (!isContains(msg)) {
            addMsgCache.put(msg.getFrom(), msg.getFrom());
            syncToDB(msg);
        }
    }

    public void removeSingle(AVIMMessage msg) {
        if (isContains(msg)) {
            addMsgCache.remove(msg.getFrom());
            deleteSingleDB(msg);
        }
    }

    public boolean isContains(AVIMMessage msg) {
        return addMsgCache.containsKey(msg.getFrom());
    }

    //同步至数据库
    private void syncToDB(AVIMMessage msg) {
        SQLiteDatabase database = MessageDatabaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.DB_OBJECT_ID, msg.getFrom());
        database.insert(Constant.DB_ADD_REQUEST_TABLE, null, values);
        values.clear();
    }

    //数据库单条删除
    private void deleteSingleDB(AVIMMessage msg) {
        SQLiteDatabase database = MessageDatabaseHelper.getInstance().getWritableDatabase();
        database.delete(Constant.DB_ADD_REQUEST_TABLE, Constant.DB_OBJECT_ID + "=", new String[]{msg.getFrom()});
    }

    //删除全部
    private void deleteAllDB() {
        SQLiteDatabase database = MessageDatabaseHelper.getInstance().getWritableDatabase();
        database.delete(Constant.DB_ADD_REQUEST_TABLE, null, null);
    }
}