package com.example.stepcounter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private float savedSteps;
    private int treatCalories = 420;
    SharedPreferences stepCountPreferences;

    //Get the SensorManager and attach it a name "sensorManager"
    private SensorManager sensorManager;
    private Sensor stepSensor;

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

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("STEPCOUNTERDEBUG", "OnReceive! New steps from foreGroundStepCount!");
            float freshSteps =  intent.getExtras().getInt("freshSteps", 4);
            freshSteps += savedSteps;
            progressBar_caloriesGoal.setMax(treatCalories);
            progressBar_caloriesGoal.setProgress((int)Math.round((freshSteps)*0.044));

            //Put steps to string
            textView_stepsTaken.setText(String.valueOf(Math.round(freshSteps)));

            //Average burned calories for 73kg people from steps.
            textView_caloriesBurned.setText(String.valueOf(Math.round((freshSteps)*0.044)));

            //Distance from steps for 174 cm person.
            textView_distance.setText(String.format(Locale.ENGLISH,"%.2f",(freshSteps)/1400));

            textView_totalStepsPref.setText(String.valueOf(stepCountPreferences.getFloat("dailyStepsKey", 0)));

        }
    };

    //Required for backwards compatibility to API 26
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("STEPCOUNTERDEBUG","onCreate()");


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
        savedSteps = stepCountPreferences.getFloat("dailyStepsKey", 0);


        //Sensor initializations
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Class initializations
        //stepCounter = new StepCounterComponent(sensorManager, stepSensor, textView_stepsTaken, textView_caloriesBurned, textView_distance, textView_totalStepsPref, progressBar_caloriesGoal, stepCountPreferences);


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

        //stepCounter.countSteps();
        foregroundStepCount.givePref(this.getApplicationContext());
        saveStepsReceiver.givePref(this.getApplicationContext());
        saveNightlyStepsReceiver.givePref(this.getApplicationContext());

    }

    private void switchToCalendarActivity(){
        startActivity(new Intent(MainActivity.this, CalendarActivity.class));
    }


    private void switchSettingsActivity() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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
        Log.d("STEPCOUNTERDEBUG","onResume()");
        registerReceiver(receiver, new IntentFilter("stepCounter.freshSteps"));
        progressBar_caloriesGoal.setMax(treatCalories);
        progressBar_caloriesGoal.setProgress((int)Math.round((savedSteps)*0.044));

        //Put steps to string
        textView_stepsTaken.setText(String.valueOf(Math.round(savedSteps)));

        //Average burned calories for 73kg people from steps.
        textView_caloriesBurned.setText(String.valueOf(Math.round((savedSteps)*0.044)));

        //Distance from steps for 174 cm person.
        textView_distance.setText(String.format(Locale.ENGLISH,"%.2f",(savedSteps)/1400));

        textView_totalStepsPref.setText(String.valueOf(stepCountPreferences.getFloat("dailyStepsKey", 0)));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("STEPCOUNTERDEBUG", "onPause()");
        unregisterReceiver(receiver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("STEPCOUNTERDEBUG", "onDestroy()");
    }
}