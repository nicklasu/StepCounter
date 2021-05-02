package com.example.stepcounter;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import static com.example.stepcounter.MainActivity.STEP_COUNT_PREFERENCES;

public class saveNightlyStepsReceiver extends BroadcastReceiver {

    CurrentDate currentDate;
    private static SharedPreferences stepCounterPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {

        currentDate = new CurrentDate();

        String today = currentDate.getDate();

        float previousSteps = stepCounterPreferences.getFloat("dailyStepsKey", 0);

        float saveSteps =  intent.getExtras().getInt("StepsToSave", 4);

        saveSteps += previousSteps;
        dayDataSingleton.getInstance().addValue(today, Math.round(saveSteps), Math.round((saveSteps/1400)*100.0)/100.0, Math.round(saveSteps/23));


        SharedPreferences.Editor editor = stepCounterPreferences.edit();
        editor.putFloat(today, saveSteps);
        editor.putFloat("dailyStepsKey", 0f);
        editor.apply();

        Log.d("STEPCOUNTERDEBUG", "Emptied dailyStepsKey!");
    }


    public static void givePref(Context context){
        stepCounterPreferences = context.getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);

    }



}
