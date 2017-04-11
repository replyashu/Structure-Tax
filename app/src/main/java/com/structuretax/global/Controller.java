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
    public static synchronized Controller getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        components = new ArrayList<Components>();
    }


    public ArrayList<Components> salaryBreak(double ctc, boolean pf, int optimize){


        if(pf) {
            computeBreakupWithPf(ctc, optimize);
        }
        else {
            computeBreakupWithoutPf(ctc, optimize);
        }

        return components;
    }

    private ArrayList<Components> computeBreakupWithPf(double ctc, int optimize){
        components.clear();
        int com = 0;
        double sal, basic, hra, food, tel, helper, books, uniform, health, special, pf;
        sal = ctc;
        while (sal > 0){
            switch (com){

                // Basic
                case 0:
                    basic =  computeComponent(optimize, ctc);
                    sal = ctc / 12 - basic;
                    components.add(new Components(Constants.BASIC, basic, 12 * basic, false));
                    com = 1;
                    break;

                // HRA
                case 1:
                    hra = computeComponent(50, (optimize * ctc) / 100);
                    components.add(new Components(Constants.HRA, hra, 12 * hra, true));
                    sal = sal - hra;
                    com = 12;
                    break;

                // Conveyance
                case 2:
                    if(sal >= 1600) {
                        sal -= 1600;
                        components.add(new Components(Constants.Conveyance, 1600, 12 * 1600, false));
                        com = 3;
                    }
                    else {
                        components.add(new Components(Constants.Conveyance, sal, 12 * sal, false));
                        sal = 0;
                        com = 10;
                    }

                    break;

                // Medical
                case 3:
                    if(sal >= 1250) {
                        sal -= 1250;
                        components.add(new Components(Constants.Medical, 1250, 12 * 1250, false));
                    }
                    else {
                        components.add(new Components(Constants.Medical, sal, 12 * sal, false));
                        sal = 0;
                    }
                    com = 4;
                    break;

                // Food
                case 4:
                    if(sal >= 1100){
                        if(sal >= 10000){
                            components.add(new Components(Constants.Food, 2200, 12 * 2200, false));
                            sal -= 2200;
                            com = 5;
                        }
                        else {
                            components.add(new Components(Constants.Food, 1100, 12 * 1100, false));
                            sal -= 1100;
                            com = 10;
                        }
                    }
                    else
                        com = 10;
                    break;

                // Mobile And Telephone
                case 5:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Mobile_And_Telephone, 2250, 12 * 2250, false));
                        sal -= 2250;
                        com = 11;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Mobile_And_Telephone, 1000, 12 * 1000, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;

                // Helper
                case 6:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Helper, 2250, 12 * 2250, false));
                        sal -= 2250;
                        com = 7;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Helper, 1100, 12 * 1100, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;

                //Books And Periodicals
                case 7:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Books, 2500, 12 * 2500, false));
                        sal -= 2250;
                        com = 8;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Books, 1250, 12 * 1250, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;


                // Uniform
                case 8:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Uniform, 2250, 12 * 2250, false));
                        sal -= 2250;
                        com = 9;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Uniform, 1000, 12 * 1000, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;

                // Health
                case 9:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Health, 2250, 12 * 2250, false));
                        sal -= 2250;
                        com = 10;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Health, 1000, 12 * 1000, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;

                // Special
                case 10:
                    components.add(new Components(Constants.Special, sal, 12 * sal, false));
                    sal = 0;
                    break;
                // LTA
                case 11:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Lta, 400, 12 * 400, false));
                        sal -= 2250;
                        com = 6;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        com = 10;
                    }
                    break;

                // PF
                case 12:
                    basic = computeComponent(optimize, ctc);
                    pf = computeComponent(12, basic * 12);
                    components.add(new Components(Constants.Pf, pf, 12 * pf, false));
                    sal -= pf;
                    com = 2;
                    break;

            }
        }

        components.add(new Components(Constants.Net, ctc / 12, ctc, false));

        return components;
    }

    private ArrayList<Components> computeBreakupWithoutPf(double ctc, int optimize){
        components.clear();
        int com = 0;
        double sal, basic, hra, food, tel, helper, books, uniform, health, special;
        sal = ctc;

        while (sal > 0){
            switch (com){

                // Basic
                case 0:
                    basic =  computeComponent(optimize, ctc);
                    sal = ctc / 12 - basic;
                    components.add(new Components(Constants.BASIC, basic, 12 * basic, false));
                    com = 1;
                    break;

                // HRA
                case 1:
                    hra = computeComponent(50, (optimize * ctc) / 100);
                    components.add(new Components(Constants.HRA, hra, 12 * hra, true));
                    sal = sal - hra;
                    com = 2;
                    break;

                // Conveyance
                case 2:
                    if(sal >= 1600) {
                        sal -= 1600;
                        components.add(new Components(Constants.Conveyance, 1600, 12 * 1600, false));
                        com = 3;
                    }
                    else {
                        components.add(new Components(Constants.Conveyance, sal, 12 * sal, false));
                        sal = 0;
                        com = 10;
                    }

                    break;

                // Medical
                case 3:
                    if(sal >= 1250) {
                        sal -= 1250;
                        components.add(new Components(Constants.Medical, 1250, 12 * 1250, false));
                    }
                    else {
                        components.add(new Components(Constants.Medical, sal, 12 * sal, false));
                        sal = 0;
                    }
                    com = 4;
                    break;

                // Food
                case 4:
                    if(sal >= 1100){
                        if(sal >= 10000){
                            components.add(new Components(Constants.Food, 2200, 12 * 2200, false));
                            sal -= 2200;
                            com = 5;
                        }
                        else if(sal >= 2500 && sal < 10000){
                            components.add(new Components(Constants.Food, 1100, 12 * 1100, false));
                            sal -= 1100;
                            com = 10;
                        }
                        else
                            com = 10;
                    }
                    else
                        com = 10;
                    break;

                // Mobile And Telephone
                case 5:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Mobile_And_Telephone, 2250, 12 * 2250, false));
                        sal -= 2250;
                        com = 11;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Mobile_And_Telephone, 1000, 12 * 1000, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;

                // Helper
                case 6:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Helper, 2250, 12 * 2250, false));
                        sal -= 2250;
                        com = 7;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Helper, 1100, 12 * 1100, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;

                //Books And Periodicals
                case 7:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Books, 2500, 12 * 2500, false));
                        sal -= 2250;
                        com = 8;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Books, 1250, 12 * 1250, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;


                // Uniform
                case 8:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Uniform, 2250, 12 * 2250, false));
                        sal -= 2250;
                        com = 9;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Uniform, 1000, 12 * 1000, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;

                // Health
                case 9:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Health, 2250, 12 * 2250, false));
                        sal -= 2250;
                        com = 10;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        components.add(new Components(Constants.Health, 1000, 12 * 1000, false));
                        sal -= 1000;
                        com = 10;
                    }
                    break;

                // Special
                case 10:
                    components.add(new Components(Constants.Special, sal, 12 * sal, false));
                    sal = 0;
                    break;
                // LTA
                case 11:
                    if(sal >= 10000){
                        components.add(new Components(Constants.Lta, 400, 12 * 400, false));
                        sal -= 2250;
                        com = 6;
                    }
                    else if(sal >= 2500 && sal < 10000){
                        com = 10;
                    }
                    break;

            }
        }

        components.add(new Components(Constants.Net, ctc / 12, ctc, false));


//      }
        return components;
    }

    private double computeComponent(long percent, double amount){

        Log.d("salaryiss", amount + " " + percent * amount);
        return (percent * amount)/(12 * 100);
    }
}
