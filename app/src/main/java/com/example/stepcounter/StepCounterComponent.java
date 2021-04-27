package com.example.stepcounter;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StepCounterComponent extends MainActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private float freshSteps;
    private TextView tv_stepsTaken;

    private TextView tv_burnedCalories;

    SharedPreferences stepCountPreferences;

    public StepCounterComponent(SensorManager sensorManager, Sensor stepSensor, TextView tv_stepsTaken, TextView tv_burnedCalories, SharedPreferences sharedPreferences) {
        this.sensorManager = sensorManager;
        this.stepSensor = stepSensor;
        this.tv_stepsTaken = tv_stepsTaken;
        this.tv_burnedCalories = tv_burnedCalories;
        this.stepCountPreferences = sharedPreferences;

    }

    public void countSteps() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.d("STEPCOUNTERDEBUG", "Sensor not found!");
        }
    }

    public void saveTotalSteps(Float freshSteps){
        SharedPreferences.Editor editor = stepCountPreferences.edit();

        editor.putFloat("dailyStepsKey", freshSteps);
        editor.apply();
        Log.d("STEPCOUNTERDEBUG","put float to dailyStepsKey");
    }

    public void onSensorChanged(SensorEvent event) {

        //event.values has something to do with how accelerometer works.
        //It is just how type_step_counter counts steps, using accelerometer.

        freshSteps = event.values[0];
        //Put the values to int from float and round them
        tv_stepsTaken.setText(String.valueOf(Math.round(freshSteps)));

        //Average burned calories for 73kg people from steps.
        tv_burnedCalories.setText(String.valueOf(Math.round(freshSteps*0.044)));

        saveTotalSteps(freshSteps);
        Log.d("STEPCOUNTERDEBUG","Steps go up!");
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}