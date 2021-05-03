package com.example.stepcounter;

import java.util.ArrayList;

/**
 * Singleton for adding treats on ArrayList.
 */
public class TreatSingleton {

    private ArrayList<Treat> treats;

    private static final TreatSingleton treatInstance = new TreatSingleton();

    public static TreatSingleton getInstance() { return treatInstance;}

    private TreatSingleton() {
        treats = new ArrayList<>();

        treats.add(new Treat("Avokado", 200));
        treats.add(new Treat("Candy", 20));


    }

    public ArrayList<Treat> getTreats(){
        return treats;
    }

}
