package com.example.yuanping.feihuaapp.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yuanping on 1/1/18.
 * 为了在Fragment中使用Butter Knife,同时为了避免重复代码,封装一个基类
 * 但是使用时需要传入对应的layout id
 */

@SuppressLint("ValidFragment")
public class BaseFragment extends Fragment {

    private Unbinder unbinder;
    private int id;

    public BaseFragment(int id) {
        super(); //Fragment源码中用到反射构造了对象，是无参数的构造函数
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(id, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
