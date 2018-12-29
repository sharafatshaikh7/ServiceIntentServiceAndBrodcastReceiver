package com.serviceintentserviceandbrodcastreceiver.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class BackGroundServices extends Service {

    private static final String TAG = "ForegroundService";
    //creating a mediaplayer object
    private MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"OnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //getting systems default ringtone
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(true);

        //staring the player
        player.start();

        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG,"onStart");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"OnDestroy");
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();
    }
}
