package com.example.yuanping.feihuaapp.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationsQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseActivity;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.db.PoetryDatabaseHelper;
import com.example.yuanping.feihuaapp.event.AddAgreeEvent;
import com.example.yuanping.feihuaapp.event.AddEvent;
import com.example.yuanping.feihuaapp.event.AddRejectEvent;
import com.example.yuanping.feihuaapp.event.FinishEvent;
import com.example.yuanping.feihuaapp.event.OfflineMsgEvent;
import com.example.yuanping.feihuaapp.fragment.SearchFriendsFragment;
import com.example.yuanping.feihuaapp.friends.NewFriendsActivity;
import com.example.yuanping.feihuaapp.test.Test;
import com.example.yuanping.feihuaapp.utils.AddMessageCache;
import com.example.yuanping.feihuaapp.utils.NotificationUtils;
import com.example.yuanping.feihuaapp.utils.Utils;
import com.example.yuanping.feihuaapp.view.SlidingMenu;
import com.example.yuanping.feihuaapp.view.listener.setMaskListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;

public class MainActivity extends BaseActivity implements setMaskListener {

    @BindView(R.id.sliding_menu)
    SlidingMenu slidingMenu;
    @BindView(R.id.user_mask)
    ImageView userMask;
    @BindView(R.id.main_search)
    ImageView search;
    @BindView(R.id.main_left)
    RelativeLayout leftRoot;
    @BindView(R.id.record_menu)
    RelativeLayout recordMenu;
    @BindView(R.id.poetry_menu)
    RelativeLayout poetryMenu;
    @BindView(R.id.friends_menu)
    RelativeLayout friendsMenu;
    @BindView(R.id.main_setting_image)
    ImageView settingIamge;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_description)
    TextView userDescription;
    @BindView(R.id.red_oval_friends)
    ImageView friendRedImg;
    //标志位
    private boolean hasNewFriends = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().post(new FinishEvent());
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasNewFriends) {
            friendRedImg.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        slidingMenu.setListener(this);
        userMask.setAlpha(0.0f); //设置初始透明
        //导入数据库资源
        PoetryDatabaseHelper.initDatabase(this);
        //打开推送服务,同时设置默认打开Activity
        PushService.setDefaultPushCallback(this, MainActivity.class);
        //订阅
        PushService.subscribe(this, "public", MainActivity.class);
        // 保存 installation 到服务器
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVInstallation.getCurrentInstallation().saveInBackground();
            }
        });
        //设置默认的用户签名
        User user = User.getCurrentUser();
        if (TextUtils.isEmpty((CharSequence) user.get(User.USERE_DESCRIPTION))) {
            user.put(User.USERE_DESCRIPTION, getResources().getString(R.string.user_description));
        }
        initView(user);
    }

    private void initView(User user) {
        userName.setText(user.getUsername());
        userDescription.setText(user.get(User.USERE_DESCRIPTION) + "");
        if (AddMessageCache.getInstance().hasNewFriends()) {
            friendRedImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setMask(float dx) {
        userMask.setAlpha(1.0f - dx);
    }

    @OnClick(R.id.main_search)
    public void onSearch() {
        setClickable(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content,
                        new SearchFriendsFragment(),
                        SearchFriendsFragment.tag)
                .addToBackStack("fragment:search")
                .commit();
    }

    @Override
    public void onBackPressed() {
        setClickable(true);
        SearchFriendsFragment fragment = (SearchFriendsFragment) getSupportFragmentManager().findFragmentByTag(SearchFriendsFragment.tag);
        if (fragment != null) {
            fragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    //为了避免Fragment中点击事件的冲突,这里设置跳转后,不可点击
    private void setClickable(boolean flag) {
        if (!flag) {
            search.setClickable(false);
            recordMenu.setClickable(false);
            poetryMenu.setClickable(false);
            friendsMenu.setClickable(false);
            settingIamge.setClickable(false);
            userImage.setClickable(false);
            userDescription.setClickable(false);
            userName.setClickable(false);
        } else {
            search.setClickable(true);
            recordMenu.setClickable(true);
            poetryMenu.setClickable(true);
            friendsMenu.setClickable(true);
            settingIamge.setClickable(true);
            userImage.setClickable(true);
            userDescription.setClickable(true);
            userName.setClickable(true);
            slidingMenu.scrollTo(slidingMenu.getMenuWidth(), 0);
            setMask(1);
        }
    }

    @OnClick(R.id.record_menu)
    public void onRecordMenu(View view) {
        Utils.toast("Record Menu");
    }

    @OnClick(R.id.poetry_menu)
    public void onPoetryMenu(View view) {
        Utils.toast("Poetry Menu");
    }

    @OnClick(R.id.friends_menu)
    public void onFriendsMenu(View view) {
        friendRedImg.setVisibility(View.INVISIBLE);
        startActivity(NewFriendsActivity.class, null, false);
    }

    //有请求添加好友的事件
    @Subscribe
    public void onEvent(AddEvent messageEvent) {
        if (AddMessageCache.getInstance().hasNewFriends()) {
            hasNewFriends = true;
            friendRedImg.setVisibility(View.VISIBLE);
            String name = (String) ((AVIMTextMessage) messageEvent.msg)
                    .getAttrs()
                    .get(Constant.ATTR_ADD_FROM_USER_NAME);
            NotificationUtils.addRequestNotify(name + getString(R.string.ask_add_friend));
        }
    }

    //离线消息,这里还需要判断消息类型(暂时只处理了添加好友的离线消息)
    @Subscribe
    public void onOffline(final OfflineMsgEvent event) {
        int count = event.conversation.getUnreadMessagesCount();
        if (count > 0) {
            event.conversation.queryMessages(count, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    if (e == null && list != null && list.size() != 0) {
                        AddMessageCache.getInstance().cacheAll(list);
                    }
                }
            });
        }
    }

    //同意添加好友事件
    @Subscribe
    public void onAddAgree(AddAgreeEvent event) {

    }

    //拒绝添加好友事件
    @Subscribe
    public void onAddReject(AddRejectEvent event) {

    }

    //测试
    public void MainTest(View view) {
        startActivity(Test.class, null, false);
//        User user = AVUser.getCurrentUser(User.class);
//        Log.d("@HusterYP", String.valueOf(AVInstallation.getCurrentInstallation().getInstallationId()));
//        Log.d("@HusterYP", String.valueOf(user.toString()));
//        try {
//            Log.d("@HusterYP", String.valueOf(user.getJSONObject("installation").getJSONObject("serverData").getString("installationId")));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    //测试
    public void onAgree(View view) {
//        User.getCurrentUser().followInBackground();
    }

    //未读消息数量方法测试
    public int getAllUnreadCount() {
        int unreadCount = 0;
        final LCIMConversationItemCache conversationItemCache = LCIMConversationItemCache.getInstance();
        List<String> conversationList = conversationItemCache.getSortedConversationList();
        for (String conversationId : conversationList) {
            unreadCount += conversationItemCache.getUnreadCount(conversationId);
        }
        Log.d("@HusterYP", String.valueOf("未读消息数量..." + unreadCount));

        //未读消息查询
        final AVIMConversationsQuery query = LCChatKit.getInstance().getClient().getConversationsQuery();
        query.setLimit(unreadCount);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (e == null && list != null && list.size() != 0) {
                    Log.d("@HusterYP", String.valueOf("未读对话的数量: " + list.size()));
                    AVIMConversation conversation = list.get(0);
                    conversation.queryMessages(new AVIMMessagesQueryCallback() {
                        @Override
                        public void done(List<AVIMMessage> list, AVIMException e) {
                            for (AVIMMessage message : list) {
                                Log.d("@HusterYP", String.valueOf("查询的离线消息: " + message.getContent()));
                            }
                        }
                    });
                }
                /*for (AVIMConversation conversation : list) {
                    Log.d("@HusterYP", String.valueOf("离线消息 : " + conversation));
                    int n = conversationItemCache.getUnreadCount(conversation.getConversationId());
                    Log.d("@HusterYP",String.valueOf("根据ConversationId获取的未读数量: "+n));
                }*/
            }
        });

        return unreadCount;
    }

    //设置左侧菜单栏的点击颜色变化
//    private void setMenuClickColor() {
//        setSingleMenuClickColor(recordMenu);
//        setSingleMenuClickColor(poetryMenu);
//        setSingleMenuClickColor(friendsMenu);
//    }
//
//    private void setSingleMenuClickColor(final View view) {
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        view.setBackgroundColor(getResources().getColor(R.color.menu_gray));
//                        break;
//                    case MotionEvent.ACTION_UP:
//                            view.setBackgroundColor(getResources().getColor(R.color.menu_white));
//                        break;
//                }
//                return false;
//            }
//        });
//    }

}
