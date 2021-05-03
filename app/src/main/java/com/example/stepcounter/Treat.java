package com.example.stepcounter;

import androidx.annotation.NonNull;

public class Treat {

    private String treatName;
    private int treatCalories;

    public Treat (String treatName, int treatCalories){
        this.treatName = treatName;
        this.treatCalories = treatCalories;
    }

    @NonNull
    @Override
    public String toString(){ return this.treatName; }

    public int getTreatCalories(){ return this.treatCalories; }

    public String getTreatName(){ return this.treatName; }
}
