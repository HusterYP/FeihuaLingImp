package com.example.yuanping.feihuaapp.test;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SendCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.utils.ExceptionUtil;

import java.util.Arrays;

/**
 * Created by yuanping on 2/6/18.
 * 消息推送,如:当发送了一条添加好友请求后,再给对方发送一条推送
 * 弃用
 */

public class PushUtils {
    public static void addRequestPush(User fromUser, User toUser) {
        Log.d("@HusterYP", String.valueOf("推送发送啦!!" +
                fromUser.getUsername() + ".." +
                toUser.getUsername() + ".." + fromUser.getInstallationId()
                + ".." + toUser.getUsername()));

        AVPush push = new AVPush();
        AVQuery<AVInstallation> query = AVInstallation.getQuery();
//        query.whereEqualTo("installationId", toUser.getInstallationId());
        query.whereContains(User.USERNAME, toUser.getUsername());
        push.setQuery(query);
        push.setChannel(Constant.PUBLIC);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.ACTION, Constant.RECEIVER_FILTER);
        jsonObject.put(Constant.FROM_USER, fromUser.getUsername());
        push.setData(jsonObject);
        push.setPushToAndroid(true);
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(AVException e) {
                if (e != null)
                    ExceptionUtil.filterException(e);
            }
        });
    }

    public static void addRequestPush2(User fromUser, final User toUser) {
        final AVIMClient tom = AVIMClient.getInstance(fromUser.getUsername());
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    client.createConversation(Arrays.asList(toUser.getUsername()), "猫和老鼠", null,
                            new AVIMConversationCreatedCallback() {
                                @Override
                                public void done(AVIMConversation conv, AVIMException e) {
                                    if (e == null) {
                                        AVIMTextMessage msg = new AVIMTextMessage();
                                        msg.setText("耗子，起床！");

                                        AVIMMessageOption messageOption = new AVIMMessageOption();
                                        messageOption.setPushData("自定义离线消息推送内容");
                                        conv.sendMessage(msg, messageOption, new AVIMConversationCallback() {
                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    // 发送成功
                                                    Log.d("@HusterYP", String.valueOf("自定义离线推送成功.."));

                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }
}
