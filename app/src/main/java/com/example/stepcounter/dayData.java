package com.example.stepcounter;

/**
 * Date, steps, distance and calories to HistoryActivityDetails
 * @return date, steps, distance and calories
 */
public class dayData {
    private String date;
    private int steps;
    private double distance;
    private int calories;

    public dayData(String date, int steps, double distance, int calories){
        this.date = date;
        this.steps = steps;
        this.distance = distance; // in km 0.5 == 500m
        this.calories = calories;
    }
    public int getSteps() {
        return steps;
    }

    public double getDistance() {
        return distance;
    }

    public int getCalories() {
        return calories;
    }

    public String getDate(){
        return date;
    }

    public String toString(){
        return date;
    }


}
