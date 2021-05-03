package com.example.stepcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/**
 * ListView, where one can see his previous days. Clicking an item launches HistoryActivityDetails, which shows steps, calories and distance.
 */
public class HistoryActivity extends AppCompatActivity {
    // return button
    Button switchToCalendar;
    public static final String STEPS = "com.example.p.STEPS";
    public static final String DIST = "com.example.p.DISTANCE";
    public static final String CALO = "com.example.p.CALORIES";
    public static final String DATE = "com.example.p.DATE";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Find listView
        ListView lv_history = findViewById(R.id.lv_HistoryList);
        lv_history.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dayDataSingleton.getInstance().getDayDatas()
        ));

        // return button, find it
        switchToCalendar = findViewById(R.id.b_returnToMain);
        switchToCalendar.setOnClickListener(view -> switchToCalendarActivity());

        lv_history.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(HistoryActivity.this, HistoryActivityDetails.class);

            String steps = ""+dayDataSingleton.getInstance().getDayDatas().get(position).getSteps();
            String distance = ""+dayDataSingleton.getInstance().getDayDatas().get(position).getDistance();
            String calories = ""+dayDataSingleton.getInstance().getDayDatas().get(position).getCalories();
            String date = ""+dayDataSingleton.getInstance().getDayDatas().get(position).getDate();

            intent.putExtra(STEPS, steps);
            intent.putExtra(DIST, distance);
            intent.putExtra(CALO, calories);
            intent.putExtra(DATE, date);

            startActivity(intent);
        });
    }

    // return method
    private void switchToCalendarActivity(){
        super.onBackPressed();
        finish();
    }

}
