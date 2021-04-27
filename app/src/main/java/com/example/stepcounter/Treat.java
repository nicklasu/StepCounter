package com.example.stepcounter;

import androidx.annotation.NonNull;

public class Treat {

    private String treat;

    public Treat (String treat){
        this.treat = treat;

    }

    @NonNull
    @Override
    public String toString(){ return this.treat; }

}
