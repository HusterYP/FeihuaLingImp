package com.example.yuanping.feihuaapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.example.yuanping.feihuaapp.App;
import com.example.yuanping.feihuaapp.R;

/**
 * Created by yuanping on 1/1/18.
 * 与界面展示有关的工具类,包括进度框,Toast等
 */

public class Utils {

    public static void toast(int id) {
        Toast.makeText(App.context, App.context.getString(id), Toast.LENGTH_SHORT).show();
    }

    public static void toast(String msg) {
        Toast.makeText(App.context, String.valueOf(msg), Toast.LENGTH_SHORT).show();
    }

    //加载进度对话框
    public static ProgressDialog showDialog(Activity activity, String msg) {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setMessage(msg);
        if (!activity.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }
}
