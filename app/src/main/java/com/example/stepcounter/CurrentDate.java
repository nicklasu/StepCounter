package com.example.stepcounter;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class CurrentDate {

    public CurrentDate(){

    }

    public String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
     }
}

