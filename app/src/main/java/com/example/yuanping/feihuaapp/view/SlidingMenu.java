package com.example.yuanping.feihuaapp.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.yuanping.feihuaapp.utils.ScreenUtils;
import com.example.yuanping.feihuaapp.view.listener.setMaskListener;


/**
 * Created by yuanping on 11/20/17.
 * 自定义侧滑菜单栏
 */

public class SlidingMenu extends HorizontalScrollView {
    //屏幕宽度
    private int mScreenWidth;
    //dp
    private int mMenuRightPadding;
    //菜单宽度,dp
    private int mMenuWidth;
    private int mMenuHalfWidth;
    private boolean once;
    private boolean isOpen;
    private float dx = 0;
    private setMaskListener listener;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
    }

    public SlidingMenu(Context context) {
        super(context);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = ScreenUtils.getScreenWidth(context);
    }

    //不知为何,从Login界面跳转过来时,会出现onMeasure方法调用两次的情况,使得mMenuRightPadding的值被两次改变
    //(因为当初mMenuRightPadding的初始化值是直接写在private定义变量的时候的)
    //这里将142写死在函数中
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);
            ViewGroup content = (ViewGroup) wrapper.getChildAt(1);
            // dp to px
            mMenuRightPadding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 142, content
                            .getResources().getDisplayMetrics());
            mMenuWidth = mScreenWidth - mMenuRightPadding;
            /*mMenuWidth = (int)TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, mMenuWidth, content
                            .getResources().getDisplayMetrics());*/
            mMenuHalfWidth = mMenuWidth / 2;
            menu.getLayoutParams().width = mMenuWidth;
            content.getLayoutParams().width = mScreenWidth;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 将菜单隐藏
            this.scrollTo(mMenuWidth, 0);
            once = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                dx = ev.getRawX(); //记录下按下的坐标
                break;
            case MotionEvent.ACTION_MOVE:
                //这里实际上mMenuRightPadding已经改变了，所以再减142就对不上了,上面进行了dp与px的转换
//                Log.d("@HusterYP",String.valueOf(getScrollX()+"...."+(mScreenWidth-142)+"..."+mMenuWidth));
                listener.setMask(getScrollX() * 1.0f / mMenuWidth);
                break;
            // Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
            case MotionEvent.ACTION_UP:
                float x1 = ev.getRawX();
                int scrollX = getScrollX();
                //点击空白事件，关闭菜单
                if ((x1 - dx < 5 && x1 - dx >= 0) || (x1 - dx <= 0 && x1 - dx > -5)) {
                    if (isOpen && x1 > mMenuWidth) {
                        closeMenu();
                    }
                } else {
                    if (scrollX > mMenuHalfWidth) {
                        this.smoothScrollTo(mMenuWidth, 0);
                        isOpen = false;
                        listener.setMask(1.0f);
                    } else {
                        this.smoothScrollTo(0, 0);
                        isOpen = true;
                        listener.setMask(0.0f);
                    }
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    private void openMenu() {
        if (isOpen) {
            return;
        }
        this.smoothScrollTo(0, 0);
        listener.setMask(0.0f);

        isOpen = true;
    }

    private void closeMenu() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            listener.setMask(1.0f);
            isOpen = false;
        }
    }

    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    public void setListener(setMaskListener listener) {
        this.listener = listener;
    }

    public int getMenuWidth() {
        return mMenuWidth;
    }
}
