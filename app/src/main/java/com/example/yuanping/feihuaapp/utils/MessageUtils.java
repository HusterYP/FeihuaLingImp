package com.example.yuanping.feihuaapp.utils;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.bean.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by yuanping on 2/7/18.
 * 消息管理
 */

public class MessageUtils {
    //当添加好友时,发送标志位
    public static void sendAddRequest(User toUser) {
        final User fromUser = User.getCurrentUser();
        List<String> chatId = Arrays.asList(toUser.getObjectId());
        LCChatKit.getInstance().getClient().createConversation(chatId,
                fromUser.getUsername() + "&&" + toUser.getUsername(),
                null, false, true,
                new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (e == null) {
                            AVIMTextMessage reply = new AVIMTextMessage();
                            Map<String, Object> map = new HashMap<>();
                            map.put(Constant.ATTR, Constant.ATTR_ADD_REQUEST);
                            map.put(Constant.ATTR_ADD_FROM_USER_NAME, fromUser.getUsername());
                            reply.setAttrs(map);
                            sendMsg(avimConversation, reply);
                        } else {
                            ExceptionUtil.filterException(e);
                        }
                    }
                });
    }

    public static void sendMsg(AVIMConversation conversation, AVIMTextMessage msg) {
        conversation.sendMessage(msg, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e != null) {
                    ExceptionUtil.filterException(e);
                }
            }
        });
    }

    //对方回复是否同意添加好友;
    public static void sendAddReply(User toUser, final boolean flag) {
        final User fromUser = User.getCurrentUser();
        List<String> chatId = Arrays.asList(toUser.getObjectId());
        LCChatKit.getInstance().getClient().createConversation(chatId,
                fromUser.getUsername() + "&&" + toUser.getUsername(),
                null, false, true,
                new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (e == null) {
                            AVIMTextMessage reply = new AVIMTextMessage();
                            Map<String, Object> map = new HashMap<>();
                            if (flag) {
                                map.put(Constant.ATTR, Constant.ATTR_ADD_AGREE_REPLY);
                            } else {
                                map.put(Constant.ATTR, Constant.ATTR_ADD_REJECT_REPLY);
                            }
                            map.put(Constant.ATTR_ADD_FROM_USER_NAME, fromUser.getUsername());
                            reply.setAttrs(map);
                            sendMsg(avimConversation, reply);
                        } else {
                            ExceptionUtil.filterException(e);
                        }
                    }
                });
    }
}
