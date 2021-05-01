package com.example.stepcounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import static com.example.stepcounter.MainActivity.STEP_COUNT_PREFERENCES;

public class saveDailyReceiver extends BroadcastReceiver {
    private static SharedPreferences stepCounterPreferences;

    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("STEPCOUNTERDEBUG", "save daily");
        float saveSteps =  intent.getExtras().getInt("StepsToSave", 4);
        Log.d("STEPCOUNTERDEBUG", "put float to yesterdaykey! dailystepkey reseted!");

        SharedPreferences.Editor editor = stepCounterPreferences.edit();
        editor.clear();
        editor.putFloat("yesterdayStepsKey", saveSteps);
        editor.commit();
    }
    public static void givePref(Context context){
        stepCounterPreferences = context.getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);
    }
}
