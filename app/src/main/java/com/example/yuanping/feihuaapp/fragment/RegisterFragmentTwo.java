package com.example.yuanping.feihuaapp.fragment;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseFragment;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.main.MainActivity;
import com.example.yuanping.feihuaapp.utils.ExceptionUtil;
import com.example.yuanping.feihuaapp.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by yuanping on 1/1/18.
 */

@SuppressLint("ValidFragment")
public class RegisterFragmentTwo extends BaseFragment {

    @BindView(R.id.set_user_info_phone)
    TextView phone;
    @BindView(R.id.set_user_info_name)
    EditText name;
    @BindView(R.id.set_user_info_pwd)
    EditText pwd;
    @BindView(R.id.set_user_info_conf_pwd)
    EditText confPwd;
    @BindView(R.id.set_user_info_radio)
    CheckBox checkBox;
    @BindView(R.id.set_user_info_protocol)
    TextView userProtocol;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        String phone_number = bundle.getString("phone");
        phone.setText(phone_number);
        return view;
    }

    public RegisterFragmentTwo(int id) {
        super(id);
    }

    @OnClick(R.id.set_user_info_join)
    public void join(View view) {
        String userName = name.getText().toString();
        String pwdStr = pwd.getText().toString();
        String confPwdStr = confPwd.getText().toString();
        boolean checked = checkBox.isChecked();
        if (userName.equals("")) {
            Utils.toast(R.string.input_user_name);
            return;
        }
        if (pwdStr.equals("")) {
            Utils.toast(R.string.input_pwd);
            return;
        }
        if (confPwdStr.equals("")) {
            Utils.toast(R.string.input_conf_pwd);
            return;
        }
        if (!confPwdStr.equals(pwdStr)) {
            Utils.toast(R.string.pwd_not_equal);
            return;
        }
        if (!checked) {
            Utils.toast(R.string.agree_user_protocol);
            return;
        }

        User.registerWithPhone(userName, pwdStr, phone.getText().toString(), new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (ExceptionUtil.filterException(e)) {
                    Utils.toast(R.string.register_success);
                    mLogin();
                }
            }
        });
    }

    //点击返回图标
    @OnClick(R.id.register_with_phone_return_image)
    public void returnBack(View view) {
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
    }

    @OnClick(R.id.register_with_phone_help_image)
    public void help(View view) {

    }

    private void mLogin() {
        Log.d("@HusterYP", String.valueOf("开始"));

        LCChatKit.getInstance().open(User.getCurrentUserId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (ExceptionUtil.filterException(e)) {
                    Log.d("@HusterYP", String.valueOf("跳转"));
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }
}
