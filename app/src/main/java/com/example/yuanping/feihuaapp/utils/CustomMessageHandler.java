package com.example.yuanping.feihuaapp.utils;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.event.AddAgreeEvent;
import com.example.yuanping.feihuaapp.event.AddEvent;
import com.example.yuanping.feihuaapp.event.AddRejectEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yuanping on 2/7/18.
 */

public class CustomMessageHandler extends AVIMTypedMessageHandler<AVIMTextMessage> {
    @Override
    public void onMessage(AVIMTextMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessage(message, conversation, client);
        String event = (String) message.getAttrs().get(Constant.ATTR);
        if (event != null) {
            switch (event) {
                case Constant.ATTR_ADD_REQUEST: {
                    AddMessageCache.getInstance().cacheSingle(message);
                    AddEvent addEvent = new AddEvent(message);
                    EventBus.getDefault().post(addEvent);
                    conversation.read();
                }
                break;
                case Constant.ATTR_COMMON_MSG: {
                    Log.d("@HusterYP", String.valueOf("Common Msg " + message.getText()));
                }
                break;
                //同意添加好友回执
                case Constant.ATTR_ADD_AGREE_REPLY: {
                    EventBus.getDefault().post(new AddAgreeEvent());
                    conversation.read();
                }
                break;
                //拒绝添加好友回执
                case Constant.ATTR_ADD_REJECT_REPLY:{
                    EventBus.getDefault().post(new AddRejectEvent());
                    conversation.read();
                }
                break;
            }
        } else {
            Log.d("@HusterYP", String.valueOf("Error Null Event"));
        }
    }
}
