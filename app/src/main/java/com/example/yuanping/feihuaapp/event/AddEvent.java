package com.example.yuanping.feihuaapp.event;

import com.avos.avoscloud.im.v2.AVIMMessage;

/**
 * Created by yuanping on 2/7/18.
 * 有请求添加好友的事件
 */

public class AddEvent {
    public AVIMMessage msg;
    public AddEvent(AVIMMessage msg) {
        this.msg = msg;
    }
}
