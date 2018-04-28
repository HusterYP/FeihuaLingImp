package com.example.yuanping.feihuaapp.login;

import android.os.Bundle;
import android.view.View;

import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseActivity;
import com.example.yuanping.feihuaapp.event.FinishEvent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterChose extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_chose);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.registerWithPhone)
    public void registerWithPhone(View view) {
        startActivity(RegisterWithPhone.class, null, false);
    }

    @OnClick(R.id.loginWithID)
    public void loginWithID(View view) {
        startActivity(Login.class, null, false);
    }

    @Subscribe
    public void onFinish(FinishEvent event) {
        finish();
    }
}
