package com.example.vcare.vcare.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by PepoManZ on 2/15/2018.
 */
public class Response {

    @SerializedName("load")
    @Expose
    private String load;
    @SerializedName("temp")
    @Expose
    private String temp;
    @SerializedName("rpm")
    @Expose
    private String rpm;
    @SerializedName("volt")
    @Expose
    private String volt;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("fuel")
    @Expose
    private String fuel;
    @SerializedName("distance")
    @Expose
    private String distance;

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getRpm() {
        return rpm;
    }

    public void setRpm(String rpm) {
        this.rpm = rpm;
    }

    public String getVolt() {
        return volt;
    }

    public void setVolt(String volt) {
        this.volt = volt;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}