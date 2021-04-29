package com.example.stepcounter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    Button switchToCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        switchToCalendar = findViewById(R.id.b_returnToCalendar);
        switchToCalendar.setOnClickListener(view -> switchToCalendarActivity());
    }

    private void switchToCalendarActivity(){
        Intent switchToCalendar = new Intent(this, CalendarActivity.class);
        startActivity(switchToCalendar);
    }

}
