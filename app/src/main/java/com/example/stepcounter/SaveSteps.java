package com.example.stepcounter;

import android.content.SharedPreferences;
import android.util.Log;

public class SaveSteps extends StepCounterComponent{

    private SharedPreferences stepCountPreferences;


    public SaveSteps(SharedPreferences sharedPreferences){
        this.stepCountPreferences = sharedPreferences;
    }

    public void saveTotalSteps(float freshSteps){
        if(freshSteps > 0) {
            SharedPreferences.Editor editor = stepCountPreferences.edit();
            editor.putFloat("dailyStepsKey", freshSteps);
            editor.apply();
            Log.d("STEPCOUNTERDEBUG", "put float to dailyStepsKey");
        }
    }
}
