package com.example.stepcounter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

public class StepCounterComponent extends MainActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private float totalSteps;
    private float savedTotalSteps;
    private TextView tv_stepsTaken;

    public StepCounterComponent(SensorManager sensorManager, Sensor stepSensor, float savedTotalSteps, TextView tv_stepsTaken) {
        this.sensorManager = sensorManager;
        this.stepSensor = stepSensor;
        this.tv_stepsTaken = tv_stepsTaken;
        this.savedTotalSteps = savedTotalSteps;

    }


    public void countSteps() {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.d("STEPCOUNTERDEBUG", "Sensor not found!");
        }
    }


    public void onSensorChanged(SensorEvent event) {

        //event.values has something to do with how accelerometer works.
        //It is just how type_step_counter counts steps, using accelerometer.
        totalSteps = event.values[0];

        //Put the values to int from float and round them
        tv_stepsTaken.setText(String.valueOf(Math.round(savedTotalSteps + totalSteps)));

        Log.d("STEPCOUNTERDEBUG","Steps go up!");

    }
    public Float saveSteps(){
        return savedTotalSteps + totalSteps;
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}