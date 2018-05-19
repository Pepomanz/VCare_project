package com.example.vcare.vcare.database;

import android.os.AsyncTask;

import com.example.vcare.vcare.dao.CarDetail;
import com.example.vcare.vcare.dao.UserDetail;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class InsertData extends AsyncTask<AppDatabase,Void,Boolean> {
    String data;
    public InsertData(String data){
        this.data = data;
    }
    @Override
    protected Boolean doInBackground(AppDatabase... mDb) {
        CarDetail temp = new CarDetail(data);
        ///mDb[0].carModel().insertCarDetail(temp);
        return null;
    }
}