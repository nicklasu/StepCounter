package com.example.stepcounter;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
/**
 * Gives current day if CurrentDate currentDate = new CurrentDate()
 * @return Current day in dd/MM/yyyy format. For example 03/05/2021
 * @author Tatu Talvikko
 */

public class CurrentDate {

    public CurrentDate(){

    }

    public String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
     }
}

