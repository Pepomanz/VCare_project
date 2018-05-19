package com.example.vcare.vcare.dao;

import android.support.annotation.NonNull;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */
public class UserDetail {
    private String userId ;
    private String tel;
    private String lName;
    private String fName;
    private String address;
    private String username;
    private String password;
    private String email;
    private String sensorId4;
    private String sensorId5;
    private String sensorId13;
    private String sensorId49;
    private String sensorId66;
    private String brand;
    private String model;
    private String year;
    private String picture;
    private String result;

    public String getUserId() {
        return userId;
    }

    public String getTel() {
        return tel;
    }

    public String getlName() {
        return lName;
    }

    public String getfName() {
        return fName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getSensorId4() {
        return sensorId4;
    }

    public String getSensorId5() {
        return sensorId5;
    }

    public String getSensorId13() {
        return sensorId13;
    }

    public String getSensorId49() {
        return sensorId49;
    }

    public String getSensorId66() {
        return sensorId66;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getPicture() {
        return picture;
    }

    public String getResult() {
        return result;
    }

    public UserDetail(String userId, String tel, String lName, String fName, String address, String username, String password, String email, String sensorId4, String sensorId5, String sensorId13, String sensorId49, String sensorId66, String brand, String model, String year, String picture, String result) {
        this.userId = userId;
        this.tel = tel;
        this.lName = lName;
        this.fName = fName;
        this.address = address;
        this.username = username;
        this.password = password;
        this.email = email;
        this.sensorId4 = sensorId4;
        this.sensorId5 = sensorId5;
        this.sensorId13 = sensorId13;
        this.sensorId49 = sensorId49;
        this.sensorId66 = sensorId66;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.picture = picture;
        this.result = result;
    }
}
