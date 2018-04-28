package com.example.yuanping.feihuaapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yuanping.feihuaapp.bean.Poetry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yuanping on 2/2/18.
 * 诗词数据库操作
 */

public class PoetryDatabaseHelper {

    private static PoetryDatabaseHelper INSTANCE;
    private SQLiteDatabase database;

    private PoetryDatabaseHelper(Context context) {
        database = initDatabase(context);
    }

    public static PoetryDatabaseHelper getInstance(Context context) {
        INSTANCE = new PoetryDatabaseHelper(context);
        return INSTANCE;
    }

    //将本地数据库资源导入
    public static SQLiteDatabase initDatabase(Context context) {
        InputStream in = context.getClass().getResourceAsStream("/assets/bhj.db");
        final String path = "data/data/com.example.yuanping.feihuaapp/bhj.db";
        File file = new File(path);
        FileOutputStream out = null;
        if (file.exists()) {
            return SQLiteDatabase.openOrCreateDatabase(path, null);
        } else {
            try {
                out = new FileOutputStream(path);
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = in.read(bytes)) != -1) {
                    out.write(bytes, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null && in != null) {
                    try {
                        out.close();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    public Poetry query(String key) {
        key = split(key);
        String sql = "select * from fhl where yuanwen2 like '%" + key + "%'";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex("timu"));
            String author = cursor.getString(cursor.getColumnIndex("zuozhe"));
            String dynasty = cursor.getString(cursor.getColumnIndex("chaodai"));
            String original = cursor.getString(cursor.getColumnIndex("yuanwen"));
            String original2 = cursor.getString(cursor.getColumnIndex("yuanwen2"));
            return new Poetry(title, author, dynasty, original, original2);
        }
        return null;
    }

    //去除逗号和句号
    public String split(String key) {
        if (key.contains("。")) {
            key = key.split("。")[0];
        }
        if (key.contains(".")) {
            key = key.split("\\.")[0];
        }
        if (key.contains("，")) {
            return splitBySeparator(key, "，");
        }
        if (key.contains(",")) {
            return splitBySeparator(key, ",");
        }
        return key;
    }

    public String splitBySeparator(String key, String separator) {
        StringBuilder builder;
        String[] temp = key.split(separator);
        builder = new StringBuilder();
        for (int i = 0; i < temp.length; i++) {
            builder.append(temp[i]);
        }
        return builder.toString();
    }
}
