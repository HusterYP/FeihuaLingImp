package com.example.yuanping.feihuaapp.fragment;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.base.BaseFragment;
import com.example.yuanping.feihuaapp.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yuanping on 1/1/18.
 */

@SuppressLint("ValidFragment")
public class RegisterFragmentOne extends BaseFragment {

    @BindView(R.id.register_with_phone_number)
    EditText editText;

    public RegisterFragmentOne(int id) {
        super(id);
    }

    //点击返回图标
    @OnClick(R.id.register_with_phone_return_image)
    public void returnBack(View view) {
    }

    @OnClick(R.id.register_with_phone_help_image)
    public void help(View view) {

    }

    @OnClick({R.id.register_with_phone_next_image, R.id.register_with_phone_next})
    public void next(View view) {
        String phone = editText.getText().toString();
        if (!phone.equals("")) {
            if (11 == phone.length()) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                RegisterFragmentTwo fragmentTwo = new RegisterFragmentTwo(R.layout.register_fragment_two);
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                fragmentTwo.setArguments(bundle);
                transaction.replace(R.id.container, fragmentTwo);
                transaction.addToBackStack(null);  //回退栈,不销毁
                transaction.commit();
            } else {
                Utils.toast(R.string.wrong_format_phone);
            }
        } else {
            Utils.toast(R.string.input_phone);
        }
    }
}
