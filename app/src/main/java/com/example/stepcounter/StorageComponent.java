package com.example.stepcounter;
import android.content.SharedPreferences;
import android.util.Log;


public class StorageComponent extends MainActivity{
    private float savedTotalSteps;
    SharedPreferences stepCountPreferences;



    public StorageComponent(SharedPreferences sharedPreferences, Float savedSteps){
        this.stepCountPreferences = sharedPreferences;
        this.savedTotalSteps = savedSteps;

    }

    public void saveTotalSteps(Float freshSteps){
        SharedPreferences.Editor editor = stepCountPreferences.edit();

        editor.putFloat("dailyStepsKey", freshSteps);
        editor.apply();
        Log.d("STEPCOUNTERDEBUG","put float to dailyStepsKey");

    }
    public Float loadTotalSteps(){
        return savedTotalSteps;

    }
}
