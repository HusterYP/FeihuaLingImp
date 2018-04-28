package com.example.yuanping.feihuaapp.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.bean.User;
import com.example.yuanping.feihuaapp.friends.AddFriends;
import com.example.yuanping.feihuaapp.utils.ExceptionUtil;
import com.example.yuanping.feihuaapp.utils.UserCacheUtils;
import com.example.yuanping.feihuaapp.utils.Utils;

import java.io.Serializable;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by yuanping on 2/4/18.
 * 搜索并添加好友界面
 */
public class SearchFriendsFragment extends Fragment {
    public static final String tag = "search_friends";
    LinearLayout searchRoot;
    LinearLayout searchMain;
    EditText searchEdit;
    ImageView searchImage;
    boolean flag = true;

    int centerX;
    int centerY;
    float radius;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_friend_fragment, container, false);
        init(view);
        befAnimation();
        return view;
    }

    public void onBackPressed() {
        flag = true;

        Animator animator = ViewAnimationUtils.createCircularReveal(searchMain,
                centerX, centerY, radius, 0);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                searchRoot.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                searchRoot.setVisibility(View.INVISIBLE);
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.start();
    }

    private void befAnimation() {
        searchImage.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        centerX = searchImage.getLeft() + searchImage.getWidth() / 2;
                        centerY = searchImage.getTop() + searchImage.getHeight() / 2;
                        radius = (float) Math.sqrt(Math.pow(searchMain.getHeight(), 2) +
                                Math.pow(searchMain.getWidth(), 2));
                        if (flag) {
                            onAnimation();
                        }
                    }
                });
    }

    private void onAnimation() {
        flag = false;
        searchRoot.setVisibility(View.VISIBLE);
        Animator animator = ViewAnimationUtils.createCircularReveal(searchMain,
                centerX, centerY, 120, radius);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.start();
    }

    private void init(View view) {
        searchRoot = view.findViewById(R.id.search_root);
        searchMain = view.findViewById(R.id.edit_lay);
        searchEdit = view.findViewById(R.id.edit_search);
        searchImage = view.findViewById(R.id.search_image);

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch(v);
            }
        });
    }

    public void onSearch(View view) {
        String userName = searchEdit.getText().toString();
        if (userName.equals("")) {
            Utils.toast(R.string.input_friends_name);
        } else {
            search(userName);
        }

    }

    public void search(String name) {
        UserCacheUtils.findUserInBackgroud(name, new UserCacheUtils.FindUser() {
            @Override
            public void findUserInBackgroud(List<User> list) {
                if (list.size() != 0) {
                    User user = list.get(0);
                    UserCacheUtils.cacheUser(user);
                    Intent intent = new Intent(getActivity(), AddFriends.class);
                    intent.putExtra(User.FINDUSER, user.getObjectId());
                    startActivity(intent);
                } else {
                    Utils.toast(R.string.no_such_user);
                }
            }
        });
    }
}

