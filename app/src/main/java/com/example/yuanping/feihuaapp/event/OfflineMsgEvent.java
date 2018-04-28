package com.example.yuanping.feihuaapp.event;

import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * Created by yuanping on 2/12/18.
 * 离线消息事件
 */

public class OfflineMsgEvent {
    public AVIMConversation conversation;

    public OfflineMsgEvent(AVIMConversation conversation) {
        this.conversation = conversation;
    }
}
