package com.serviceintentserviceandbrodcastreceiver.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("RestartService","Start Service From RestartService");
        context.startService(new Intent(context,NotificationService.class));
    }
}
