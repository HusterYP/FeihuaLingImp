package com.example.yuanping.feihuaapp.utils;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.example.yuanping.feihuaapp.event.OfflineMsgEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by yuanping on 2/8/18.
 */

public class CustomConversationEventHandler extends AVIMConversationEventHandler {

    private static CustomConversationEventHandler eventHandler;

    public static synchronized CustomConversationEventHandler getInstance() {
        if (null == eventHandler) {
            eventHandler = new CustomConversationEventHandler();
        }
        return eventHandler;
    }

    private CustomConversationEventHandler() {
    }

    @Override
    public void onLastReadAtUpdated(AVIMClient client, AVIMConversation conversation) {
        Log.d("@HusterYP", String.valueOf("Last ReadAtUpdated"));

    }

    @Override
    public void onUnreadMessagesCountUpdated(AVIMClient client, AVIMConversation conversation) {
        EventBus.getDefault().post(new OfflineMsgEvent(conversation));
        conversation.read();
    }

    @Override
    public void onMemberLeft(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {

    }

    @Override
    public void onMemberJoined(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {

    }

    @Override
    public void onKicked(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

    }

    @Override
    public void onInvited(AVIMClient avimClient, AVIMConversation avimConversation, String s) {

    }
}
