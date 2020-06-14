package com.example.aimeliveapp;

import android.Manifest;
import android.content.SharedPreferences;
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

import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class OutGoingVideoChatActivity extends AppCompatActivity {
    private SinchClient sinchClient;
    public static final String TAG = "MYTAG";
    private Call call;
    TextView mCallDuration, tv_statusofCall;
    private UpdateCallDurationTask mDurationTask;
    private Timer mTimer;
    private boolean mLocalVideoViewAdded = false;
    private boolean mRemoteVideoViewAdded = false;
    boolean mToggleVideoViewPositions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);
        tv_statusofCall = findViewById(R.id.tv_statusofCalls);
        mCallDuration = findViewById(R.id.mCallDurations);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA},
                    1);
        }
        String position = getIntent().getStringExtra("position");
        int positionInt = Integer.parseInt(position);
        String callerid = MatchMakingActivity.LIST.get(positionInt).getUuid();
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String MyUUID = sharedPreferences.getString(LoginActivity.LOGIN_ID, "");
        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(MyUUID)
                .applicationKey("4d584ef6-4040-4692-90f7-b635dcc809cd")
                .applicationSecret("9FU0UplZCEyZJ9Rlt9ek4A==")
                .environmentHost("clientapi.sinch.com")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.addSinchClientListener(new SinchClientListener() {
            @Override
            public void onClientStarted(SinchClient sinchClient) {
                Log.d(TAG, "onClientStarted: ");
                call = sinchClient.getCallClient().callUserVideo(callerid);
                call.addCallListener(new SinchCallListener());
                setVideoViewsVisibility(true, false);

            }

            @Override
            public void onClientStopped(SinchClient sinchClient) {
                Log.d(TAG, "onClientStopped: ");
            }

            @Override
            public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
                Log.d(TAG, "onClientFailed: ");
            }

            @Override
            public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {
                Log.d(TAG, "onRegistrationCredentialsRequired: ");
            }

            @Override
            public void onLogMessage(int i, String s, String s1) {
                Log.d(TAG, "onLogMessage: ");
            }
        });
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

    public void hangupAudioCall(View view) {
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            Toast.makeText(OutGoingVideoChatActivity.this, "" + cause.toString(), Toast.LENGTH_SHORT).show();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
            call.hangup();
            removeVideoViews();
            onBackPressed();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = sinchClient.getAudioController();
            audioController.enableSpeaker();

            if (call.getDetails().isVideoOffered()) {
                setVideoViewsVisibility(true, true);
            }
            Log.d(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
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

    /*   private class SinchCallClientListener implements CallClientListener {
           @Override
           public void onIncomingCall(CallClient callClient, Call incomingCall) {
   //            call = incomingCall;
   //            call.answer();
   //            call.addCallListener(new SinchCallListener());
               Log.d(TAG, "onIncomingCall: ");
           }
       }*/
    private class UpdateCallDurationTask extends TimerTask {


        @Override
        public void run() {
            OutGoingVideoChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }

    }

    private void setVideoViewsVisibility(final boolean localVideoVisibile, final boolean remoteVideoVisible) {
        if (mRemoteVideoViewAdded == false) {
            addRemoteView();
        }
        if (mLocalVideoViewAdded == false) {
            addLocalView();
        }
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
            });
        }
    }

    private ViewGroup getVideoView(boolean localView) {
        if (mToggleVideoViewPositions) {
            localView = !localView;
        }
        return localView ? findViewById(R.id.localVideo) : findViewById(R.id.remoteVideo);
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
