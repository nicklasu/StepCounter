package com.example.stepcounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import static com.example.stepcounter.MainActivity.STEP_COUNT_PREFERENCES;

public class saveStepsReceiver extends BroadcastReceiver {

    private static SharedPreferences stepCounterPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {

        float previousSteps = stepCounterPreferences.getFloat("dailyStepsKey", 0);

        Log.d("STEPCOUNTERDEBUG", "saved xdd");
        float saveSteps =  intent.getExtras().getInt("StepsToSave", 4);

        saveSteps += previousSteps;

        Log.d("STEPCOUNTERDEBUG", "put float to dailyStepsKey");

        SharedPreferences.Editor editor = stepCounterPreferences.edit();
        editor.putFloat("dailyStepsKey", saveSteps);
        editor.apply();
    }


    public static void givePref(Context context){
        stepCounterPreferences = context.getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);

    }

}
