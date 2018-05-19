package com.example.vcare.vcare.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.vcare.vcare.activity.LoginActivity;
import com.example.vcare.vcare.dao.MainPageDetail;
import com.example.vcare.vcare.http.NetworkConnectManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchData extends AsyncTask<String, Integer, String> {
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public FetchData (Context context){
        mContext = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        shared = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = shared.edit();
        fetchData();
        return null;
    }

    public void fetchData(){
        Call<MainPageDetail> call = NetworkConnectManager.getInstance().getService().getMainPage();
        call.enqueue(new Callback<MainPageDetail>(){
            @Override
            public void onResponse(Call<MainPageDetail> call, Response<MainPageDetail> response) {
                MainPageDetail temp = response.body() ;
                if (response.isSuccessful()) {
                    editor.putString("fuelData",temp.getFuelLevel());
                    editor.putString("mileageData",temp.getMileage());
                    editor.putString("batteryData",temp.getVoltage());
                    editor.putString("picture",temp.getPicture());
                    editor.commit();
                }
                else
                {
                    Log.d("MainPage", response.toString());
                }
            }

            @Override
            public void onFailure(Call<MainPageDetail> call, Throwable t) {
                Log.d("MainPage", t.toString());
            }
        });
    }
}
