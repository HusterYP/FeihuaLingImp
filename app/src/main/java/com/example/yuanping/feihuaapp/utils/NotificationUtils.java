package com.example.yuanping.feihuaapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.yuanping.feihuaapp.App;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.friends.NewFriendsActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by yuanping on 2/7/18.
 * 显示通知栏
 */

public class NotificationUtils {
    //有好友添加信息时的通知显示
    public static void addRequestNotify(String msg) {
        Intent intent = new Intent(App.context, NewFriendsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(App.context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(App.context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(App.context.getResources().getString(R.string.app_name))
                .setContentText(msg)
                .setTicker(msg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)//震动和声音
                .build();
        NotificationManager manager = (NotificationManager) App.context
                .getSystemService(NOTIFICATION_SERVICE);
        manager.notify(Constant.ADD_NOTIFICATIONID, notification);
    }

    public static void cancelAddNotifycation(Context context) {
        //如果还有通知的话,取消该通知
        ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE))
                .cancel(Constant.ADD_NOTIFICATIONID);
    }
}
