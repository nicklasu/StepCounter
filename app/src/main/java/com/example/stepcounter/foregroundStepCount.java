package com.example.stepcounter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class foregroundStepCount extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        NotificationChannel foreGroundService = new NotificationChannel("TREAT_01", "Treat foreground service", NotificationManager.IMPORTANCE_HIGH);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(foreGroundService);

        Intent stepCountIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, stepCountIntent, 0);


        Notification notification =
                new Notification.Builder(this, "TREAT_01")
                        .setContentTitle(getText(R.string.treatStepCounter))
                        .setContentText(getText(R.string.notificationMessage))
                        .setContentIntent(pendingIntent)
                        .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }
}
