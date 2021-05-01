package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Where the treats reside. Click a treat, and it becomes goal in MainActivity.
 */
public class SettingsActivity extends AppCompatActivity {

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

        //Back to MainActivity
        switchMain = findViewById(R.id.switchToMain);
        switchMain.setOnClickListener(view -> switchToMainActivity());
    }
    private void switchToMainActivity() {
        Intent switchToMain = new Intent(this, MainActivity.class);
        startActivity(switchToMain);
    }
}
