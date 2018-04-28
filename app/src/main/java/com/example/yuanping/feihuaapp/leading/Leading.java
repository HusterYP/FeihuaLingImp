package com.example.yuanping.feihuaapp.leading;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseActivity;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.event.FinishEvent;
import com.example.yuanping.feihuaapp.login.RegisterChose;
import com.example.yuanping.feihuaapp.main.MainActivity;
import com.example.yuanping.feihuaapp.utils.AddMessageCache;
import com.example.yuanping.feihuaapp.utils.ExceptionUtil;

import org.greenrobot.eventbus.Subscribe;

import cn.leancloud.chatkit.LCChatKit;

public class Leading extends BaseActivity {

    private final int TO_MAIN = 0;
    private final int TO_LOGIN = 1;
    //    private final int DURATION = 2000;
    private final int DURATION = 0; //测试阶段改为0s

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TO_LOGIN:
                    startActivity(RegisterChose.class, null, true);
                    break;
                case TO_MAIN:
                    mLogIn();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leading);
        AddMessageCache.getInstance().init();
        if (null == User.getCurrentUser()) {
            handler.sendEmptyMessageDelayed(TO_LOGIN, DURATION);
        } else {
            User.getCurrentUser().updateUserInfo();
            handler.sendEmptyMessageDelayed(TO_MAIN, DURATION);
        }
    }

    private void mLogIn() {
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
