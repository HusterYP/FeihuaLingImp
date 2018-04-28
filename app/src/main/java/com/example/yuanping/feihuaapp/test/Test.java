package com.example.yuanping.feihuaapp.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseActivity;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.event.NewMessageEvent;
import com.example.yuanping.feihuaapp.friends.AddRequest;
import com.example.yuanping.feihuaapp.friends.FriendsManager;
import com.example.yuanping.feihuaapp.utils.CustomMessageHandler;
import com.example.yuanping.feihuaapp.utils.ExceptionUtil;
import com.example.yuanping.feihuaapp.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;

/*
* 用于测试
* */
public class Test extends BaseActivity {

    @BindView((R.id.test_input))
    EditText input;
    @BindView(R.id.test_id)
    EditText id;
    @BindView(R.id.test_room_id)
    EditText roomId;
    private AVIMConversation conversation;
//    CustomMessageHandler handler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, handler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
//        handler = new CustomMessageHandler();
        //消息接受测试
//        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, handler);
    }

    @OnClick(R.id.test_add)
    public void onAdd(View view) {
        String name = id.getText().toString();
        AVQuery<User> query = User.getQuery(User.class);
        query.whereContains(User.USERNAME, name);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        User user = list.get(0);
                        FriendsManager.getInstance().createAddRequestInBackgroud(user);
                    } else {
                        Utils.toast(R.string.no_such_user);
                    }
                } else {
                    ExceptionUtil.filterException(e);
                }
            }
        });
    }

    @OnClick(R.id.test_create_conversation)
    public void onCreateConversation(View view) {
        AVIMTextMessage reply = new AVIMTextMessage();
        reply.setText(input.getText().toString());
        //设置需要回执
        AVIMMessageOption option = new AVIMMessageOption();
        option.setReceipt(true);
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.ATTR, Constant.ATTR_COMMON_MSG);
        reply.setAttrs(map);
        //将消息标记为已读
        conversation.read();
        conversation.sendMessage(reply, option, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                Log.d("@HusterYP", String.valueOf("回复成功啦!"));
            }
        });
    }

    private void init(User toUser) {
        List<String> chatId = Arrays.asList(toUser.getObjectId());
        LCChatKit.getInstance().getClient().createConversation(chatId, roomId.getText().toString(),
                null, false, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (e == null) {
                            conversation = avimConversation;
                            Log.d("@HusterYP", String.valueOf(conversation.getConversationId()));
                            initReceiveMsg();
                        } else {
                            Log.d("@HusterYP", String.valueOf("Received Error " + e));
                        }
                    }
                });
    }

    private void initReceiveMsg() {
        conversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (e == null && list.size() > 0) {
                    Log.d("@HusterYP", String.valueOf(list.size() + "..第一条消息.." + list.get(0).getContent()));
                } else {
                    Log.d("@HusterYP", String.valueOf(e + "...MsgSize.." + list.size()));
                }
            }
        });
    }

    @OnClick(R.id.test_create_room)
    public void onCreateRoom(View view) {
        search(id.getText().toString());
    }

    @OnClick(R.id.test_add_ok)
    public void onAddOK(View view) {
        Log.d("@HusterYP", String.valueOf("同意之前"));
        FriendsManager.getInstance().findAddRequests(0, 20, new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (e != null && list.size() != 0) {
                    Log.d("@HusterYP", String.valueOf("FindAddRequest Error " + e));
                } else {
                    Log.d("@HusterYP", String.valueOf("AddReqeust .. " + ((AddRequest) list.get(0)).toString()));
                    FriendsManager.getInstance().agreeAddRequest((AddRequest) list.get(0), new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e != null) {
                                Log.d("@HusterYP", String.valueOf("Error.." + e));
                            } else {
                                Log.d("@HusterYP", String.valueOf("同意成功!"));
                            }

                        }
                    });
                }
            }

            @Override
            protected void internalDone0(Object o, AVException e) {

            }
        });
    }

    public void search(String name) {
        AVQuery<User> query = User.getQuery(User.class);
        query.whereContains(User.USERNAME, name);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        init(list.get(0));
                    } else {
                        Utils.toast(R.string.no_such_user);
                    }
                } else {
                    ExceptionUtil.filterException(e);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(NewMessageEvent event) {
        Log.d("@HusterYP", String.valueOf("接收到消息啦  onEvent"));

//        AVIMTextMessage reply = new AVIMTextMessage();
//        reply.setText(input.getText().toString());
//        event.conversation.sendMessage(reply, new AVIMConversationCallback() {
//            @Override
//            public void done(AVIMException e) {
//                Log.d("@HusterYP",String.valueOf("回复成功啦!"));
//            }
//        });
    }
}
