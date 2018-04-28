package com.example.yuanping.feihuaapp.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseActivity;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.event.EmptyEvent;
import com.example.yuanping.feihuaapp.utils.UserCacheUtils;
import com.example.yuanping.feihuaapp.utils.UserUtils;
import com.example.yuanping.feihuaapp.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriends extends BaseActivity {

    private User user;
    @BindView(R.id.add_user_image)
    CircleImageView userImage;
    @BindView(R.id.add_user_name)
    TextView userName;
    @BindView(R.id.add_user_id)
    TextView userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String id = intent.getStringExtra(User.FINDUSER);
        user = UserCacheUtils.getCachedUser(id);
        if (user != null) {
            setUserInfo(user);
        }
    }

    private void setUserInfo(User user) {
        userID.setText(UserUtils.splitUserID(user));
        userName.setText(user.getUsername());
    }

    //点击添加好友事件
    @OnClick(R.id.add_friend)
    public void onAddFriends(View view) {
        if (user != null) {
            FriendsManager.getInstance().createAddRequestInBackgroud(user);
        } else {
            Utils.toast(R.string.somthing_error);
        }
    }

    @OnClick(R.id.add_return_back)
    public void onReturnBack(View view) {
        finish();
    }

    //测试
    @Subscribe
    public void test(EmptyEvent test) {
    }
}
