package com.example.vcare.vcare.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class Alert {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("response")
    @Expose
    private Integer response;
    @SerializedName("error")
    @Expose
    private String error;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getResponse() {
        return response;
    }

    public void setResponse(Integer response) {
        this.response = response;
    }

    public Object getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
