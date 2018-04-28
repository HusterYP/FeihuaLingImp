package com.example.yuanping.feihuaapp.utils;

import android.text.TextUtils;

import com.example.yuanping.feihuaapp.bean.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanping on 2/5/18.
 * 用户信息的管理类
 */

public class UserUtils {
    //用于暂时缓存待添加好友的用户名
    private static Map<String, String> userNameMap;

    static {
        userNameMap = new HashMap<String, String>();
    }

    //分离出User ID;未完善
    public static String splitUserID(User user) {
        if (user != null) {
            if (user.getMobilePhoneNumber() != null)
                return user.getMobilePhoneNumber();
            else
                return user.getObjectId();
        }
        return "";
    }

    public static void cacheUserName(String userName) {
        if (userName != null && !TextUtils.isEmpty(userName)){
            userNameMap.put(userName,userName);
        }
    }

    public static Map getUserNameMap(){
        return userNameMap;
    }

    public static void removeUserName(String userName){
        if (userName != null && !TextUtils.isEmpty(userName)){
            userNameMap.remove(userName);
        }
    }

    public static void removeAll(){
        userNameMap.clear();
    }
}
