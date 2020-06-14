package com.example.aimeliveapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.aimeliveapp.Adapter.ChatAdapter;
import com.example.aimeliveapp.Adapter.GiftAdapter;
import com.example.aimeliveapp.matchmaker.GetMatch;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallActivity extends AppCompatActivity {
    String position;
    int positionInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA},
                    1);
        }
        position = getIntent().getStringExtra("position");
        positionInt = Integer.parseInt(position);
        initToolbar();
//        initInformationDialog();
        initChatRecycler();
    }

    List<GetMatch> list;


    public void showGiftBottomSheet(View view) {

//        initGiftBottomSheetDialog();
    }

    private void initChatRecycler() {
        RecyclerView chatRecycler = findViewById(R.id.call_chat_recycler);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        ChatAdapter adapter = new ChatAdapter(1);
        chatRecycler.setAdapter(adapter);
    }

    private void initGiftBottomSheetDialog() {

        final BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.dialog_gift, (LinearLayout) findViewById(R.id.bottomSheetContainer));

        final RecyclerView giftRecycler = bottomSheetView.findViewById(R.id.gift_recycler);
        giftRecycler.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayout.VERTICAL));
        GiftAdapter adapter = new GiftAdapter();
        giftRecycler.setAdapter(adapter);

        dialog.setContentView(bottomSheetView);
        dialog.show();
    }

    public void showWarningDialog(View view) {
        initReportDialog();
    }

    private void initReportDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_report);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        final ImageButton close = dialog.findViewById(R.id.report_close);
        final TextView pornography = dialog.findViewById(R.id.report_pornography);
        final TextView fakeGender = dialog.findViewById(R.id.report_fake_gender);
        final TextView spam = dialog.findViewById(R.id.report_spam);
        final TextView langViolence = dialog.findViewById(R.id.report_lang_violence);
        final TextView other = dialog.findViewById(R.id.report_other);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }

    public void dialVideoCall(View view) {
        startActivity(new Intent(this, OutGoingVideoChatActivity.class)
                .putExtra("position", String.valueOf(positionInt)));
    }

    private void initToolbar() {
        RelativeLayout toolbar = findViewById(R.id.toolbar_mirror);
        CircleImageView profilePic = toolbar.findViewById(R.id.profile_pic);

        TextView likes = toolbar.findViewById(R.id.likes);
        TextView location = toolbar.findViewById(R.id.location);
        TextView username = toolbar.findViewById(R.id.caller_name);
        ImageButton moreOption = toolbar.findViewById(R.id.more_options);
        list = MatchMakingActivity.LIST;
        if (list != null) {
            Glide.with(this).load(list.get(positionInt).getProfileImage()).into(profilePic);
            likes.setText(list.get(positionInt).getHearts());
            location.setText(list.get(positionInt).getLocation());
            username.setText(list.get(positionInt).getName());
        }


        ImageButton conferenceCall = toolbar.findViewById(R.id.conference_call);

        conferenceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CallActivity.this, AudioCallActivity.class)
                        .putExtra("position", String.valueOf(positionInt)));
            }
        });

    }


    private void initInformationDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_information);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        final TextView remove = dialog.findViewById(R.id.remove_info_dialog);
//        final TextView pornography = dialog.findViewById(R.id.report_pornography);
//        final TextView fakeGender = dialog.findViewById(R.id.report_fake_gender);
//        final TextView spam = dialog.findViewById(R.id.report_spam);
//        final TextView langViolence = dialog.findViewById(R.id.report_lang_violence);
//        final TextView other = dialog.findViewById(R.id.report_other);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }
}
