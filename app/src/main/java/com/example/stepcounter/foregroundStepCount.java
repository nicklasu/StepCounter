package com.example.stepcounter;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Locale;

import static com.example.stepcounter.MainActivity.STEP_COUNT_PREFERENCES;

public class foregroundStepCount extends Service implements SensorEventListener {

    private int freshSteps;
    private int dailyTotalSteps;
    private int dailyCheck;
    private static SensorManager sensorManager;
    private static Sensor stepSensor;

    Intent dailyStepSaveIntent;
    PendingIntent dailySaveStepsPendingIntent;

    Intent changeDayStepSaveIntent;
    PendingIntent changeDaySaveStepsPendingIntent;

    AlarmManager stepSaveAlarm;
    private static SharedPreferences stepCounterPreferences;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){


        countSteps();
        dailyTotalSteps = (int) stepCounterPreferences.getFloat("dailyStepsKey", 0);

        NotificationChannel foreGroundService = new NotificationChannel("TREAT_01", "Treat foreground service", NotificationManager.IMPORTANCE_HIGH);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(foreGroundService);

        Notification stepCounterNotification =
                new Notification.Builder(this, "TREAT_01")
                        .setContentTitle(getText(R.string.treatStepCounter))
                        .setContentText(getText(R.string.notificationMessage))
                        .setContentIntent(dailySaveStepsPendingIntent)
                        .build();

        startForeground(1, stepCounterNotification);

        return START_NOT_STICKY;
    }

    public static void givePref(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepCounterPreferences = context.getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);

    }


    public void countSteps() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.d("STEPCOUNTERDEBUG", "Sensor not found!");
        }
    }

    public void onSensorChanged(SensorEvent event) {
        //Take steps from sensors and put them to freshSteps


        freshSteps = (int) event.values[0];


       // if(dailyCheck == 0){
          //  dailyTotalSteps = 0;
        //}
        //Put steps to string


        saveBroadcast();
        //getSteps(freshSteps);

        Log.d("STEPCOUNTERDEBUG","Steps go up!");
    }

    private void saveBroadcast(){
        dailyStepSaveIntent = new Intent(this, saveStepsReceiver.class);
        dailyStepSaveIntent.putExtra("StepsToSave", freshSteps);
        dailySaveStepsPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 0, dailyStepSaveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        stepSaveAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        //stepSaveAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, dailySaveStepsPendingIntent);
        stepSaveAlarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, dailySaveStepsPendingIntent);

/*
        changeDayStepSaveIntent = new Intent(this, saveDailyReceiver.class);
        changeDayStepSaveIntent.putExtra("StepsToSave", dailyTotalSteps);
        changeDaySaveStepsPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, changeDayStepSaveIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //stepSaveAlarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, changeDaySaveStepsPendingIntent);
*/
        //stepSaveAlarm.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, dailySaveStepsPendingIntent);
    }

    //Necessary evil :)
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
