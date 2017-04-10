package com.structuretax.model;

/**
 * Created by apple on 09/04/17.
 */

public class Components {

    private String componentName;
    private double monthly;
    private double yearly;
    private boolean proof;

    public Components(String componentName, double monthly, double yearly, boolean proof) {
        this.componentName = componentName;
        this.monthly = monthly;
        this.yearly = yearly;
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

    public void setMonthly(long monthly) {
        this.monthly = monthly;
    }

    public double getYearly() {
        return yearly;
    }

    public void setYearly(long yearly) {
        this.yearly = yearly;
    }

    public boolean isProof() {
        return proof;
    }

    public void setProof(boolean proof) {
        this.proof = proof;
    }
}
