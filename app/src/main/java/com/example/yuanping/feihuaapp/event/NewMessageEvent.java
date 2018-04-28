package com.example.yuanping.feihuaapp.event;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * Created by yuanping on 2/7/18.
 */

public class NewMessageEvent {
    public AVIMConversation conversation;
    public AVIMClient client;

    public NewMessageEvent(AVIMConversation conversation, AVIMClient client) {
        this.conversation = conversation;
        this.client = client;
    }
}
