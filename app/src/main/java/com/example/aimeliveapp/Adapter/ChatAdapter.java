package com.example.aimeliveapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimeliveapp.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {


    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    private Integer whichItem;
    private OnItemClickListener listener;

    public ChatAdapter(Integer whichItem) {
        this.whichItem = whichItem;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (whichItem == LEFT){
            return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false));
        }

        if (whichItem == RIGHT){
            return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false));
        }

        else {
            ChatViewHolder o = null;
            return o;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return 25;
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {

        private ImageView giftIcon;
        private TextView coins;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        if (whichItem == LEFT){
            return LEFT;
        }

        if (whichItem == RIGHT){
            return RIGHT;
        }
        return super.getItemViewType(position);
    }
}
