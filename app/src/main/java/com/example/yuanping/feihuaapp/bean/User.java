package com.example.yuanping.feihuaapp.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import org.json.JSONException;

import java.io.Serializable;

/**
 * Created by yuanping on 1/1/18.
 */

@AVClassName("User")
public class User extends AVUser {
    public static final String INSTALLATION = "installation";
    public static final String USERNAME = "username";
    public static final String FINDUSER = "finduser";
    public static final String AVATAR = "avatar";
    public static final String USERE_DESCRIPTION = "description";

    public static User getCurrentUser() {
        return getCurrentUser(User.class);
    }

    public void updateUserInfo() {
        AVInstallation installation = AVInstallation.getCurrentInstallation();
        if (installation != null) {
            put(INSTALLATION, installation);
            saveInBackground();
        }
    }

    public static String getCurrentUserId() {
        User currentUser = getCurrentUser(User.class);
        return (null != currentUser ? currentUser.getObjectId() : null);
    }

    public static void registerWithPhone(String name, String password, String phone,
                                         SignUpCallback callback) {
        AVUser user = new AVUser();
        user.setUsername(name);
        user.setPassword(password);
        user.setMobilePhoneNumber(phone);
        user.signUpInBackground(callback);
    }

    //头像
    public String getAvatarUrl() {
        AVFile avatar = getAVFile(AVATAR);
        if (avatar != null) {
            return avatar.getUrl();
        } else {
            return null;
        }
    }

    //获取InstallationId
    public String getInstallationId() {
        String id = "";
        try {
            id = this.getJSONObject("installation")
                    .getJSONObject("serverData")
                    .getString("installationId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }
}
