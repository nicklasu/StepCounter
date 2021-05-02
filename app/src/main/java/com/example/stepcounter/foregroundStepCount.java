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

public class foregroundStepCount extends Service implements SensorEventListener {

    private float freshSteps;
    private static SensorManager sensorManager;
    private static Sensor stepSensor;
    private float minusCounter;
    Intent dailyStepSaveIntent;
    Intent nightlyStepSaveIntent;
    PendingIntent dailySaveStepsPendingIntent;
    PendingIntent nightlySaveStepsPendingIntent;
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
        }

        saveBroadcast();
        saveNightlyBroadcast();
        Log.d("STEPCOUNTERDEBUG","Steps go up!");
    }


    private void saveBroadcast(){

        dailyStepSaveIntent = new Intent(this, saveStepsReceiver.class);
        dailyStepSaveIntent.putExtra("StepsToSave", Math.round(freshSteps));
        dailySaveStepsPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 0, dailyStepSaveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        stepSaveAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        stepSaveAlarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, 10000, dailySaveStepsPendingIntent);



    }
    private void saveNightlyBroadcast(){
        Calendar nightlySaveStepCalendar = Calendar.getInstance();
        nightlySaveStepCalendar.setTimeInMillis(System.currentTimeMillis());
        nightlySaveStepCalendar.set(Calendar.HOUR_OF_DAY, 23);
        nightlySaveStepCalendar.set(Calendar.MINUTE, 0);
        nightlySaveStepCalendar.set(Calendar.SECOND, 0);
        nightlySaveAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //This also creates a new value in singleton list.
        nightlyStepSaveIntent = new Intent(this, saveNightlyStepsReceiver.class);
        nightlyStepSaveIntent.putExtra("StepsToSaveNightly", Math.round(freshSteps));
        nightlySaveStepsPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 1, nightlyStepSaveIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        nightlySaveAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, nightlySaveStepCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, nightlySaveStepsPendingIntent);
        //nightlySaveAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, nightlySaveStepsPendingIntent);
    }

    //Necessary evil :)
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
