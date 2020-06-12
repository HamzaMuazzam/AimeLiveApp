package com.example.aimeliveapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimeliveapp.R;

public class VideoBroadcastAdapter extends RecyclerView.Adapter<VideoBroadcastAdapter.GiftViewHolder> {
private Context context;

    private OnItemClickListener listener;

    public VideoBroadcastAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GiftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_broadcast_video,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, final int position) {
        Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return 5;
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
