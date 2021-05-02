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
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.Map;


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
    private TextView textView_totalStepsPref;
    //Progress bar
    private ProgressBar progressBar_caloriesGoal;
    //Button for switching to settings
    Button switchToSettings;
    Button resetTotalPref;
    Button loadListView;
    Button saveListView;
    Button switchToCalendar;

    CurrentDate currentDate;
    //Required for backwards compatibility to API 26
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentDate = new CurrentDate();

        Intent stepCounterService = new Intent(this, foregroundStepCount.class);
        startForegroundService(stepCounterService);


        //Check for permission for step counter
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        //FindView
        textView_stepsTaken = findViewById(R.id.stepsTaken);
        textView_caloriesBurned = findViewById(R.id.caloriesBurned);
        textView_distance = findViewById(R.id.distanceView);
        textView_totalStepsPref = findViewById(R.id.totalStepsFromPref);
        progressBar_caloriesGoal = findViewById(R.id.caloriesGoal);


        //Load data
        stepCountPreferences = getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);


        //Sensor initializations
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Class initializations
        stepCounter = new StepCounterComponent(sensorManager, stepSensor, textView_stepsTaken, textView_caloriesBurned, textView_distance, textView_totalStepsPref, progressBar_caloriesGoal, stepCountPreferences);


        resetTotalPref = findViewById(R.id.resetTotalStepsPref);
        resetTotalPref.setOnClickListener(view -> resetPreferenceSteps());
        loadListView = findViewById(R.id.loadToList);
        loadListView.setOnClickListener(view -> loadToList());
        saveListView = findViewById(R.id.saveToList);
        saveListView.setOnClickListener(view -> saveToList());


        //Button for switching to treats
        switchToSettings = findViewById(R.id.switchToTreatActivity);
        switchToSettings.setOnClickListener(view -> switchSettingsActivity());

        // Button for switching to Calendar
        switchToCalendar = findViewById(R.id.b_Calendar);
        switchToCalendar.setOnClickListener(view -> switchToCalendarActivity());

        stepCounter.countSteps();
        foregroundStepCount.givePref(this.getApplicationContext());
        saveStepsReceiver.givePref(this.getApplicationContext());
        saveNightlyStepsReceiver.givePref(this.getApplicationContext());

    }

    private void switchToCalendarActivity(){
        Intent switchToCalendar = new Intent(this, CalendarActivity.class);
        startActivity(switchToCalendar);
    }


    private void switchSettingsActivity() {
        Intent switchToSettings = new Intent(this, SettingsActivity.class);
        startActivity(switchToSettings);
    }

    private void resetPreferenceSteps(){
        Map<String, ?> allEntries = stepCountPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(!entry.getKey().equals("dailyStepsKey")) {
                Log.d("STEPCOUNTERDEBUG", entry.getKey() + ": " + entry.getValue().toString());
                SharedPreferences.Editor editor = stepCountPreferences.edit();
                editor.remove(entry.getKey());
                editor.apply();
            }
        }
        dayDataSingleton.getInstance().clearDayDatas();

    }

    private void loadToList(){
        Map<String, ?> allEntries = stepCountPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(!entry.getKey().equals("dailyStepsKey")) {
                Log.d("STEPCOUNTERDEBUG", entry.getKey() + ": " + entry.getValue().toString());
                float steps = Float.parseFloat(entry.getValue().toString());
                dayDataSingleton.getInstance().addValue(entry.getKey(), Math.round(Float.parseFloat(entry.getValue().toString())), Math.round(steps/1400)*100.0/100.0, Math.round(steps/23));
            }
        }
    }

    private void saveToList(){
        totalSteps = stepCountPreferences.getFloat("dailyStepsKey", 0);
        String today = currentDate.getDate();
        dayDataSingleton.getInstance().addValue(today, Math.round(totalSteps), Math.round((totalSteps/1400)*100.0)/100.0, Math.round(totalSteps/23));
        SharedPreferences.Editor editor = stepCountPreferences.edit();
        editor.putFloat(today, totalSteps);
        editor.apply();
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