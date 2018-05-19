package com.example.vcare.vcare.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainPageDetail {

    @SerializedName("FuelLevel")
    @Expose
    private String fuelLevel;
    @SerializedName("Mileage")
    @Expose
    private String mileage;
    @SerializedName("Voltage")
    @Expose
    private String voltage;
    @SerializedName("Picture")
    @Expose
    private String picture;

    public String getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(String fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
