package com.example.vcare.vcare.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.vcare.vcare.activity.LoginActivity;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class MyAlarmService extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("flag",1);
        editor.commit();
        Intent i = new Intent(context, UpdateDataService.class);
        context.startService(i);
    }
}
