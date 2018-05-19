package com.example.vcare.vcare.http;

import com.example.vcare.vcare.dao.Alert;
import com.example.vcare.vcare.dao.CarDetail;
import com.example.vcare.vcare.dao.LoginDetail;
import com.example.vcare.vcare.dao.MainPageDetail;
import com.example.vcare.vcare.dao.UserDetail;
import com.example.vcare.vcare.dao.WarningData;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public interface ApiService {
    @POST("getdata")
    Call<WarningData> loadAlert(@Body CarDetail params);
    @POST("login")
    Call<String> logIn(@Body LoginDetail params);
    @POST("register")
    Call<String> register(@Body UserDetail params);
    @GET("getMainPage")
    Call<MainPageDetail> getMainPage();
}
