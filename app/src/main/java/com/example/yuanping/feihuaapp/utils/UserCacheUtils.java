package com.example.yuanping.feihuaapp.utils;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 * 1、本地存储
 * 2、避免内存、外存占用过多
 */
public class UserCacheUtils {

    private static Map<String, User> userMap;

    static {
        userMap = new HashMap<String, User>();
    }

    public static User getCachedUser(String objectId) {
        return userMap.get(objectId);
    }

    public static boolean hasCachedUser(String objectId) {
        return userMap.containsKey(objectId);
    }

    public static void cacheUser(User user) {
        if (null != user && !TextUtils.isEmpty(user.getObjectId())) {
            userMap.put(user.getObjectId(), user);
        }
    }

    public static void cacheUsers(List<User> users) {
        if (null != users) {
            for (User user : users) {
                cacheUser(user);
            }
        }
    }

    public static void fetchUsers(List<String> ids) {
        fetchUsers(ids, null);
    }

    public static void fetchUsers(final List<String> ids, final CacheUserCallback cacheUserCallback) {
        Set<String> uncachedIds = new HashSet<String>();
        for (String id : ids) {
            if (!userMap.containsKey(id)) {
                uncachedIds.add(id);
            }
        }

        if (uncachedIds.isEmpty()) {
            if (null != cacheUserCallback) {
                cacheUserCallback.done(getUsersFromCache(ids), null);
                return;
            }
        }

        AVQuery<User> q = User.getQuery(User.class);
        q.whereContainedIn(Constant.OBJECT_ID, uncachedIds);
        q.setLimit(1000);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (null == e) {
                    for (User user : list) {
                        userMap.put(user.getObjectId(), user);
                    }
                }
                if (null != cacheUserCallback) {
                    cacheUserCallback.done(getUsersFromCache(ids), e);
                }
            }
        });
    }

    public static List<User> getUsersFromCache(List<String> ids) {
        List<User> userList = new ArrayList<User>();
        for (String id : ids) {
            if (userMap.containsKey(id)) {
                userList.add(userMap.get(id));
            }
        }
        return userList;
    }

    public static abstract class CacheUserCallback {
        public abstract void done(List<User> userList, Exception e);
    }

    //通过用户名查询用户
    public static void findUserInBackgroud(String name, final FindUser findUser) {
        if (!TextUtils.isEmpty(name)) {
            AVQuery<User> query = User.getQuery(User.class);
            query.whereContains(User.USERNAME, name);
            query.findInBackground(new FindCallback<User>() {
                @Override
                public void done(List<User> list, AVException e) {
                    if (e == null) {
                        findUser.findUserInBackgroud(list);
                    } else {
                        ExceptionUtil.filterException(e);
                    }
                }
            });
        }
    }

    public interface FindUser {
        void findUserInBackgroud(List<User> list);
    }
}
