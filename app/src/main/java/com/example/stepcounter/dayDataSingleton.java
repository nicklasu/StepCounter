package com.example.stepcounter;

import java.util.ArrayList;

public class dayDataSingleton {

    private ArrayList<dayData> dayDatas;
    private static final dayDataSingleton dayDataInstance = new dayDataSingleton();

    public static dayDataSingleton getInstance() {
        return dayDataInstance;
    }

    private dayDataSingleton(){
        dayDatas = new ArrayList<>();
        /* Test data 1.week */
        dayDatas.add(new dayData("1.1.2021", 7124, 5.43, 200));
        dayDatas.add(new dayData("2.1.2021", 8423, 6.42, 200));
        dayDatas.add(new dayData("3.1.2021", 5843, 4.45, 200));
        dayDatas.add(new dayData("4.1.2021", 4221, 3.22, 200));
        dayDatas.add(new dayData("5.1.2021", 7432, 5.66, 200));
        dayDatas.add(new dayData("6.1.2021", 4325, 3.3, 200));
        dayDatas.add(new dayData("7.1.2021", 9523, 7.26, 200));
    }

    public void addValue(String date, int steps, double distance, int calories){
        dayDatas.add(new dayData(date, steps, distance, calories));
    }

    public ArrayList<dayData> getDayDatas(){
        return dayDatas;
    }
}
