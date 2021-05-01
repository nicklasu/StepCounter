package com.example.stepcounter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    Button switchToMain;
    Button switchToHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CurrentDate ff = new CurrentDate();
        // Reutrn to main activity
        switchToMain = findViewById(R.id.b_Return);
        switchToMain.setOnClickListener(view -> switchToMainActivity());

        // Go to History activity
        switchToHistory = findViewById(R.id.b_Tohistory);
        switchToHistory.setOnClickListener(view -> switchToHistoryActivity());

        TextView date = findViewById(R.id.tv_Weekday);
        date.setText(ff.getDate());
    }

    private void switchToHistoryActivity(){
        Intent switchToHistory = new Intent(this, HistoryActivity.class);
        startActivity(switchToHistory);
    }

    private void switchToMainActivity(){
        Intent switchToMain = new Intent(this, MainActivity.class);
        startActivity(switchToMain);
    }
}
