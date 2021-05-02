package com.example.stepcounter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivityDetails extends AppCompatActivity {
    Button switchToHistory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);


        switchToHistory = findViewById(R.id.b_returnToHistory);
        switchToHistory.setOnClickListener(view -> switchToHistoryActivity());
        Intent intent = getIntent();

        TextView date = findViewById(R.id.tv_ahd_date);
        TextView calories = findViewById(R.id.tv_ahd_calories);
        TextView distance = findViewById(R.id.tv_ahd_distance);
        TextView steps = findViewById(R.id.tv_ahd_steps);

        date.setText(intent.getStringExtra(HistoryActivity.DATE));
        calories.setText(intent.getStringExtra(HistoryActivity.CALO));
        distance.setText(intent.getStringExtra(HistoryActivity.DIST));
        steps.setText(intent.getStringExtra(HistoryActivity.STEPS));

    }

    private void switchToHistoryActivity(){
        super.onBackPressed();
        finish();
    }
}
