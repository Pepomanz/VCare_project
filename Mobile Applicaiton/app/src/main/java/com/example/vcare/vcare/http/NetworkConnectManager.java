package com.example.vcare.vcare.http;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class NetworkConnectManager {
    private static  NetworkConnectManager instance;
    private ApiService service;
    private String url = "http://52.221.200.1:8081";
    public  static  NetworkConnectManager getInstance(){
        if(instance == null)
            instance = new  NetworkConnectManager();
        return instance;
    }
    public NetworkConnectManager() {
        OkHttpClient client = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager));
        oktHttpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url+"/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(oktHttpClient.build())
                .build();
        service = retrofit.create(ApiService.class);
    }
    public  ApiService getService(){
        return service;
    }
}

