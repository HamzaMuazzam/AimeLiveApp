package com.example.aimeliveapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimeliveapp.R;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    private OnItemClickListener listener;


    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GiftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return 25;
    }

    static class GiftViewHolder extends RecyclerView.ViewHolder {

        private ImageView giftIcon;
        private TextView coins;

        GiftViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
