package com.example.stepcounter;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Class for saving steps, child of StepCounterComponent.
 */
public class SaveSteps extends StepCounterComponent{

    private SharedPreferences stepCountPreferences;

    public SaveSteps(SharedPreferences sharedPreferences){
        this.stepCountPreferences = sharedPreferences;
    }

   /* public void saveTotalSteps(){
        if(giveFreshSteps() > 0) {
            SharedPreferences.Editor editor = stepCountPreferences.edit();
            editor.putFloat("dailyStepsKey", giveFreshSteps());
            editor.apply();
            Log.d("STEPCOUNTERDEBUG", "put float to dailyStepsKey");

        }
    }*/
}
