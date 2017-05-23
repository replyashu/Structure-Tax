package com.structuretax.global;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.structuretax.model.Components;
import com.structuretax.model.TaxComponents;

import io.fabric.sdk.android.Fabric;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 08/04/17.
 */

public class Controller extends Application{
    private static Controller sInstance;
    private ArrayList<Components> components;
    private ArrayList<TaxComponents> taxComponents;
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
        Fabric.with(this, new Crashlytics());
        sInstance = this;

        components = new ArrayList<Components>();
        taxComponents = new ArrayList<>();
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

    public ArrayList<TaxComponents> taxBreakup(ArrayList<Components> components){
        taxComponents.clear();

        double tax = 0;
        double basic = 0;
        double saving = 0;
        // Compute Tax and Return
        for(int i = 0; i < components.size(); i++){

            String name = components.get(i).getComponentName();
            double monthly = components.get(i).getMonthly();
            double yearly = components.get(i).getYearly();

            // TODO 13 cases to be calculated and handled
            if(name.equalsIgnoreCase(Constants.BASIC)){
                basic = yearly;
                taxComponents.add(new TaxComponents(name, monthly, yearly, yearly, false));
            }
            else if(name.equalsIgnoreCase(Constants.HRA)){
                double hra = computeHRATax(yearly, basic);
                if(hra <= 8333){
                    taxComponents.add(new TaxComponents(name, monthly, yearly, hra, false));
                    tax += hra;
                    saving += hra;
                }
                else {
                    taxComponents.add(new TaxComponents(name, monthly, yearly, hra, true));
                    tax += yearly;
                    saving += yearly;
                }

            }

            else if(name.equalsIgnoreCase(Constants.Pf)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, false));
                tax -= yearly;
                saving += yearly;
            }
            else if(name.equalsIgnoreCase(Constants.Conveyance)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, false));
                saving += yearly;
            }
            else if(name.equalsIgnoreCase(Constants.Medical)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, false));
                saving += yearly;
            }
            else if(name.equalsIgnoreCase(Constants.Food)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, false));
                saving += yearly;
            }
            else if(name.contains(Constants.Mobile_And_Telephone)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, true));
                saving += yearly;
            }
            else if(name.contains(Constants.Lta)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, true));
                saving += yearly;
            }
            else if(name.contains(Constants.Helper)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, false));
                saving += yearly;
            }
            else if(name.contains(Constants.Books)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, true));
                saving += yearly;
            }
            else if(name.contains(Constants.Uniform)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, true));
                saving += yearly;
            }
            else if(name.contains(Constants.Health)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, 0, true));
                saving += yearly;
            }
            else if(name.contains(Constants.Special)){
                taxComponents.add(new TaxComponents(name, monthly, yearly, yearly, false));
            }
            else if(name.contains(Constants.Net)){
                tax = computeTaxableSalary(yearly, tax);


                taxComponents.add(new TaxComponents("Take Home", monthly - tax/12, yearly - tax, tax, false));
                saving = yearly - basic;
                taxComponents.add(new TaxComponents("Savings", saving, saving, tax, false));
                if(tax == 0){
                    taxComponents.add(new TaxComponents("Tax", 0, 0, tax, false));
                }
                else
                    taxComponents.add(new TaxComponents("Tax", monthly, yearly, tax, false));
            }


        }

        return  taxComponents;
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
        return (percent * amount)/(12 * 100);
    }

    private double computeHRATax(double amount, double basic){
        double hra = amount;
        double metro = computeComponent(40, basic);

        return returnMinHRA(hra, metro);
    }

    private double returnMinHRA(double amount1, double amount2){
        if(amount1 <= amount2)
            return  amount1;
        else
            return amount2;
    }

    public double computeTaxableSalary(double salary, double tax){
        double cut = salary - tax;
        tax = 0;
        if(cut <= 500000 && cut > 250000){
            cut -= 250000;
//            double ded = 50000 - cut;
            double payableTax = 0.05 * cut + 2400;
            payableTax += computeComponent(3, payableTax);
            tax = payableTax;
        }
        else if(cut <= 1000000 && cut > 500000){
            cut -= 500000;
//            double ded = 100000 - cut;
            double payableTax =12500 + 0.2 * cut + 2400;
            payableTax += computeComponent(3, payableTax);
            tax = payableTax;
        }
        else if(cut <= 5000000 && cut > 1000000){
            cut -= 1000000;
            double payableTax = 112500 + 0.3 * cut + 2400;
            payableTax += computeComponent(3, payableTax);
            tax = payableTax;
        }
        else if(cut > 5000000 && cut <= 10000000){
            cut -= 1000000;
            double payableTax = 112500 + 0.3 * cut + 2400;
            payableTax += computeComponent(10, payableTax);
            payableTax += computeComponent(3, payableTax);
            tax = payableTax;
        }
        else if(cut > 10000000) {
            cut -= 1000000;
            double payableTax = 112500 + 0.3 * cut + 2400;
            payableTax += computeComponent(15, payableTax);
            payableTax += computeComponent(3, payableTax);
            tax = payableTax;
        }

        return tax;
    }


}
