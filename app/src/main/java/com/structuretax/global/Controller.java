package com.structuretax.global;

import android.app.Application;
import android.util.Log;

import com.structuretax.model.Components;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 08/04/17.
 */

public class Controller extends Application{
    private static Controller sInstance;
    private ArrayList<Components> components;
    private String componentName;
    private String monthly;
    private String yearly;
    private boolean proof;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        components = new ArrayList<Components>();
    }

    public static synchronized Controller getInstance() {
        return sInstance;
    }

    public ArrayList<Components> salaryBreak(double ctc, boolean pf){

        if(pf) {
            computeBreakupWithPf(ctc);
        }
        else
            computeBreakupWithoutPf(ctc);

        return components;
    }

    private ArrayList<Components> computeBreakupWithPf(double ctc){
        components.clear();
        if(ctc <= 300000){
            components.add(new Components(Constants.BASIC, computeComponent(40,ctc), 12 * computeComponent(40, ctc), true));
        }
        return components;
    }

    private ArrayList<Components> computeBreakupWithoutPf(double ctc){
        components.clear();
//        if(ctc <= 300000){
        double sal, basic, hra, conveyance, medical, food, tel, helper, books, uniform, health, special;
        if(ctc > 0) {
            basic =  computeComponent(40, ctc);
            sal = ctc / 12 - basic;
            components.add(new Components(Constants.BASIC, basic, 12 * basic, false));
            hra = computeComponent(50, (40 * ctc) / 100);
            components.add(new Components(Constants.HRA, hra, 12 * hra, false));
            sal = sal - hra;
            if(sal > 0) {
                if(sal >= 1600) {
                    sal -= 1600;
                    components.add(new Components(Constants.Conveyance, 1600, 12 * 1600, false));
                }
                else {
                    components.add(new Components(Constants.Conveyance, sal, 12 * sal, false));
                    sal = 0;
                }
            }

            if(sal > 0) {
                if(sal >= 1250) {
                    sal -= 1250;
                    components.add(new Components(Constants.Medical, 1250, 12 * 1250, false));
                }
                else {
                    components.add(new Components(Constants.Medical, sal, 12 * sal, false));
                    sal = 0;
                }
            }

            Log.d("salary", sal + "");
        }



//      }
        return components;
    }

    private double computeComponent(long percent, double amount){

        Log.d("salaryiss", amount + " " + percent * amount);
        return (percent * amount)/(12 * 100);
    }
}
