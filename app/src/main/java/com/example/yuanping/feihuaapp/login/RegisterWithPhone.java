package com.example.yuanping.feihuaapp.login;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.fragment.RegisterFragmentOne;

import butterknife.ButterKnife;

public class RegisterWithPhone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_phone);
        ButterKnife.bind(this);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        RegisterFragmentOne fragmentOne = new RegisterFragmentOne(R.layout.register_fragment_one);
        transaction.replace(R.id.container, fragmentOne);
        transaction.commit();
    }
}
