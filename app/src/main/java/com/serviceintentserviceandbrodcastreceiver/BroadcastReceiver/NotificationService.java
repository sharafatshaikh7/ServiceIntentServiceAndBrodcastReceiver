package com.serviceintentserviceandbrodcastreceiver.BroadcastReceiver;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.serviceintentserviceandbrodcastreceiver.Constants;
import com.serviceintentserviceandbrodcastreceiver.MainActivity;
import com.serviceintentserviceandbrodcastreceiver.R;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sunil on 09-06-2018.
 */

public class NotificationService extends Service {

    public static final int notify = 30000;  //interval between two services(Here Service run every 2 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        Log.e("NotificationService","inside NotificationService onCreate Method time set to "+String.valueOf( notify / 60000)+" Minuts");
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Log.e("NotificationService","inside NotificationService onDestroy Method = restartService going to call");
        Intent restartService = new Intent("RestartService");
        sendBroadcast(restartService);
    }

    void MakeNotificationAndFire(){

        Log.e("NotificationService","inside MakeNotificationAndFire");
        Intent intent = new Intent(NotificationService.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fromservice","yes");
        intent.putExtra("data",bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Service With Broadcast Receiver")
                .setContentText("Example Of Service With Broadcast Receiver")
                .setContentIntent(pendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MakeNotificationAndFire();
                }
            });
        }
    }
}
