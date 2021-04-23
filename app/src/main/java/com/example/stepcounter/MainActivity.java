package com.example.stepcounter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final String STEP_COUNT_PREFERENCES = "StepCountPreferences";
    public static final String dailyStepsPref = "dailyStepsKey";
    SharedPreferences stepCountPreferences;

    //Get the SensorManager and attach it a name "sensorManager"
    private SensorManager sensorManager;
    private Sensor stepSensor;

    private float totalSteps;
    private float savedTotalSteps;

    private TextView tv_stepsTaken;


    //For backwards compatibility to android version 8.0
    @RequiresApi(api = Build.VERSION_CODES.Q)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepCountPreferences = getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);
        savedTotalSteps = stepCountPreferences.getFloat("dailyStepsKey", 0);

        tv_stepsTaken = findViewById(R.id.stepsTaken);

        tv_stepsTaken.setText(String.valueOf(Math.round(totalSteps)));

        Log.d("STEPCOUNTERDEBUG", "Load float!");

        //Check for permission for step counter
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        //Initialize sensorManager so it can getDefaultSensor later
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

    }


    @Override
    protected void onResume(){
        super.onResume();

        //Uses sensorManager to get TYPE_STEP_COUNTER, which uses accelerometer to calculate steps
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Print debug log if sensor isn't detected. Else start registering steps
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == null) {
            Log.d("STEPCOUNTERDEBUG", "No sensor detected!");
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor editor = stepCountPreferences.edit();

        editor.putFloat("dailyStepsKey", totalSteps + savedTotalSteps);
        editor.commit();
        Log.d("STEPCOUNTERDEBUG","put float to dailyStepsKey");

    }
    //Has to be called, else SensorEventListener (implementation at the beginning) wouldn't work
    public void onAccuracyChanged (Sensor sensor, int accuracy){

    }

    public void onSensorChanged(SensorEvent event) {

            //event.values has something to do with how accelerometer works.
            //It is just how type_step_counter counts steps, using accelerometer.
            totalSteps = event.values[0];

            //Put the values to int from float and round them
            tv_stepsTaken.setText(String.valueOf(Math.round(totalSteps + savedTotalSteps)));

            Log.d("STEPCOUNTERDEBUG","Steps go up!");

    }
}