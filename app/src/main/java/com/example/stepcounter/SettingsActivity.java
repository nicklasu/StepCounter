package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import static com.example.stepcounter.MainActivity.STEP_COUNT_PREFERENCES;

/**
 * Where the treats reside. Click a treat, and it becomes goal in MainActivity.
 * @author Nicklas Sundell
 */
public class SettingsActivity extends AppCompatActivity {

    private static SharedPreferences stepCounterPreferences;

    Button switchMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ListView lv_treatList = findViewById(R.id.lv_HistoryList);

        lv_treatList.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                TreatSingleton.getInstance().getTreats()
        ));


        lv_treatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String treatName = TreatSingleton.getInstance().getTreats().get(position).getTreatName();
                int treatCalories = TreatSingleton.getInstance().getTreats().get(position).getTreatCalories();

                SharedPreferences.Editor editor = stepCounterPreferences.edit();
                editor.putString("treatNameKey", treatName);
                editor.putInt("treatCaloriesKey", treatCalories);
                editor.apply();

            }
        });
        //Back to MainActivity
        switchMain = findViewById(R.id.switchToMain);
        switchMain.setOnClickListener(view -> switchToMainActivity());
    }
    private void switchToMainActivity() {
        super.onBackPressed();
        finish();
    }
    public static void givePref(Context context){
        stepCounterPreferences = context.getSharedPreferences(STEP_COUNT_PREFERENCES, Context.MODE_PRIVATE);
    }
}
