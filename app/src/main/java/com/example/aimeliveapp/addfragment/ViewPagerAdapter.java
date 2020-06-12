package com.example.aimeliveapp.addfragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Objects;

class ViewPagerAdapter extends FragmentStatePagerAdapter {
    List<DocumentSnapshot> documentsList;
    Context context;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<DocumentSnapshot> documentsList, Context context) {
        super(fm, behavior);
        this.documentsList = documentsList;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ChildFragment child = new ChildFragment();
        Bundle bundle = new Bundle();

        try {
            String comments = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("comments")).toString();
            String hearts = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("hearts")).toString();
            String music_name = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("music_name")).toString();
            String profile_image = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("profile_image")).toString();
            String shares = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("shares")).toString();
            String title = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("title")).toString();
            String video_link = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("video_link")).toString();
            String user_id = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("user_id")).toString();
            String post_id = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("post_id")).toString();
            String description = Objects.requireNonNull(Objects.requireNonNull(documentsList.get(position).getData()).get("description")).toString();

            bundle.putString("comments",comments );
            bundle.putString("hearts",hearts );
            bundle.putString("music_name", music_name);
            bundle.putString("profile_image", profile_image);
            bundle.putString("shares",shares );
            bundle.putString("title", title);
            bundle.putString("video_link", video_link);
            bundle.putString("user_id", user_id);
            bundle.putString("post_id", post_id);
            bundle.putString("description", description);

            child.setArguments(bundle);
//            Log.d("MYTAGS", comments + "\n" + hearts + music_name + "\n" + profile_image + shares + "\n" + title + video_link);


        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        assert child != null;
        return child;

    }

    @Override
    public int getCount() {
        return documentsList.size();
    }
}
