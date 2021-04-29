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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String STEP_COUNT_PREFERENCES = "StepCountPreferences";

    private float savedTotalSteps;

    SharedPreferences stepCountPreferences;

    //Get the SensorManager and attach it a name "sensorManager"
    private SensorManager sensorManager;
    private Sensor stepSensor;

    private StepCounterComponent stepCounter;


    private TextView textView_stepsTaken;

    private StorageComponent storageSteps;


    //Probably temp button
    Button switchToSettings;
    Button switchToCalendar;


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

        //Load data
        stepCountPreferences = getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);
        savedTotalSteps = stepCountPreferences.getFloat("dailyStepsKey", 0);

        //Sensor initializations
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Class initializations
        storageSteps = new StorageComponent(stepCountPreferences, savedTotalSteps);
        stepCounter = new StepCounterComponent(sensorManager, stepSensor, storageSteps.loadTotalSteps(), textView_stepsTaken);

        //Fresh data for textView
        textView_stepsTaken.setText(String.valueOf(Math.round(storageSteps.loadTotalSteps())));

        //Button for switching to treats
        switchToSettings = findViewById(R.id.switchToTreatActivity);
        switchToSettings.setOnClickListener(view -> switchSettingsActivity());

        //Button for switching to calendar
        switchToCalendar = findViewById(R.id.b_Calendar);
        switchToCalendar.setOnClickListener(view -> switchCalendarActivity());
    }
    private void switchSettingsActivity() {
        Intent switchToSettings = new Intent(this, SettingsActivity.class);
        startActivity(switchToSettings);
    }

    private void switchCalendarActivity(){
        Intent switchToCalendar = new Intent(this, CalendarActivity.class);
        startActivity(switchToCalendar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        stepCounter.countSteps();
    }

    @Override
    protected void onPause() {
        super.onPause();
        storageSteps.saveTotalSteps(stepCounter.saveSteps());
        stepCounter.countSteps();
    }


}