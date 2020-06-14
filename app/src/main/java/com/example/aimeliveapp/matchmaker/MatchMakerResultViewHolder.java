package com.example.aimeliveapp.matchmaker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimeliveapp.R;

class MatchMakerResultViewHolder extends RecyclerView.ViewHolder {
    ImageView profile_image;
    TextView tv_name,tv_age,tv_hearts;
View view;
    public MatchMakerResultViewHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
        profile_image=itemView.findViewById(R.id.profile_image);
        tv_name=itemView.findViewById(R.id.tv_name);
        tv_age=itemView.findViewById(R.id.tv_age);
        tv_hearts=itemView.findViewById(R.id.tv_hearts);
    }
}
