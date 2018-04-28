package com.example.yuanping.feihuaapp.friends;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.adapter.NewFriendsAdapter;
import com.example.yuanping.feihuaapp.base.BaseActivity;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.event.AddEvent;
import com.example.yuanping.feihuaapp.utils.MessageUtils;
import com.example.yuanping.feihuaapp.utils.NotificationUtils;
import com.example.yuanping.feihuaapp.utils.UserCacheUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* 处理添加好友的请求
* Todo: 改进这个界面,同时还需要添加滑动删除,添加下拉加载更多,添加清除所有的功能
* */

public class NewFriendsActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, NewFriendsAdapter.OnItemClickListener {
    @BindView(R.id.new_friends_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.new_friends_recycler)
    RecyclerView recyclerView;
    NewFriendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);
        ButterKnife.bind(this);
        setTitle(R.string.new_add_friend);
        NotificationUtils.cancelAddNotifycation(this);
        init();
    }

    private void init() {
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewFriendsAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    //Todo: 每一个条目的点击事件,应该跳转到用户的详情界面
    @Override
    public void onItemClick(int position) {
        AVIMMessage message = adapter.getMessage(position);
        if (message != null) {
            Toast.makeText(getApplication(), String.valueOf(message.getFrom() + "用户详情!"), Toast.LENGTH_SHORT).show();
        }
    }

    //发送一个同意添加回执
    @Override
    public void agreeAddFriend(int position) {
        AVIMMessage msg = adapter.getMessage(position);
        String name = (String) ((AVIMTextMessage) msg)
                .getAttrs()
                .get(Constant.ATTR_ADD_FROM_USER_NAME);
        if (!TextUtils.isEmpty(name)) {
            UserCacheUtils.findUserInBackgroud(name, new UserCacheUtils.FindUser() {
                @Override
                public void findUserInBackgroud(List<User> list) {
                    if (list != null && list.size() > 0) {
                        MessageUtils.sendAddReply(list.get(0),true);
                    }
                }
            });
        } else {
            Log.d("@HusterYP", String.valueOf("添加好友回执中,name为null"));
        }
    }

    //刷新事件
    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                Toast.makeText(getApplication(), String.valueOf("刷新成功!"), Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }

    @Subscribe
    public void onEvent(AddEvent event) {
        NotificationUtils.cancelAddNotifycation(this);
        adapter.notifyDataSetChanged();
    }
}
