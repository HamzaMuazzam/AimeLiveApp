package com.example.aimeliveapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aimeliveapp.service.AudioPlayer;
import com.example.aimeliveapp.service.ExampleService;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class IncomingAudioCallActivity extends AppCompatActivity {
    AudioPlayer audioPlayer;
    private SinchClient sinchClient;
    public static final String TAG = "HELLO_MYTAG";
    private Call call;
    TextView mCallDuration, tv_statusofCall;
    private UpdateCallDurationTask mDurationTask;
    private Timer mTimer;
    ImageView pickup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_audio_call);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA},
                    1);
        }
        tv_statusofCall = findViewById(R.id.tv_statusofCall);
        mCallDuration = findViewById(R.id.mCallDuration);
        pickup = findViewById(R.id.pickup);
        int extra_id = getIntent().getIntExtra("EXTRA_ID", 0);
        String call_id = getIntent().getStringExtra("CALL_ID");
        audioPlayer = new AudioPlayer(this);
        audioPlayer.playRingtone();
        call = ExampleService.sinchClient.getCallClient().getCall(call_id);
        call.addCallListener(new SinchCallListener());

//        sinchClient = Sinch.getSinchClientBuilder()
//                .context(this)
//                .userId("b")
//                .applicationKey("4d584ef6-4040-4692-90f7-b635dcc809cd")
//                .applicationSecret("9FU0UplZCEyZJ9Rlt9ek4A==")
//                .environmentHost("clientapi.sinch.com")
//                .build();
//        sinchClient.setSupportCalling(true);
//        sinchClient.startListeningOnActiveConnection();
//        sinchClient.start();
//        sinchClient.addSinchClientListener(new SinchClientListener() {
//            @Override
//            public void onClientStarted(SinchClient sinchClient) {
//                Log.d(TAG, "onClientStarted: ");
//                if (call == null) {
//                    Log.d(TAG, "onClientStarted: ");
//                    Toast.makeText(IncomingAudioCallActivity.this, "onClientStarted", Toast.LENGTH_SHORT).show();
//
//                    if (call != null) {
//
//                        IncomingAudioCallActivity.this.call.addCallListener(new SinchCallListener());
//
//                    } else {
//                        Toast.makeText(IncomingAudioCallActivity.this, "Call is null", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Log.d(TAG, "call.hangup(): ");
//                }
//
//            }
//
//            @Override
//            public void onClientStopped(SinchClient sinchClient) {
//                Log.d(TAG, "onClientStopped: ");
//                Toast.makeText(IncomingAudioCallActivity.this, "onClientStopped", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
//                Log.d(TAG, "onClientFailed: ");
//            }
//
//            @Override
//            public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {
//                Log.d(TAG, "onRegistrationCredentialsRequired: ");
//            }
//
//            @Override
//            public void onLogMessage(int i, String s, String s1) {
////                Log.d(TAG, s+"onLogMessage: "+s1);
//            }
//        });

    }

    private class UpdateCallDurationTask extends TimerTask {


        @Override
        public void run() {
            IncomingAudioCallActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }

    }

    public void hangupAudioCall(View view) {
        call.hangup();
        audioPlayer.stopRingtone();
        finish();
    }

    public void attendCall(View view) {
        audioPlayer.stopRingtone();
        pickup.setVisibility(View.GONE);
        call.answer();

    }


    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            if (audioPlayer != null) {
                audioPlayer.stopRingtone();
            }
            call = null;
            Log.d(TAG, "onCallEnded: ");
            CallEndCause endCause = endedCall.getDetails().getEndCause();
            Log.d(TAG, "onCallEnded: " + endCause);
            SinchError a = endedCall.getDetails().getError();
            tv_statusofCall.setText("Call Ended");
            finish();
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            pickup.setVisibility(View.GONE);

            if (audioPlayer != null) {
                updateCallDuration();
                tv_statusofCall.setText("Connected");
            }
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(IncomingAudioCallActivity.this, "onCallEstablished", Toast.LENGTH_SHORT).show();
//            callState.setText("connected");
            Log.d(TAG, "onCallEstablished: ");
//            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mTimer = new Timer();
            mDurationTask = new UpdateCallDurationTask();
            mTimer.schedule(mDurationTask, 0, 500);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
//            callState.setText("ringing");
            Log.d(TAG, "onCallProgressing: ");
            Toast.makeText(IncomingAudioCallActivity.this, "onCallProgressing", Toast.LENGTH_SHORT).show();
            tv_statusofCall.setText("Connecting...");


        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            Log.d(TAG, "onShouldSendPushNotification: ");
        }
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

}