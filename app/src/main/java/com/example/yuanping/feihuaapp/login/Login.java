package com.example.yuanping.feihuaapp.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseActivity;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.event.FinishEvent;
import com.example.yuanping.feihuaapp.main.MainActivity;
import com.example.yuanping.feihuaapp.utils.ExceptionUtil;
import com.example.yuanping.feihuaapp.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;

public class Login extends BaseActivity {

    @BindView(R.id.login_name)
    EditText userName;
    @BindView(R.id.login_pwd)
    EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void login(View view) {
        String name = userName.getText().toString();
        String pwdStr = pwd.getText().toString();
        if (name.equals("")) {
            Utils.toast(R.string.input_user_name);
            return;
        }
        if (pwdStr.equals("")) {
            Utils.toast(R.string.input_pwd);
            return;
        }
        User.logInInBackground(name, pwdStr, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (ExceptionUtil.filterException(e)) {
                    mLogin();
                }
            }
        });
    }

    public void mLogin() {
        LCChatKit.getInstance().open(User.getCurrentUserId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (ExceptionUtil.filterException(e)) {
                    startActivity(MainActivity.class, null, true);
                }
            }
        });
    }

    @Subscribe
    public void onFinish(FinishEvent event) {
        finish();
    }
}
