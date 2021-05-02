package com.example.stepcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import static com.example.stepcounter.MainActivity.STEP_COUNT_PREFERENCES;

public class dayDataSingleton {

    private ArrayList<dayData> dayDatas;
    private static final dayDataSingleton dayDataInstance = new dayDataSingleton();

    public static dayDataSingleton getInstance() {
        return dayDataInstance;
    }


    public void addValue(String date, int steps, double distance, int calories){

        dayDatas.add(new dayData(date, steps, distance, calories));
    }

    public int getSize(){
        return dayDatas.size();
    }

    public void del(int index){
        dayDatas.remove(index);
    }

    public ArrayList<String> getAllDates(){
        ArrayList<String> allDates = new ArrayList<>();
        for (int i = 0; i < getSize(); i++){
            allDates.add(dayDatas.get(i).getDate());
        }
        return allDates;
    }

    private dayDataSingleton(){

        dayDatas = new ArrayList<>();
        /* Test data 1.week *
        dayDatas.add(new dayData("01/01/2021", 2345, 5.43, 200));
        dayDatas.add(new dayData("02/01/2021", 8423, 6.42, 200));
        dayDatas.add(new dayData("03/01/2021", 5843, 4.45, 200));
        dayDatas.add(new dayData("04/01/2021", 4221, 3.22, 200));
        dayDatas.add(new dayData("05/01/2021", 7432, 5.66, 200));
        dayDatas.add(new dayData("06/01/2021", 4325, 3.3, 200));
        dayDatas.add(new dayData("07/01/2021", 9523, 7.26, 200));
        */
    }



    public ArrayList<dayData> getDayDatas(){
        return dayDatas;
    }

    public void clearDayDatas(){
        this.dayDatas = null;
        dayDatas = new ArrayList<>();
    }
}
