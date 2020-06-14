package com.example.aimeliveapp.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.aimeliveapp.IncomingAudioCallActivity;
import com.example.aimeliveapp.IncomingVideoCallActivity;
import com.example.aimeliveapp.OutGoingVideoChatActivity;
import com.example.aimeliveapp.MainActivity;
import com.example.aimeliveapp.R;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallDetails;

import java.util.List;

import static com.example.aimeliveapp.service.App.CHANNEL_ID;

public class ExampleService extends Service {
    public static SinchClient sinchClient;
    public static Call call;
    AudioPlayer audioPlayer;
    public static int MESSAGE_ID = 14;
    public static final String ACTION_ANSWER = "answer";
    public static final String ACTION_IGNORE = "ignore";
    public static final String TAG = "MYTAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        String uuid = intent.getStringExtra("uuid");

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(uuid)
                .applicationKey("4d584ef6-4040-4692-90f7-b635dcc809cd")
                .applicationSecret("9FU0UplZCEyZJ9Rlt9ek4A==")
                .environmentHost("clientapi.sinch.com")
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Example Service")
                .setContentText("Online")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        sinchClient.addSinchClientListener(new SinchClientListener() {
            @Override
            public void onClientStarted(SinchClient sinchClient) {
                Log.d(TAG, "onClientStarted: ");
                if (call == null) {
                    ExampleService.this.sinchClient = sinchClient;
                    sinchClient.getCallClient().addCallClientListener(new IncomingCallSinchCallClientListener());
                    //                    call = sinchClient.getCallClient().callUser("b");
                    //                    call.addCallListener(new SinchCallListener());
                    Log.d(TAG, "onClientStarted: ");
                } else {
                    Log.d(TAG, "call.hangup(): ");
                    call.hangup();
                }

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
//                Log.d(TAG, s+"onLogMessage: "+s1);
            }
        });


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

/*    private class SinchCallListener implements CallListener {
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
//            button.setText("Call");
//            callState.setText("");
//            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            if (audioPlayer != null) {
                audioPlayer.stopRingtone();
            }
//            callState.setText("connected");
            Log.d(TAG, "onCallEstablished: ");
//            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
//            callState.setText("ringing");
            Log.d(TAG, "onCallProgressing: ");
//            Toast.makeText(CallActivity.this, "onCallProgressing", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            Log.d(TAG, "onShouldSendPushNotification: ");
        }
    }*/

    private class IncomingCallSinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            CallDetails details = call.getDetails();
            boolean videoOffered = details.isVideoOffered();
            if (!videoOffered) {
//                call.addCallListener(new SinchCallListener());
//                Log.d(TAG, "onIncomingCall: ");
                Intent intent = new Intent(ExampleService.this, IncomingAudioCallActivity.class);
                intent.putExtra("EXTRA_ID", MESSAGE_ID);
                intent.putExtra("CALL_ID", call.getCallId());
                boolean inForeground = isAppOnForeground(getApplicationContext());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                if (!inForeground) {
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !inForeground) {
//                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(MESSAGE_ID, createIncomingCallNotification(call.getRemoteUserId(), intent));
                } else {
                    startActivity(intent);
                }

            }
            else {
                Intent intent = new Intent(ExampleService.this, IncomingVideoCallActivity.class);
                intent.putExtra("EXTRA_ID", MESSAGE_ID);
                intent.putExtra("CALL_ID", call.getCallId());
                boolean inForeground = isAppOnForeground(getApplicationContext());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                if (!inForeground) {
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !inForeground) {
//                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(MESSAGE_ID, createIncomingCallNotification(call.getRemoteUserId(), intent));
                } else {
                    startActivity(intent);
                }


            }


        }
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}