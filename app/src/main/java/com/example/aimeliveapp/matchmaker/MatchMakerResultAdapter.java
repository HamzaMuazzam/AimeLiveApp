package com.example.aimeliveapp.matchmaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aimeliveapp.CallActivity;
import com.example.aimeliveapp.R;

import java.util.List;

class MatchMakerResultAdapter extends RecyclerView.Adapter<MatchMakerResultViewHolder> {
    private Context context;
    private List<GetMatch> list;

    public MatchMakerResultAdapter(Context context, List<GetMatch> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MatchMakerResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatchMakerResultViewHolder(LayoutInflater.from(context).inflate(R.layout.matchmakingresult_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MatchMakerResultViewHolder holder, int position) {
        GetMatch model = list.get(position);
        holder.tv_age.setText("Age-" + model.getAge());
        holder.tv_hearts.setText(model.getHearts());
        holder.tv_name.setText(model.getName());
        Glide.with(context).load(model.getProfileImage()).into(holder.profile_image);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CallActivity.class)
                .putExtra("position",String.valueOf(position)));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
