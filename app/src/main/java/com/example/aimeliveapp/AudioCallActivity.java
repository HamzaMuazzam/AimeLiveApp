package com.example.aimeliveapp;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AudioCallActivity extends AppCompatActivity {
    private SinchClient sinchClient;
    public static final String TAG = "MYTAG";
    private Call call;
    TextView mCallDuration, tv_statusofCall;
    private UpdateCallDurationTask mDurationTask;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_call);
        tv_statusofCall = findViewById(R.id.tv_statusofCall);
        mCallDuration = findViewById(R.id.mCallDuration);

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
                call = sinchClient.getCallClient().callUser(callerid);
                call.addCallListener(new SinchCallListener());

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

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call endedCall) {
            Toast.makeText(AudioCallActivity.this, "onCallEnded", Toast.LENGTH_SHORT).show();

            call = null;
            try {
                CallEndCause endCause = endedCall.getDetails().getEndCause();
                Log.d(TAG, "onCallEnded: " + endCause);
                SinchError a = endedCall.getDetails().getError();
            } catch (Exception e) {
                Toast.makeText(AudioCallActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finish();
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            Log.d(TAG, "onCallEstablished: ");
            updateCallDuration();
            tv_statusofCall.setText("Connected");
            mTimer = new Timer();
            mDurationTask = new UpdateCallDurationTask();
            mTimer.schedule(mDurationTask, 0, 500);
            Toast.makeText(AudioCallActivity.this, "onCallEstablished", Toast.LENGTH_SHORT).show();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            Log.d(TAG, "onCallProgressing: ");
            tv_statusofCall.setText("Connecting...");
            Toast.makeText(AudioCallActivity.this, "onCallProgressing", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            Log.d(TAG, "onShouldSendPushNotification: ");
            Toast.makeText(AudioCallActivity.this, "onShouldSendPushNotification", Toast.LENGTH_SHORT).show();
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
            AudioCallActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }

    }
}
