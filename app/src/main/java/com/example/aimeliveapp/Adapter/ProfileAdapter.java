package com.example.aimeliveapp.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimeliveapp.Model.ProfileModel;
import com.example.aimeliveapp.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private OnItemClickListener listener;
    private Integer editProfile;
    private List<ProfileModel> profileModels;

    public ProfileAdapter(List<ProfileModel> profileModels) {
        this.profileModels = profileModels;
    }

    public ProfileAdapter(Integer editProfile, List<ProfileModel> profileModels) {
        this.editProfile = editProfile;
        this.profileModels = profileModels;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, final int position) {

        ProfileModel profile = profileModels.get(position);
        holder.title.setText(profile.getTitle());
        holder.desc.setText(profile.getDesc());
        holder.title.setCompoundDrawablesWithIntrinsicBounds(profile.getIcon(),0,0,0);
        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }
        });

        if (editProfile != null){
            holder.title.setTextColor(Color.parseColor("#5D5D5D"));
            holder.title.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            if (profile.getTitle().equals("ID")){
                holder.next.setImageResource(R.drawable.copy);
            }
        }
    }

    @Override
    public int getItemCount() {
        return profileModels.size();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {

        private ImageView next;
        private TextView title,desc;

        ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            next = itemView.findViewById(R.id.prof_next);
            title = itemView.findViewById(R.id.prof_title);
            desc = itemView.findViewById(R.id.prof_desc);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
