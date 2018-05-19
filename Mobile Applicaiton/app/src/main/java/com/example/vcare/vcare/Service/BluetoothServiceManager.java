package com.example.vcare.vcare.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by VCare_kmutt on 1/25/2018.
 */

public class BluetoothServiceManager {
    private static final String TAG = "BT Manager";
    private Context context;
    private String macAddress;

    public BluetoothServiceManager (Context context)
    {
        this.context = context;
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        macAddress = shared.getString("mac","0000-0000-0000");
        Log.d(TAG,macAddress);
        if (macAddress.equals("0000-0000-0000"))
        {
            Toast.makeText(context,"Please select device first",Toast.LENGTH_LONG).show();
        }
    }

    public boolean start ()
    {
        Intent intent = new Intent(context, MyBluetoothService.class);
        intent.putExtra("mac",macAddress);
        context.startService(intent);
        return true;
    }

    public boolean stop (){
        Intent intent = new Intent(context, MyBluetoothService.class);
        context.stopService(intent);
        return true;
    }

}
