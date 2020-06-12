package com.example.aimeliveapp.addfragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.aimeliveapp.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChildFragment extends Fragment {


    public ChildFragment() {
        // Required empty public constructor
    }

    View view;
    CircleImageView iv_profile;
    TextView tv_hearts, tv_comments, tv_shares, tv_title, tv_description, tv_mucisName;

    String comments;
    String hearts;
    String music_name;
    String profile_image;
    String shares;
    String title;
    String video_link;
    String user_id;
    String post_id;
    String description;
    //    PlayerView playerView;
//    SimpleExoPlayer exoPlayerInstance = null;
//    DataSource.Factory dataSourceFactory;
    Uri uri;
    VideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_child, container, false);
        initviews();

        return view;
    }

    private void initviews() {
//        Binding xml view
//        playerView = view.findViewById(R.id.player_view);
//        Setup Exoplayer instance
//        exoPlayerInstance = ExoPlayerFactory.newSimpleInstance(getContext());
//        dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getContext().getPackageName()));
        Bundle bundle;
        bundle = getArguments();
        comments = bundle.getString("comments");
        hearts = bundle.getString("hearts");
        music_name = bundle.getString("music_name");
        profile_image = bundle.getString("profile_image");
        shares = bundle.getString("shares");
        title = bundle.getString("title");
        video_link = bundle.getString("video_link");
        user_id = bundle.getString("user_id");
        post_id = bundle.getString("post_id");
        description = bundle.getString("description");
        videoView = view.findViewById(R.id.videoView);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_hearts = view.findViewById(R.id.tv_hearts);
        tv_hearts.setText(hearts);
        tv_comments = view.findViewById(R.id.tv_comments);
        tv_comments.setText(comments);
        tv_shares = view.findViewById(R.id.tv_shares);
        tv_shares.setText(shares);
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_description = view.findViewById(R.id.tv_description);
        tv_description.setText(description);
        tv_mucisName = view.findViewById(R.id.tv_mucisName);
        tv_mucisName.setText(music_name);
        uri = Uri.parse(video_link);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
//        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        mediaController.hide();
//        videoView.requestFocus();
//        MediaSource firstSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
//        Prepare the exoPlayerInstance with the source
//        exoPlayerInstance.prepare(firstSource);
//        playerView.setPlayer(exoPlayerInstance);
//        playerView.setControllerAutoShow(false);
//        playerView.hideController();
//        playerView.setUseController(false);
//        exoPlayerInstance.setPlayWhenReady(true);
//        playerView.onResume();
        Glide.with(Objects.requireNonNull(getContext())).load(profile_image).into(iv_profile);
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoView.pause();
    }
}