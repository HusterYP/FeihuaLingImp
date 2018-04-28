package com.example.yuanping.feihuaapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.yuanping.feihuaapp.Constant;
import com.example.yuanping.feihuaapp.R;
import com.example.yuanping.feihuaapp.utils.AddMessageCache;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yuanping on 2/13/18.
 * 新朋友添加界面
 */

public class NewFriendsAdapter extends RecyclerView.Adapter<NewFriendsAdapter.NewFriendsHolder> {

    private List<String> data;//objectId
    private Context context;
    private OnItemClickListener onItemClickListener;

    public NewFriendsAdapter(Context context) {
        data = AddMessageCache.getInstance().getAddMsgCache();
        this.context = context;
    }

    //Item的点击事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        //同意添加好友
        void agreeAddFriend(int position);
    }

    @Override
    public NewFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_new_friends, parent, false);
        NewFriendsHolder holder = new NewFriendsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewFriendsHolder holder, final int position) {
        AVIMTextMessage msg = (AVIMTextMessage) data.get(position);
        holder.textView.setText(msg.getAttrs().get(Constant.ATTR_ADD_FROM_USER_NAME) + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        //同意按钮的点击事件
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.agreeAddFriend(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public AVIMMessage getMessage(int position) {
        if (data != null && data.size() > 0) {
            return data.get(position);
        }
        return null;
    }

    class NewFriendsHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        private TextView textView;
        private Button button;

        public NewFriendsHolder(final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.new_friends_item_image);
            textView = itemView.findViewById(R.id.new_friends_item_text);
            button = itemView.findViewById(R.id.new_friends_item_bt);
        }
    }

}
