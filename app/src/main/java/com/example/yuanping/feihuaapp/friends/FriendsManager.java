package com.example.yuanping.feihuaapp.friends;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.yuanping.feihuaapp.App;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseAsynctask;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.utils.ExceptionUtil;
import com.example.yuanping.feihuaapp.utils.MessageUtils;
import com.example.yuanping.feihuaapp.utils.Utils;

/**
 * Created by yuanping on 2/4/18.
 * 与好友有关的操作封装
 */

public class FriendsManager {
    private static FriendsManager friendsManager;

    public FriendsManager() {
    }

    public static synchronized FriendsManager getInstance() {
        if (friendsManager == null) {
            friendsManager = new FriendsManager();
        }
        return friendsManager;
    }

    public void createAddRequestInBackgroud(final User user) {
        new SendAddRequest(user).execute();
    }

    //发送添加请求内部静态类
    static class SendAddRequest extends BaseAsynctask {
        private User user;

        public SendAddRequest(User user) {
            this.user = user;
        }

        private void createAddRequest(User toUser) throws Exception {
            User curUser = User.getCurrentUser();
            AVQuery<AddRequest> q = AVObject.getQuery(AddRequest.class);
            q.whereEqualTo(AddRequest.FROM_USER, curUser);
            q.whereEqualTo(AddRequest.TO_USER, toUser);
            q.whereEqualTo(AddRequest.STATUS, AddRequest.STATUS_WAIT);
            int count = 0;
            try {
                count = q.count();
            } catch (AVException e) {
                if (e.getCode() == AVException.OBJECT_NOT_FOUND) {
                    count = 0;
                } else {
                    throw e;
                }
            }
            if (count > 0) {
                // 抛出异常，然后提示用户
                throw new IllegalStateException(App.context.getString(R.string.request_has_sended));
            } else {
                AddRequest add = new AddRequest();
                add.setFromUser(curUser);
                add.setToUser(toUser);
                add.setStatus(AddRequest.STATUS_WAIT);
                add.setIsRead(false);
                add.save();
            }
        }

        @Override
        public void doOnPrevious() {
            //调起对话框
        }

        @Override
        public void doInBackgroud() throws Exception {
            createAddRequest(user);
            //发送标志位
            MessageUtils.sendAddRequest(user);
        }

        @Override
        public void doPost(Exception exception) {
            if (exception != null) {
                ExceptionUtil.filterException(exception);
            } else {
                Utils.toast(R.string.send_successfully);
                //发送一条推送
//                PushUtils.addRequestPush2(User.getCurrentUser(), user);
            }
        }
    }

    public void agreeAddRequest(final AddRequest addRequest, final SaveCallback saveCallback) {
        addFriend(addRequest.getFromUser().getObjectId(), new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    if (e.getCode() == AVException.DUPLICATE_VALUE) {
                        addRequest.setStatus(AddRequest.STATUS_DONE);
                        addRequest.saveInBackground(saveCallback);
                    } else {
                        saveCallback.done(e);
                    }
                } else {
                    addRequest.setStatus(AddRequest.STATUS_DONE);
                    addRequest.saveInBackground(saveCallback);
                }
            }
        });
    }

    public static void addFriend(String friendId, final SaveCallback saveCallback) {
        User user = User.getCurrentUser();
        user.followInBackground(friendId, new FollowCallback() {
            @Override
            public void done(AVObject object, AVException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }

    public void findAddRequests(int skip, int limit, FindCallback findCallback) {
        User user = User.getCurrentUser();
        AVQuery<AddRequest> q = AVObject.getQuery(AddRequest.class);
        q.include(AddRequest.FROM_USER);
        q.skip(skip);
        q.limit(limit);
        q.whereEqualTo(AddRequest.TO_USER, user);
        q.orderByDescending(AVObject.CREATED_AT);
        q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        q.findInBackground(findCallback);
    }
}

