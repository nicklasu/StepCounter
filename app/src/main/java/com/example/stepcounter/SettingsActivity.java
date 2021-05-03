package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import static com.example.stepcounter.MainActivity.STEP_COUNT_PREFERENCES;

/**
 * Where the treats reside. Click a treat, and it becomes goal in MainActivity.
 * @author Nicklas Sundell
 */
public class SettingsActivity extends AppCompatActivity {

    private static SharedPreferences stepCounterPreferences;

    EditText editTreatName;
    EditText editTreatCalories;
    Button switchMain;
    Button submitButton;
    Button resetTreatsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editTreatName = findViewById(R.id.treatNameEdit);
        editTreatCalories = findViewById(R.id.treatCaloriesEdit);
        submitButton = findViewById(R.id.submitButton);
        resetTreatsButton = findViewById(R.id.resetTreatsButton);
        ListView lv_treatList = findViewById(R.id.lv_HistoryList);

        lv_treatList.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                TreatSingleton.getInstance().getTreats()
        ));


        lv_treatList.setOnItemClickListener((parent, view, position, id) -> {
            String treatName = TreatSingleton.getInstance().getTreats().get(position).getTreatName();
            int treatCalories = TreatSingleton.getInstance().getTreats().get(position).getTreatCalories();

            SharedPreferences.Editor editor = stepCounterPreferences.edit();
            editor.putString("treatNameKey", treatName);
            editor.putInt("treatCaloriesKey", treatCalories);
            editor.apply();

        });

        submitButton.setOnClickListener(view ->{
            String userInputTreatName = editTreatName.getText().toString();
            String userInputTreatCalories = editTreatCalories.getText().toString();
            if (userInputTreatName.equals("") || userInputTreatCalories.equals("")){
                Toast.makeText(SettingsActivity.this, "One of the fields is empty!", Toast.LENGTH_SHORT).show();
            } else {
                TreatSingleton.getInstance().addTreat(userInputTreatName, Integer.parseInt(userInputTreatCalories));
                finish();
                startActivity(getIntent());
            }
        });

        resetTreatsButton.setOnClickListener(view -> {
            TreatSingleton.getInstance().clearTreatData();
            finish();
            startActivity(getIntent());
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
