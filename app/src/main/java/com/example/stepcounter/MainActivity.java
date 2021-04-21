package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //Get the SensorManager and attach it a name "sensorManager"
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private float totalSteps = 0f;
    private TextView stepsTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stepsTaken = findViewById(R.id.stepsTaken);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


    }

    @Override
    protected void onResume(){
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == null) {
            Log.d("STEPCOUNTERDEBUG", "No sensor detected!");
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }

    }
    public void onAccuracyChanged (Sensor sensor, int accuracy){

    }

    public void onSensorChanged(SensorEvent event) {

            totalSteps = event.values[0];
            String currentSteps = String.valueOf(totalSteps);
            stepsTaken.setText(currentSteps);
            Log.d("STEPCOUNTERDEBUG","Steps go up!");

    }
}