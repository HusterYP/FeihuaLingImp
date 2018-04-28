package com.example.yuanping.feihuaapp.utils;

import android.util.Log;

/**
 * Created by yuanping on 1/1/18.
 */

public class ExceptionUtil {
    public static boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            remind(e);
            return false;
        } else {
            return true;
        }
    }

    public static Integer getErrorCode(Exception e) {
        if (null != e) {
            return Integer.parseInt(e.getMessage().split(",")[0].split(":")[1]);
        }
        return null;
    }

    /*
    * 这里为了防止还有其他异常发生导致程序奔溃,所以直接使
    * 用了一旦发生异常,就直接回报原文给用户,而不是将错误
    * 码解析,这样不可避免会遗漏异常,我认为这样的用户体验
    * 牺牲是值得的
    * */
    public static void remind(Exception e) {
        Utils.toast(e.getMessage());
//        if (211 == getErrorCode(e)) {
//            Utils.toast(R.string.no_such_user);
//        }
//        if (127 == getErrorCode(e)) {
//            Utils.toast(R.string.no_use_phone);
//        }
//        if (214 == getErrorCode(e)) {
//            Utils.toast(R.string.phone_has_exist);
//        }
    }
}
