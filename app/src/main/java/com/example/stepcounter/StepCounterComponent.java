package com.example.stepcounter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.Locale;

/**
 *Class which calculates steps using the sensor. Service. Updates steps when sensor detects movement.
 */
public class StepCounterComponent extends foregroundStepCount implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private AlarmManager alarmMgr;

    private float freshSteps;
    private float countSteps;
    //For testing set to 410 calories. Will be user-defined
    private int caloriesGoal = 410;

    private TextView tv_stepsTaken;
    private TextView tv_burnedCalories;
    private TextView tv_distance;
    private TextView tv_totalStepsPref;


    private SaveSteps saveSteps;

    private ProgressBar pb_caloriesGoal;


    SharedPreferences stepCountPreferences;


    public StepCounterComponent(SensorManager sensorManager, Sensor stepSensor, TextView tv_stepsTaken,
                                TextView tv_burnedCalories, TextView tv_distance, TextView tv_totalStepsPref, ProgressBar pb_caloriesGoal,
                                SharedPreferences sharedPreferences) {
        this.sensorManager = sensorManager;
        this.stepSensor = stepSensor;
        this.tv_stepsTaken = tv_stepsTaken;
        this.tv_burnedCalories = tv_burnedCalories;
        this.tv_distance = tv_distance;
        this.tv_totalStepsPref = tv_totalStepsPref;
        this.pb_caloriesGoal = pb_caloriesGoal;
        this.stepCountPreferences = sharedPreferences;
        saveSteps = new SaveSteps(stepCountPreferences);
        freshSteps = 0;
        countSteps = 0;
    }

    public StepCounterComponent() {
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

            if (countSteps == 0){
                countSteps = event.values[0];

            }
            freshSteps = stepCount - countSteps;
        }
        //Set calories target and current calories as progress
        pb_caloriesGoal.setMax(caloriesGoal);
        pb_caloriesGoal.setProgress((int)Math.round(freshSteps*0.044));

        //Put steps to string
        tv_stepsTaken.setText(String.valueOf(Math.round(freshSteps)));

        //Average burned calories for 73kg people from steps.
        tv_burnedCalories.setText(String.valueOf(Math.round(freshSteps*0.044)));

        //Distance from steps for 174 cm person.
        tv_distance.setText(String.format(Locale.ENGLISH,"%.2f",freshSteps/1400));

        tv_totalStepsPref.setText(String.valueOf(stepCountPreferences.getFloat("dailyStepsKey", 0)));

        Log.d("STEPCOUNTERDEBUG","Steps go up!");
    }
    public float giveFreshSteps(){
        return freshSteps;
    }

    //Necessary evil :)
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}