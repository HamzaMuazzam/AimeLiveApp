package com.example.aimeliveapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aimeliveapp.service.AudioPlayer;
import com.example.aimeliveapp.service.ExampleService;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

public class IncomingVideoCallActivity extends AppCompatActivity {
    private Call call;
    AudioPlayer audioPlayer;
    SinchClient sinchClient;
    TextView mCallDuration, tv_statusofCall;

    private boolean mLocalVideoViewAdded = false;
    private boolean mRemoteVideoViewAdded = false;
    boolean mToggleVideoViewPositions = false;
    String call_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_video_call);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA},
                    1);
        }
        audioPlayer = new AudioPlayer(this);
        audioPlayer.playRingtone();
        int extra_id = getIntent().getIntExtra("EXTRA_ID", 0);
        call_id = getIntent().getStringExtra("CALL_ID");
        sinchClient = ExampleService.sinchClient;
        try {

            call = sinchClient.getCallClient().getCall(call_id);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (call != null) {
            call.addCallListener(new SinchCallListener());
        }

    }

    public void hangupAudioCall(View view) {
        if (call != null) {
            call.hangup();
            if (audioPlayer!=null){
                audioPlayer.stopRingtone();
            }
        }

    }

    public void callAtnd(View view) {
        if (call != null) {
            call.answer();
            findViewById(R.id.pickup).setVisibility(View.GONE);
//            findViewById(R.id.cameracll).setVisibility(View.GONE);
        }
    }

    private class SinchCallListener implements VideoCallListener {

        public static final String TAG = "MYTAG";

        @Override
        public void onCallEnded(Call call) {
            if (audioPlayer!=null){
                audioPlayer.stopRingtone();
            }    Toast.makeText(IncomingVideoCallActivity.this, "onCallEnded", Toast.LENGTH_SHORT).show();
            CallEndCause cause = call.getDetails().getEndCause();
            Toast.makeText(IncomingVideoCallActivity.this, "" + cause.toString(), Toast.LENGTH_SHORT).show();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
            call.hangup();
            removeVideoViews();
            onBackPressed();
        }

        @Override
        public void onCallEstablished(Call call) {
        if (audioPlayer!=null){
            audioPlayer.stopRingtone();
        }
            Log.d(TAG, "Call established");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = sinchClient.getAudioController();
            audioController.enableSpeaker();

            if (call.getDetails().isVideoOffered()) {
                setVideoViewsVisibility(true, true);
            }
            Log.d(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
            Toast.makeText(IncomingVideoCallActivity.this, "onCallEstablished", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            Toast.makeText(IncomingVideoCallActivity.this, "onCallProgressing", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {

        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }

    private class UpdateCallDurationTask extends TimerTask {


        @Override
        public void run() {
            IncomingVideoCallActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }

    }

    private void setVideoViewsVisibility(final boolean localVideoVisibile, final boolean remoteVideoVisible) {

        addLocalView();
        addRemoteView();

        final VideoController vc = sinchClient.getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                vc.getLocalView().setVisibility(localVideoVisibile ? View.VISIBLE : View.GONE);
                vc.getRemoteView().setVisibility(remoteVideoVisible ? View.VISIBLE : View.GONE);
            });
        }
    }

    private void addRemoteView() {

        final VideoController vc = sinchClient.getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                try {
                    ViewGroup remoteView = getVideoView(false);
                    remoteView.addView(vc.getRemoteView());
                    remoteView.setOnClickListener((View v) -> {
                        removeVideoViews();
                        mToggleVideoViewPositions = !mToggleVideoViewPositions;
                        addRemoteView();
                        addLocalView();
                    });
                    mRemoteVideoViewAdded = true;
                    vc.setLocalVideoZOrder(!mToggleVideoViewPositions);

                } catch (Exception e) {
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private ViewGroup getVideoView(boolean localView) {
        if (mToggleVideoViewPositions) {
            localView = !localView;
        }
        return localView ? findViewById(R.id.localVideo) : findViewById(R.id.remoteVideo);
    }

    private void updateCallDuration() {
        if (call != null) {
            mCallDuration.setText(formatTimespan(call.getDetails().getDuration()));
        }

    }

    private String formatTimespan(int totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void removeVideoViews() {

        VideoController vc = sinchClient.getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                ((ViewGroup) (vc.getRemoteView().getParent())).removeView(vc.getRemoteView());
                ((ViewGroup) (vc.getLocalView().getParent())).removeView(vc.getLocalView());
                mLocalVideoViewAdded = false;
                mRemoteVideoViewAdded = false;
            });
        }
    }

    private void addLocalView() {

        final VideoController vc = sinchClient.getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                ViewGroup localView = getVideoView(true);

                localView.addView(vc.getLocalView());
                localView.setOnClickListener(v -> vc.toggleCaptureDevicePosition());
                mLocalVideoViewAdded = true;
                vc.setLocalVideoZOrder(!mToggleVideoViewPositions);
            });
        }
    }


}