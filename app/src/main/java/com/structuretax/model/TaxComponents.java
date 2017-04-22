package com.structuretax.model;

/**
 * Created by apple on 23/04/17.
 */

public class TaxComponents {

    private String componentName;
    private double monthly;
    private double yearly;
    private double tax;
    private boolean proof;

    public TaxComponents(String componentName, double monthly, double yearly, double tax, boolean proof) {
        this.componentName = componentName;
        this.monthly = monthly;
        this.yearly = yearly;
        this.tax = tax;
        this.proof = proof;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public double getMonthly() {
        return monthly;
    }

    public void setMonthly(double monthly) {
        this.monthly = monthly;
    }

    public double getYearly() {
        return yearly;
    }

    public void setYearly(double yearly) {
        this.yearly = yearly;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public boolean isProof() {
        return proof;
    }

    public void setProof(boolean proof) {
        this.proof = proof;
    }
}
