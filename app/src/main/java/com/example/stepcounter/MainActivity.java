package com.example.stepcounter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Main program view, which also initializes sensors and preferences.
 */
public class MainActivity extends AppCompatActivity {

    public static final String STEP_COUNT_PREFERENCES = "StepCountPreferences";

    private float totalSteps;

    SharedPreferences stepCountPreferences;

    //Get the SensorManager and attach it a name "sensorManager"
    private SensorManager sensorManager;
    private Sensor stepSensor;

    private StepCounterComponent stepCounter;

    //TextViews
    private TextView textView_stepsTaken;
    private TextView textView_caloriesBurned;
    private TextView textView_distance;

    //Progress bar
    private ProgressBar progressBar_caloriesGoal;
    //Button for switching to settings
    Button switchToSettings;


    //Required for backwards compatibility to API 26
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check for permission for step counter
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        //FindView
        textView_stepsTaken = findViewById(R.id.stepsTaken);
        textView_caloriesBurned = findViewById(R.id.caloriesBurned);
        textView_distance = findViewById(R.id.distanceView);

        progressBar_caloriesGoal = findViewById(R.id.caloriesGoal);

        //Load data
        stepCountPreferences = getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);
        totalSteps = stepCountPreferences.getFloat("dailyStepsKey", 0);

        //Sensor initializations
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Class initializations
        stepCounter = new StepCounterComponent(sensorManager, stepSensor, textView_stepsTaken, textView_caloriesBurned, textView_distance, progressBar_caloriesGoal, stepCountPreferences);

        //Button for switching to treats
        switchToSettings = findViewById(R.id.switchToTreatActivity);
        switchToSettings.setOnClickListener(view -> switchSettingsActivity());
        stepCounter.countSteps();
    }

    private void switchSettingsActivity() {
        Intent switchToSettings = new Intent(this, SettingsActivity.class);
        startActivity(switchToSettings);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}