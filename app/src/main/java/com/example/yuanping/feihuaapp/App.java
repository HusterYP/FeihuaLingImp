package com.example.yuanping.feihuaapp;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.friends.AddRequest;
import com.example.yuanping.feihuaapp.utils.CustomConversationEventHandler;
import com.example.yuanping.feihuaapp.utils.CustomMessageHandler;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by yuanping on 1/1/18.
 */

public class App extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        User.alwaysUseSubUserClass(User.class);
        AVObject.registerSubclass(AddRequest.class);
        AVUser.registerSubclass(User.class);
        // 节省流量
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.initialize(this, Constant.APP_ID, Constant.APP_KEY);
        //开启未读消息
        LCChatKit.getInstance().init(this, Constant.APP_ID, Constant.APP_KEY);
        AVIMClient.setUnreadNotificationEnabled(true);
        AVIMMessageManager.setConversationEventHandler(CustomConversationEventHandler.getInstance());
        //消息接受测试
        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, new CustomMessageHandler());
    }
}
