package com.example.dandan.mobileapp;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dandan.mobileapp.MainActivity;
import com.example.dandan.mobileapp.R;

public class RunService extends Service {
    public static final int ONGOING_NOTIFICATION_ID = 5552;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.i("RunService", "Starting RunService");
        startServiceOnForeground();

        return Service.START_NOT_STICKY; //2
    }

    private void startServiceOnForeground() {
        startForeground(ONGOING_NOTIFICATION_ID, new NotificationCompat.Builder(getApplicationContext()).setContentTitle("Title").setContentText("Preventing you from texting while driving ...").setSmallIcon(R.drawable.ic_launcher_background).setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT)).setTicker("Ticker").build());
//        startForeground(ONGOING_NOTIFICATION_ID, new Builder(getApplicationContext()).setContentTitle(getText(C0338R.string.app_name)).setContentText("Preventing you from texting while driving ...").setSmallIcon(C0338R.mipmap.ic_launcher).setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), 134217728)).setTicker(getText(C0338R.string.app_name)).build());

    }
}