package com.example.yuanping.feihuaapp.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.utils.ExceptionUtil;

import org.json.JSONObject;

/**
 * Created by yuanping on 2/6/18.
 * 处理推送事件,但是好像不行,启弃用
 */

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(Constant.RECEIVER_FILTER)) {
                Log.d("@HusterYP", String.valueOf("推送接收到了 !"));
                Log.d("@HusterYP", String.valueOf("intent toString.." + intent.toString()));

                JSONObject json = new JSONObject(intent.getExtras().getString(Constant.JSON_DATA));
                Log.d("@HusterYP", String.valueOf("Json 数据..." + intent.getExtras().getString(Constant.JSON_DATA)));

                if (json != null) {
                    Log.d("@HusterYP", String.valueOf("json 解析中"));
                    Log.d("@HusterYP", String.valueOf(json.toString()));
                }
            }
        } catch (Exception e) {
            ExceptionUtil.filterException(e);
            Log.d("@HusterYP", String.valueOf("Receiver Exception .." + e));
        }
    }
}
