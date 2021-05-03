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

import java.util.Calendar;

import static com.example.stepcounter.MainActivity.STEP_COUNT_PREFERENCES;
/**
 * Service for counting steps. Sends many broadcasts.
 * Broadcast for saving every 15 minutes
 * Broadcast saveNightlyBroadcast for clearing daily steps and saving them to current day every day at midnight
 * Broadcast for sending steps to MainActivity so steps can be seen increasing in real-time.
 * It runs itself in the background as foreground service.
 * It can be seen as a notification when it is running.
 * It uses Sensor.TYPE_STEP_COUNTER for counting steps.
 */
public class foregroundStepCount extends Service implements SensorEventListener {

    private float freshSteps;
    private static SensorManager sensorManager;
    private static Sensor stepSensor;
    private float minusCounter;
    Intent dailyStepSaveIntent;
    Intent nightlyStepSaveIntent;
    PendingIntent dailySaveStepsPendingIntent;
    PendingIntent nightlySaveStepsPendingIntent;
    Intent freshStepsIntent;
   /* Intent changeDayStepSaveIntent;
    PendingIntent changeDaySaveStepsPendingIntent;
*/
    AlarmManager stepSaveAlarm;
    AlarmManager nightlySaveAlarm;
    private static SharedPreferences stepCounterPreferences;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        saveNightlyBroadcast();
        countSteps();
        freshSteps = 0;
        minusCounter = 0;

        NotificationChannel foreGroundService = new NotificationChannel("TREAT_01", "Treat foreground service", NotificationManager.IMPORTANCE_HIGH);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(foreGroundService);

        Notification stepCounterNotification =
                new Notification.Builder(this, "TREAT_01")
                        .setContentTitle(getText(R.string.treatStepCounter))
                        .setContentText(getText(R.string.notificationMessage))
                        .setContentIntent(nightlySaveStepsPendingIntent)
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


        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            float stepCount = event.values[0];

            if (minusCounter == 0){
                minusCounter = event.values[0];

            }

            freshSteps = stepCount - minusCounter;
            freshStepsBroadcast();
            saveBroadcast();

        }


        Log.d("STEPCOUNTERDEBUG","Steps go up!");
    }

    private void freshStepsBroadcast(){
        freshStepsIntent = new Intent();
        freshStepsIntent.putExtra("freshSteps", Math.round(freshSteps));
        freshStepsIntent.setAction("stepCounter.freshSteps");
        sendBroadcast(freshStepsIntent);
    }

    private void saveBroadcast(){

            dailyStepSaveIntent = new Intent(this, saveStepsReceiver.class);
            dailyStepSaveIntent.putExtra("StepsToSave", Math.round(freshSteps));
            dailySaveStepsPendingIntent =
                    PendingIntent.getBroadcast(getApplicationContext(), 0, dailyStepSaveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            stepSaveAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            stepSaveAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, dailySaveStepsPendingIntent);


    }
    private void saveNightlyBroadcast(){
        Calendar nightlySaveStepCalendar = Calendar.getInstance();
        nightlySaveStepCalendar.setTimeInMillis(System.currentTimeMillis());
        nightlySaveStepCalendar.set(Calendar.HOUR_OF_DAY, 23);
        nightlySaveStepCalendar.set(Calendar.MINUTE, 55);
        nightlySaveStepCalendar.set(Calendar.SECOND, 0);
        nightlySaveAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //This also creates a new value in singleton list.
        nightlyStepSaveIntent = new Intent(this, saveNightlyStepsReceiver.class);
        nightlySaveStepsPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 1, nightlyStepSaveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        nightlySaveAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, nightlySaveStepCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, nightlySaveStepsPendingIntent);
        //nightlySaveAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, nightlySaveStepsPendingIntent);
    }

    //Necessary evil :)
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
